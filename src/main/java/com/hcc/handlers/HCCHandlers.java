package com.hcc.handlers;

import com.hcc.HCC;
import com.hcc.event.EventBus;

public class HCCHandlers {

    private LocationHandler handler;

    public HCCHandlers() {
        register((handler = new LocationHandler()));
    }

    private void register(Object object) {
        HCC.config.register(object);
        EventBus.INSTANCE.register(object);
    }

    public LocationHandler getLocationHandler() {
        return handler;
    }
}
