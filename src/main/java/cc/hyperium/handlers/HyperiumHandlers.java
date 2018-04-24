/*
 *     Copyright (C) 2018  Hyperium <https://hyperium.cc/>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.handlers;

import cc.hyperium.Hyperium;
import cc.hyperium.commands.HyperiumCommandHandler;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.TickEvent;
import cc.hyperium.gui.ScoreboardRenderer;
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
    private TimeTrackHandler timeTrackHandler;
    private PrivateMessageHandler privateMessageHandler;
    private HyperiumCommandHandler commandHandler;
    private RemoteResourcesHandler remoteResourcesHandler;
    private HyperiumNetwork network;
    private BrowserManager browserManager;
    private ScoreboardRenderer scoreboardRenderer;
    private OtherConfigOptions configOptions;
    private DabHandler dabHandler;
    private FlossDanceHandler flossDanceHandler;
    private GameDataTracking dataTracking;
    private QuestTrackingChatHandler questTracking;
    private FlipHandler flipHandler;

    public HyperiumHandlers() {
        System.out.println("Loading handlers");
        register(network = new HyperiumNetwork());

        this.remoteResourcesHandler = new RemoteResourcesHandler();
        chatHandlers = new ArrayList<>();
        register(configOptions = new OtherConfigOptions());

        register(generalChatHandler = new GeneralChatHandler(chatHandlers));
        register(keybindHandler = new KeyBindHandler());
        register(hypixelDetector = new HypixelDetector());
        register(flipHandler = new FlipHandler());
        register(locationHandler = new LocationHandler());
        register(valueHandler = new ValueHandler());
        register(browserManager = new BrowserManager());
        register(resolutionUtil = new ResolutionUtil());
        register(guiDisplayHandler = new GuiDisplayHandler());
        register(scoreboardRenderer = new ScoreboardRenderer());
        register(dataTracking = new GameDataTracking());
        register(privateMessageHandler = new PrivateMessageHandler());
        register(dabHandler = new DabHandler());
        register(flossDanceHandler = new FlossDanceHandler());

        commandQueue = new CommandQueue();
        dataHandler = new ApiDataHandler();
        timeTrackHandler = new TimeTrackHandler();
        //Chat Handlers
        System.out.println("Loading chat handlers");
        registerChatHandler(new RankedRatingChatHandler());
        registerChatHandler(new AutoWhoChatHandler());
        registerChatHandler(new PrivateMessageReader());
        registerChatHandler(new GuildPartyChatParser());
        registerChatHandler(questTracking = new QuestTrackingChatHandler());
        registerChatHandler(new WinTrackingChatHandler());
        registerChatHandler(new FriendRequestChatHandler());
        registerChatHandler(new PartyInviteChatHandler());
        System.out.println("Registering events");
        EventBus.INSTANCE.register(this);
        System.out.println("Done");

        //Command Handler
        register(commandHandler = new HyperiumCommandHandler());
    }

    public FlipHandler getFlipHandler() {
        return flipHandler;
    }

    public void postInit() {
        generalChatHandler.post();
    }

    public GameDataTracking getDataTracking() {
        return dataTracking;
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


    public BrowserManager getBrowserManager() {
        return browserManager;
    }

    public TimeTrackHandler getTimeTrackHandler() {
        return timeTrackHandler;
    }

    public ScoreboardRenderer getScoreboardRenderer() {
        return scoreboardRenderer;
    }

    public OtherConfigOptions getConfigOptions() {
        return configOptions;
    }

    public QuestTrackingChatHandler getQuestTracking() {
        return questTracking;
    }

    public DabHandler getDabHandler() {
        return dabHandler;
    }

    public FlossDanceHandler getFlossDanceHandler() {
        return flossDanceHandler;
    }
}
