package com.hcc.utils.mods;

import com.hcc.config.ConfigOpt;
import com.hcc.event.*;
import com.hcc.gui.settings.items.GeneralSetting;
import org.lwjgl.opengl.Display;

public class FPSLimiter {

    private static FPSLimiter instance;

    private static boolean limbo;
    private static long time = 0L;
    @ConfigOpt
    private long secondsWait = 5;

    @ConfigOpt
    private int fpsLimit = 30;
    public static FPSLimiter getInstance() {
        if (instance == null)
            instance = new FPSLimiter();
        return instance;
    }

    public static boolean shouldLimitFramerate() {
        return getInstance().limit();
    }

    public boolean limit() {
        return (!Display.isActive() || limbo) && GeneralSetting.framerateLimiterEnabled && time * 20 >= secondsWait;
    }

    @InvokeEvent(priority = Priority.LOW)
    public void onChat(ChatEvent event) {
        if (event.getChat().getUnformattedText().trim().equals("You are currently in limbo")) {
            limbo = true;
        }
    }

    @InvokeEvent
    public void tick(TickEvent event) {
        if (limbo)
            time++;
        else time = 0;
    }

    @InvokeEvent(priority = Priority.LOW)
    public void onWorldChange(SpawnpointChangeEvent event) {
        limbo = false;
    }


    public int getFpsLimit() {
        return fpsLimit;
    }
}
