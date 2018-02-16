/*
 *     Hypixel Community Client, Client optimized for Hypixel Network
 *     Copyright (C) 2018  HCC Dev Team
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.hcc.handlers;

import com.hcc.HCC;
import com.hcc.event.EventBus;
import com.hcc.event.InvokeEvent;
import com.hcc.event.TickEvent;
import com.hcc.handlers.handlers.*;
import com.hcc.handlers.handlers.chat.*;
import com.hcc.handlers.handlers.keybinds.KeyBindHandler;
import com.hcc.handlers.handlers.privatemessages.PrivateMessageHandler;
import com.hcc.mods.sk1ercommon.ResolutionUtil;
import net.minecraft.client.Minecraft;
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
    private KeyBindHandler keybindHandler;
    private PrivateMessageHandler privateMessageHandler;

    public HCCHandlers() {
        register(keybindHandler = new KeyBindHandler());
        register(locationHandler = new LocationHandler());
        register(hypixelDetector = new HypixelDetector());
        register(valueHandler = new ValueHandler());
        register(resolutionUtil = new ResolutionUtil());
        register(guiDisplayHandler = new GuiDisplayHandler());
        register(privateMessageHandler = new PrivateMessageHandler());
        commandQueue = new CommandQueue();
        dataHandler = new ApiDataHandler();

        //Chat Handlers
        chatHandlers = new ArrayList<>();
        register((generalChatHandler = new GeneralChatHandler(chatHandlers)));
        registerChatHandler(new RankedRatingChatHandler());
        registerChatHandler(new AutoWhoChatHandler());
        registerChatHandler(new PrivateMessageReader());
        EventBus.INSTANCE.register(this);
    }

    private void registerChatHandler(HCCChatHandler hccChatHandler) {
        register(hccChatHandler);
        chatHandlers.add(hccChatHandler);
    }

    @InvokeEvent
    public void tick(TickEvent event) {
        //Runs first tick
        IntegratedServer integratedServer = Minecraft.getMinecraft().getIntegratedServer();
        if (integratedServer == null)
            return;
        ICommandManager commandManager = integratedServer.getCommandManager();
        if (commandManager == null)
            return;
        EventBus.INSTANCE.unregister(HCCHandlers.class);

        System.out.println("Registered");

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

    public KeyBindHandler getKeybindHandler() {
        return keybindHandler;
    }

    public PrivateMessageHandler getPrivateMessageHandler() {
        return privateMessageHandler;
    }
}
