package com.hcc.handlers;

import com.hcc.HCC;
import com.hcc.event.EventBus;
import com.hcc.handlers.handlers.HypixelDetector;
import com.hcc.handlers.handlers.LocationHandler;

public class HCCHandlers {

    private LocationHandler handler;
    private HypixelDetector hypixelDetector;

    public HCCHandlers() {
        register((handler = new LocationHandler()));
        register((hypixelDetector = new HypixelDetector()));
    }

    private void register(Object object) {
        HCC.config.register(object);
        EventBus.INSTANCE.register(object);
    }

    public LocationHandler getLocationHandler() {
        return handler;
    }

    public HypixelDetector getHypixelDetector() {
        return hypixelDetector;
    }
}
