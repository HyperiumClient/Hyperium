package com.hcc.handlers;

import com.hcc.HCC;
import com.hcc.debug.DebugCommand;
import com.hcc.event.EventBus;
import com.hcc.event.InvokeEvent;
import com.hcc.event.TickEvent;
import com.hcc.handlers.handlers.*;
import com.hcc.handlers.handlers.chat.AutoWhoChatHandler;
import com.hcc.handlers.handlers.chat.GeneralChatHandler;
import com.hcc.handlers.handlers.chat.HCCChatHandler;
import com.hcc.handlers.handlers.chat.RankedRatingChatHandler;
import com.hcc.mods.sk1ercommon.ResolutionUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandHandler;
import net.minecraft.command.ICommandManager;
import net.minecraft.server.integrated.IntegratedServer;

import java.util.ArrayList;
import java.util.List;

public class HCCHandlers {

    private LocationHandler locationHandler;
    private HypixelDetector hypixelDetector;
    private CommandQueue commandQueue;
    private ValueHandler valueHandler;
    private List<HCCChatHandler> chatHandlers;
    private GeneralChatHandler generalChatHandler;
    private ApiDataHandler dataHandler;
    private ResolutionUtil resolutionUtil;
    private GuiDisplayHandler guiDisplayHandler;
    private boolean reg = false;

    public HCCHandlers() {
        register(locationHandler = new LocationHandler());
        register(hypixelDetector = new HypixelDetector());
        register(valueHandler = new ValueHandler());
        register(resolutionUtil = new ResolutionUtil());
        register(guiDisplayHandler = new GuiDisplayHandler());
        commandQueue = new CommandQueue();
        dataHandler = new ApiDataHandler();

        //Chat Handlers
        chatHandlers = new ArrayList<>();
        register((generalChatHandler = new GeneralChatHandler(chatHandlers)));
        registerChatHandler(new RankedRatingChatHandler());
        registerChatHandler(new AutoWhoChatHandler());
        EventBus.INSTANCE.register(this);
    }

    private void registerChatHandler(HCCChatHandler hccChatHandler) {
        register(hccChatHandler);
        chatHandlers.add(hccChatHandler);
    }

    @InvokeEvent
    public void tick(TickEvent event) {
        if (reg)
            return;
        //Runs first tick
        IntegratedServer integratedServer = Minecraft.getMinecraft().getIntegratedServer();
        if (integratedServer == null)
            return;
        ICommandManager commandManager = integratedServer.getCommandManager();
        if (commandManager == null)
            return;
        ((CommandHandler) commandManager).registerCommand(new DebugCommand());
        EventBus.INSTANCE.unregister(this);
        System.out.println("Registered");

        reg = true;
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

    public ApiDataHandler getDataHandler() {
        return dataHandler;
    }

    public ResolutionUtil getResolutionUtil() {
        return resolutionUtil;
    }

    public GuiDisplayHandler getGuiDisplayHandler() {
        return guiDisplayHandler;
    }
}
