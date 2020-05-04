package me.semx11.autotip.event.impl;

import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.network.server.ServerJoinEvent;
import cc.hyperium.event.network.server.ServerLeaveEvent;
import me.semx11.autotip.Autotip;
import me.semx11.autotip.core.SessionManager;
import me.semx11.autotip.core.TaskManager;
import me.semx11.autotip.core.TaskManager.TaskType;
import me.semx11.autotip.event.Event;
import me.semx11.autotip.universal.UniversalUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IChatComponent;

public class EventClientConnection implements Event {
    private final Autotip autotip;
    private final String hypixelHeader;

    private String serverIp;
    private long lastLogin;

    public EventClientConnection(Autotip autotip) {
        this.autotip = autotip;
        hypixelHeader = autotip.getGlobalSettings().getHypixelHeader();
    }

    public String getServerIp() {
        return serverIp;
    }

    public long getLastLogin() {
        return lastLogin;
    }

    public Object getHeader() {
        return Autotip.tabHeader;
    }

    public void setHeader(IChatComponent component) {
        Minecraft.getMinecraft().ingameGUI.getTabList().setHeader(component);
    }

    private void resetHeader() {
        setHeader(null);
    }

    @InvokeEvent
    public void playerLoggedIn(ServerJoinEvent event) {
        TaskManager taskManager = autotip.getTaskManager();
        SessionManager manager = autotip.getSessionManager();

        autotip.getMessageUtil().clearQueues();

        serverIp = UniversalUtil.getRemoteAddress(event).toLowerCase();
        lastLogin = System.currentTimeMillis();

        taskManager.getExecutor().execute(() -> {
            Object header;
            int attempts = 0;
            while ((header = getHeader()) == null) {
                if (attempts > 15) return;
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignored) {
                }
                attempts++;
            }

            if (UniversalUtil.getUnformattedText(header).equals(hypixelHeader)) {
                manager.setOnHypixel(true);
                if (autotip.getConfig().isEnabled()) {
                    taskManager.executeTask(TaskType.LOGIN, manager::login);
                }
            } else {
                manager.setOnHypixel(false);
            }
        });
    }

    @InvokeEvent
    public void playerLoggedOut(ServerLeaveEvent event) {
        TaskManager taskManager = autotip.getTaskManager();
        SessionManager manager = autotip.getSessionManager();
        manager.setOnHypixel(false);
        taskManager.executeTask(TaskType.LOGOUT, manager::logout);
        resetHeader();
    }
}
