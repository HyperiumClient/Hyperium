package com.hcc.handlers.handlers;

import com.hcc.event.InvokeEvent;
import com.hcc.event.ServerJoinEvent;
import com.hcc.event.ServerLeaveEvent;

import java.util.regex.Pattern;

public class HypixelDetector {

    private static HypixelDetector instance;

    public HypixelDetector(){
        instance = this;
    }

    private static final Pattern HYPIXEL_PATTERN =
            Pattern.compile("^(?:(?:(?:\\w+\\.)?hypixel\\.net)|(?:209\\.222\\.115\\.\\d{1,3}))(?::\\d{1,5})?$", Pattern.CASE_INSENSITIVE);

    private boolean hypixel = false;

    @InvokeEvent
    public void serverJoinEvent(ServerJoinEvent event) {
        hypixel = HYPIXEL_PATTERN.matcher(event.getServer()).find();
    }

    @InvokeEvent
    public void serverLeaveEvent(ServerLeaveEvent event) {
        hypixel = false;
    }

    public boolean isHypixel() {
        return hypixel;
    }

    public static HypixelDetector getInstance(){
        return instance;
    }
}
