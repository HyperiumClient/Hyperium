package com.hcc.utils.mods;

import com.hcc.event.ChatEvent;
import com.hcc.event.InvokeEvent;
import com.hcc.event.Priority;
import com.hcc.event.SpawnpointChangeEvent;
import com.hcc.gui.settings.items.GeneralSetting;
import org.lwjgl.opengl.Display;

public class FPSLimiter {

    private static FPSLimiter instance;

    private static boolean limbo;

    @InvokeEvent(priority = Priority.LOW)
    public void onChat(ChatEvent event) {
        if(event.getChat().getUnformattedText().trim().equals("You are currently in limbo")) {
            limbo = true;
        }
    }

    @InvokeEvent(priority = Priority.LOW)
    public void onWorldChange(SpawnpointChangeEvent event) {
        limbo = false;
    }

    public static FPSLimiter getInstance() {
        if(instance == null)
            instance = new FPSLimiter();
        return instance;
    }

    public static boolean shouldLimitFramerate() {
        return (!Display.isActive() || limbo) && GeneralSetting.framerateLimiterEnabled;
    }


}
