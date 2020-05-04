package me.semx11.autotip.core;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import me.semx11.autotip.Autotip;
import me.semx11.autotip.api.SessionKey;
import me.semx11.autotip.api.reply.impl.KeepAliveReply;
import me.semx11.autotip.api.reply.impl.LoginReply;
import me.semx11.autotip.api.reply.impl.LogoutReply;
import me.semx11.autotip.api.reply.impl.TipReply;
import me.semx11.autotip.api.reply.impl.TipReply.Tip;
import me.semx11.autotip.api.request.impl.KeepAliveRequest;
import me.semx11.autotip.api.request.impl.LoginRequest;
import me.semx11.autotip.api.request.impl.LogoutRequest;
import me.semx11.autotip.api.request.impl.TipRequest;
import me.semx11.autotip.chat.MessageUtil;
import me.semx11.autotip.core.TaskManager.TaskType;
import me.semx11.autotip.event.impl.EventClientConnection;
import me.semx11.autotip.stats.StatsRange;
import me.semx11.autotip.util.ErrorReport;
import me.semx11.autotip.util.HashUtil;
import net.minecraft.util.Session;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SessionManager {

    private final Autotip autotip;
    private final MessageUtil messageUtil;
    private final TaskManager taskManager;

    private final Queue<Tip> tipQueue = new ConcurrentLinkedQueue<>();

    private LoginReply reply;
    private SessionKey sessionKey;

    private boolean onHypixel;
    private boolean loggedIn;

    private long lastTipWave;
    private long nextTipWave;

    public SessionManager(Autotip autotip) {
        this.autotip = autotip;
        messageUtil = autotip.getMessageUtil();
        taskManager = autotip.getTaskManager();
    }

    public SessionKey getKey() {
        return sessionKey;
    }

    public boolean hasKey() {
        return sessionKey != null;
    }

    public boolean isOnHypixel() {
        return onHypixel;
    }

    public void setOnHypixel(boolean onHypixel) {
        this.onHypixel = onHypixel;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public long getLastTipWave() {
        return lastTipWave;
    }

    public long getNextTipWave() {
        return nextTipWave;
    }

    public void login() {
        Session session = autotip.getMinecraft().getSession();
        GameProfile profile = session.getProfile();

        String uuid = profile.getId().toString().replace("-", "");
        String serverHash = HashUtil.hash(uuid + HashUtil.getNextSalt());

        int statusCode = authenticate(session.getToken(), uuid, serverHash);
        if (statusCode / 100 != 2) {
            messageUtil.send("&cError {} during authentication: Session servers down?", statusCode);
            return;
        }

        StatsRange all = autotip.getStatsManager().getAll();
        LoginRequest request = LoginRequest.of(autotip, profile, serverHash, all.getTipsTotalInt());

        long lastLogin = autotip.getEvent(EventClientConnection.class).getLastLogin();
        long delay = lastLogin + 5000 - System.currentTimeMillis();
        delay /= 1000;

        reply = taskManager.scheduleAndAwait(request::execute, (delay < 1) ? 1 : delay);
        if (reply == null || !reply.isSuccess()) {
            messageUtil.send("&cError during login: {}", reply == null ? "null" : reply.getCause());
            return;
        }

        sessionKey = reply.getSessionKey();

        loggedIn = true;

        long keepAlive = reply.getKeepAliveRate();
        long tipWave = reply.getTipWaveRate();

        taskManager.addRepeatingTask(TaskType.KEEP_ALIVE, this::keepAlive, keepAlive, keepAlive);
        taskManager.addRepeatingTask(TaskType.TIP_WAVE, this::tipWave, 0, tipWave);
    }

    public void logout() {
        if (!loggedIn) return;
        LogoutReply reply = LogoutRequest.of(sessionKey).execute();
        if (!reply.isSuccess()) {
            Autotip.LOGGER.warn("Error during logout: {}", reply.getCause());
        }

        loggedIn = false;
        sessionKey = null;

        taskManager.cancelTask(TaskType.KEEP_ALIVE);
        tipQueue.clear();
    }

    private void keepAlive() {
        if (!onHypixel || !loggedIn) {
            taskManager.cancelTask(TaskType.KEEP_ALIVE);
            return;
        }
        KeepAliveReply r = KeepAliveRequest.of(sessionKey).execute();
        if (!r.isSuccess()) Autotip.LOGGER.warn("KeepAliveRequest failed: {}", r.getCause());
    }

    private void tipWave() {
        if (!onHypixel || !loggedIn) {
            taskManager.cancelTask(TaskType.TIP_WAVE);
            return;
        }

        lastTipWave = System.currentTimeMillis();
        nextTipWave = System.currentTimeMillis() + reply.getTipWaveRate() * 1000;

        TipReply r = TipRequest.of(sessionKey).execute();
        if (r.isSuccess()) {
            tipQueue.addAll(r.getTips());
            Autotip.LOGGER.info("Current tip queue: {}",
                StringUtils.join(tipQueue.iterator(), ", "));
        } else {
            tipQueue.addAll(TipReply.getDefault().getTips());
            Autotip.LOGGER.info("Failed to fetch tip queue, tipping 'all' instead.");
        }

        long tipCycle = reply.getTipCycleRate();
        taskManager.addRepeatingTask(TaskType.TIP_CYCLE, this::tipCycle, 0, tipCycle);
    }

    private void tipCycle() {
        if (tipQueue.isEmpty() || !onHypixel) {
            taskManager.cancelTask(TaskType.TIP_CYCLE);
            return;
        }

        Autotip.LOGGER.info("Attempting to tip: {}", tipQueue.peek().toString());
        messageUtil.sendCommand(tipQueue.poll().getAsCommand());
    }

    private int authenticate(String token, String uuid, String serverHash) {
        HttpURLConnection conn = null;
        try {
            URL url = new URL("https://sessionserver.mojang.com/session/minecraft/join");
            conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            JsonObject obj = new JsonObject();
            obj.addProperty("accessToken", token);
            obj.addProperty("selectedProfile", uuid);
            obj.addProperty("serverId", serverHash);

            byte[] jsonBytes = obj.toString().getBytes(StandardCharsets.UTF_8);

            conn.setFixedLengthStreamingMode(jsonBytes.length);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.connect();

            try (OutputStream out = conn.getOutputStream()) {
                out.write(jsonBytes);
            }

            return conn.getResponseCode();
        } catch (IOException e) {
            ErrorReport.reportException(e);
            return HttpStatus.SC_BAD_REQUEST;
        } finally {
            if (conn != null) conn.disconnect();
        }
    }
}
