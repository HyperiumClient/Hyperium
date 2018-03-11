/*
 *  Hypixel Community Client, Client optimized for Hypixel Network
 *     Copyright (C) 2018  Hyperium Dev Team
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

package cc.hyperium.handlers;

import cc.hyperium.Hyperium;
import cc.hyperium.commands.HyperiumCommandHandler;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.TickEvent;
import cc.hyperium.handlers.handlers.*;
import cc.hyperium.handlers.handlers.chat.*;
import cc.hyperium.handlers.handlers.keybinds.KeyBindHandler;
import cc.hyperium.handlers.handlers.privatemessages.PrivateMessageHandler;
import cc.hyperium.handlers.handlers.remoteresources.RemoteResourcesHandler;
import cc.hyperium.mods.sk1ercommon.ResolutionUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommandManager;
import net.minecraft.server.integrated.IntegratedServer;

import java.util.ArrayList;
import java.util.List;

/**
 * A class containing most of Hyperium's internal handlers
 */
public class HyperiumHandlers {

    private LocationHandler locationHandler;
    private HypixelDetector hypixelDetector;
    private CommandQueue commandQueue;
    private ValueHandler valueHandler;
    private List<HyperiumChatHandler> chatHandlers;
    private GeneralChatHandler generalChatHandler;
    private ApiDataHandler dataHandler;
    private ResolutionUtil resolutionUtil;
    private GuiDisplayHandler guiDisplayHandler;
    private KeyBindHandler keybindHandler;
    private PrivateMessageHandler privateMessageHandler;
    private HyperiumCommandHandler commandHandler;
    private RemoteResourcesHandler remoteResourcesHandler;
    private HyperiumNetwork network;
    private RenderOptomizer renderOptomizer;
    private BrowserManager browserManager;

    public HyperiumHandlers() {
        network = new HyperiumNetwork();

        this.remoteResourcesHandler = new RemoteResourcesHandler();
        chatHandlers = new ArrayList<>();
        register(generalChatHandler = new GeneralChatHandler(chatHandlers));
        register(keybindHandler = new KeyBindHandler());
        register(locationHandler = new LocationHandler());
        register(hypixelDetector = new HypixelDetector());
        register(valueHandler = new ValueHandler());
        register(browserManager = new BrowserManager());
        register(resolutionUtil = new ResolutionUtil());
        register(guiDisplayHandler = new GuiDisplayHandler());
        register(renderOptomizer = new RenderOptomizer());

        register(privateMessageHandler = new PrivateMessageHandler());
        commandQueue = new CommandQueue();
        dataHandler = new ApiDataHandler();

        //Chat Handlers

        registerChatHandler(new RankedRatingChatHandler());
        registerChatHandler(new AutoWhoChatHandler());
        registerChatHandler(new PrivateMessageReader());
        registerChatHandler(new GuildPartyChatParser());
        EventBus.INSTANCE.register(this);

        //Command Handler

        register(commandHandler = new HyperiumCommandHandler());
    }

    public HyperiumNetwork getNetwork() {
        return network;
    }

    private void registerChatHandler(HyperiumChatHandler HyperiumChatHandler) {
        register(HyperiumChatHandler);
        chatHandlers.add(HyperiumChatHandler);
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
        EventBus.INSTANCE.unregister(HyperiumHandlers.class);

    }

    private void register(Object object) {
        Hyperium.CONFIG.register(object);
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

    public HyperiumCommandHandler getHyperiumCommandHandler() {
        return commandHandler;
    }

    public RemoteResourcesHandler getRemoteResourcesHandler() {
        return remoteResourcesHandler;
    }

    public RenderOptomizer getRenderOptomizer() {
        return renderOptomizer;
    }

    public BrowserManager getBrowserManager() {
        return browserManager;
    }
}
