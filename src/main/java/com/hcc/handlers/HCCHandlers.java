package com.hcc.handlers;

import com.hcc.HCC;
import com.hcc.event.EventBus;
import com.hcc.handlers.handlers.CommandQueue;
import com.hcc.handlers.handlers.HypixelDetector;
import com.hcc.handlers.handlers.LocationHandler;
import com.hcc.handlers.handlers.ValueHandler;
import com.hcc.handlers.handlers.chat.AutoWhoChatHandler;
import com.hcc.handlers.handlers.chat.GeneralChatHandler;
import com.hcc.handlers.handlers.chat.HCCChatHandler;
import com.hcc.handlers.handlers.chat.RankedRatingChatHandler;

import java.util.ArrayList;
import java.util.List;

public class HCCHandlers {

    private LocationHandler locationHandler;
    private HypixelDetector hypixelDetector;
    private CommandQueue commandQueue;
    private ValueHandler valueHandler;
    private List<HCCChatHandler> chatHandlers;
    private GeneralChatHandler generalChatHandler;

    public HCCHandlers() {
        register((locationHandler = new LocationHandler()));
        register((hypixelDetector = new HypixelDetector()));
        register(((valueHandler = new ValueHandler())));
        commandQueue = new CommandQueue();


        //Chat Handlers
        chatHandlers = new ArrayList<>();
        register((generalChatHandler = new GeneralChatHandler(chatHandlers)));
        registerChatHandler(new RankedRatingChatHandler());
        registerChatHandler(new AutoWhoChatHandler());
    }

    private void registerChatHandler(HCCChatHandler hccChatHandler) {
        register(hccChatHandler);
        chatHandlers.add(hccChatHandler);
    }

    private void register(Object object) {
        HCC.config.register(object);
        EventBus.INSTANCE.register(object);
    }

    public LocationHandler getLocationHandler() {
        return locationHandler;
    }

    public HypixelDetector getHypixelDetector() {
        return hypixelDetector;
    }

    public CommandQueue getCommandQueue() {
        return commandQueue;
    }

    public ValueHandler getValueHandler() {
        return valueHandler;
    }

    public GeneralChatHandler getGeneralChatHandler() {
        return generalChatHandler;
    }
}
