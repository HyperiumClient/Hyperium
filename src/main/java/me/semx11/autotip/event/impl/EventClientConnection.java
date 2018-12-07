package me.semx11.autotip.event.impl;

import java.lang.reflect.Field;

import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.ServerJoinEvent;
import cc.hyperium.event.ServerLeaveEvent;
import me.semx11.autotip.Autotip;
import me.semx11.autotip.core.SessionManager;
import me.semx11.autotip.core.TaskManager;
import me.semx11.autotip.core.TaskManager.TaskType;
import me.semx11.autotip.event.Event;
import me.semx11.autotip.universal.ReflectionUtil;
import me.semx11.autotip.universal.UniversalUtil;
import me.semx11.autotip.util.ErrorReport;
import net.minecraft.client.gui.GuiPlayerTabOverlay;

public class EventClientConnection implements Event {

    private static final Field HEADER_FIELD = ReflectionUtil
            .findField(GuiPlayerTabOverlay.class, "field_175256_i", "header");

    private final Autotip autotip;
    private final String hypixelHeader;

    private String serverIp;
    private long lastLogin;

    public EventClientConnection(Autotip autotip) {
        this.autotip = autotip;
        this.hypixelHeader = autotip.getGlobalSettings().getHypixelHeader();
    }

    public String getServerIp() {
        return serverIp;
    }

    public long getLastLogin() {
        return lastLogin;
    }

    public Object getHeader() {
        try {
            return HEADER_FIELD.get(autotip.getMinecraft().ingameGUI.getTabList());
        } catch (IllegalAccessException | NullPointerException e) {
            ErrorReport.reportException(e);
            return null;
        }
    }

    private void resetHeader() {
        try {
            HEADER_FIELD.set(autotip.getMinecraft().ingameGUI.getTabList(), null);
        } catch (IllegalAccessException e) {
            ErrorReport.reportException(e);
        }
    }

    @InvokeEvent
    public void playerLoggedIn(ServerJoinEvent event) {
        TaskManager taskManager = autotip.getTaskManager();
        SessionManager manager = autotip.getSessionManager();

        autotip.getMessageUtil().clearQueues();

        this.serverIp = UniversalUtil.getRemoteAddress(event).toLowerCase();
        this.lastLogin = System.currentTimeMillis();

        taskManager.getExecutor().execute(() -> {
            Object header;
            int attempts = 0;
            while ((header = this.getHeader()) == null) {
                if (attempts > 15) {
                    return;
                }
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
                    taskManager.schedule(manager::checkVersions, 5);
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
        this.resetHeader();
    }

}