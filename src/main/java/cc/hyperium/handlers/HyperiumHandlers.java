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
import cc.hyperium.handlers.handlers.BroadcastEvents;
import cc.hyperium.handlers.handlers.CommandQueue;
import cc.hyperium.handlers.handlers.FlipHandler;
import cc.hyperium.handlers.handlers.FontRendererData;
import cc.hyperium.handlers.handlers.GuiDisplayHandler;
import cc.hyperium.handlers.handlers.HyperiumNetwork;
import cc.hyperium.handlers.handlers.HypixelDetector;
import cc.hyperium.handlers.handlers.LocationHandler;
import cc.hyperium.handlers.handlers.OtherConfigOptions;
import cc.hyperium.handlers.handlers.SettingsHandler;
import cc.hyperium.handlers.handlers.StatusHandler;
import cc.hyperium.handlers.handlers.ValueHandler;
import cc.hyperium.handlers.handlers.animation.DabHandler;
import cc.hyperium.handlers.handlers.animation.FlossDanceHandler;
import cc.hyperium.handlers.handlers.animation.TPoseHandler;
import cc.hyperium.handlers.handlers.animation.TwerkDance;
import cc.hyperium.handlers.handlers.animation.YeetHandler;
import cc.hyperium.handlers.handlers.animation.cape.CapeHandler;
import cc.hyperium.handlers.handlers.animation.fortnite.FortniteDefaultDance;
import cc.hyperium.handlers.handlers.chat.DMChatHandler;
import cc.hyperium.handlers.handlers.chat.FriendRequestChatHandler;
import cc.hyperium.handlers.handlers.chat.GeneralChatHandler;
import cc.hyperium.handlers.handlers.chat.HyperiumChatHandler;
import cc.hyperium.handlers.handlers.chat.PartyInviteChatHandler;
import cc.hyperium.handlers.handlers.chat.QuestTrackingChatHandler;
import cc.hyperium.handlers.handlers.chat.RankedRatingChatHandler;
import cc.hyperium.handlers.handlers.chat.WinTrackingChatHandler;
import cc.hyperium.handlers.handlers.data.HypixelAPI;
import cc.hyperium.handlers.handlers.hud.VanillaEnhancementsHud;
import cc.hyperium.handlers.handlers.hypixel.HypixelGuiAugmenter;
import cc.hyperium.handlers.handlers.keybinds.KeyBindHandler;
import cc.hyperium.handlers.handlers.mixin.LayerDeadmau5HeadHandler;
import cc.hyperium.handlers.handlers.particle.ParticleAuraHandler;
import cc.hyperium.handlers.handlers.reach.ReachDisplay;
import cc.hyperium.handlers.handlers.stats.StatsHandler;
import cc.hyperium.handlers.handlers.tracking.HypixelValueTracking;
import cc.hyperium.mods.common.PerspectiveModifierHandler;
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
    private CapeHandler capeHandler;
    private ValueHandler valueHandler;
    private List<HyperiumChatHandler> chatHandlers;
    private GeneralChatHandler generalChatHandler;
    private HypixelAPI dataHandler;
    private ResolutionUtil resolutionUtil;
    private StatusHandler statusHandler;
    private GuiDisplayHandler guiDisplayHandler;
    private KeyBindHandler keybindHandler;
    private HyperiumCommandHandler commandHandler;
    private HyperiumNetwork network;
    private ScoreboardRenderer scoreboardRenderer;
    private OtherConfigOptions configOptions;
    private DabHandler dabHandler;
    private FlossDanceHandler flossDanceHandler;
    private ParticleAuraHandler particleAuraHandler;
    private VanillaEnhancementsHud vanillaEnhancementsHud;
    private QuestTrackingChatHandler questTracking;
    private ReachDisplay reachDisplay;
    private FlipHandler flipHandler;
    private LayerDeadmau5HeadHandler layerDeadmau5HeadHandler;
    private PerspectiveModifierHandler perspectiveHandler;
    private TPoseHandler tPoseHandler;
    private FortniteDefaultDance fortniteDefaultDance;
    private HypixelGuiAugmenter hypixelGuiAugmenter;
    private TwerkDance twerkDance;
    private StatsHandler statsHandler;
    private BroadcastEvents broadcastEvents;
    private HypixelValueTracking hypixelValueTracking;
    private SettingsHandler settingsHandler;
    private YeetHandler yeetHandler;

    public HyperiumHandlers() {
        System.out.println("[Handlers] Loading handlers");
        register(network = new HyperiumNetwork());
        settingsHandler = new SettingsHandler();
        chatHandlers = new ArrayList<>();
        register(configOptions = new OtherConfigOptions());
        register(FontRendererData.INSTANCE);
        register(generalChatHandler = new GeneralChatHandler(chatHandlers));
        register(perspectiveHandler = new PerspectiveModifierHandler());
        register(keybindHandler = new KeyBindHandler());
        register(hypixelDetector = new HypixelDetector());
        register(flipHandler = new FlipHandler());
        register(reachDisplay = new ReachDisplay());
        register(locationHandler = new LocationHandler());
        register(vanillaEnhancementsHud = new VanillaEnhancementsHud());
        register(valueHandler = new ValueHandler());
        register(layerDeadmau5HeadHandler = new LayerDeadmau5HeadHandler());
        register(hypixelValueTracking = new HypixelValueTracking());
        register(resolutionUtil = new ResolutionUtil());
        register(capeHandler = new CapeHandler());
        register(guiDisplayHandler = new GuiDisplayHandler());
        register(scoreboardRenderer = new ScoreboardRenderer());
        register(dabHandler = new DabHandler());
        register(twerkDance = new TwerkDance());
        register(particleAuraHandler = new ParticleAuraHandler());
        register(yeetHandler = new YeetHandler());
        register(hypixelGuiAugmenter = new HypixelGuiAugmenter());
        register(statusHandler = new StatusHandler());
        register(flossDanceHandler = new FlossDanceHandler());
        register(tPoseHandler = new TPoseHandler());
        register(fortniteDefaultDance = new FortniteDefaultDance());
        register(statsHandler = new StatsHandler());
        register(broadcastEvents = new BroadcastEvents());
        commandQueue = new CommandQueue();
        dataHandler = new HypixelAPI();
        // Chat Handlers
        System.out.println("Loading chat handlers");
        registerChatHandler(new RankedRatingChatHandler());
        registerChatHandler(new DMChatHandler());
        registerChatHandler(questTracking = new QuestTrackingChatHandler());
        registerChatHandler(new WinTrackingChatHandler());
        registerChatHandler(new FriendRequestChatHandler());
        registerChatHandler(new PartyInviteChatHandler());
        System.out.println("[Handlers] Registering events");
        EventBus.INSTANCE.register(this);
        System.out.println("[Handlers] Done");

        // Command Handler
        register(commandHandler = new HyperiumCommandHandler());
    }

    public YeetHandler getYeetHandler() {
        return yeetHandler;
    }

    public void postInit() {
        generalChatHandler.post();
    }

    public HypixelValueTracking getHypixelValueTracking() {
        return hypixelValueTracking;
    }

    public HypixelGuiAugmenter getHypixelGuiAugmenter() {
        return hypixelGuiAugmenter;
    }

    public StatsHandler getStatsHandler() {
        return statsHandler;
    }

    public List<HyperiumChatHandler> getChatHandlers() {
        return chatHandlers;
    }

    public HyperiumCommandHandler getCommandHandler() {
        return commandHandler;
    }

    public HyperiumNetwork getNetwork() {
        return network;
    }

    public ParticleAuraHandler getParticleAuraHandler() {
        return particleAuraHandler;
    }

    public VanillaEnhancementsHud getVanillaEnhancementsHud() {
        return vanillaEnhancementsHud;
    }

    public FlipHandler getFlipHandler() {
        return flipHandler;
    }

    public LayerDeadmau5HeadHandler getLayerDeadmau5HeadHandler() {
        return layerDeadmau5HeadHandler;
    }

    public TwerkDance getTwerkDance() {
        return twerkDance;
    }

    public BroadcastEvents getBroadcastEvents() {
        return broadcastEvents;
    }

    public SettingsHandler getSettingsHandler() {
        return settingsHandler;
    }

    private void registerChatHandler(HyperiumChatHandler chatHandler) {
        register(chatHandler);
        chatHandlers.add(chatHandler);
    }

    @InvokeEvent
    public void tick(TickEvent event) {
        // Runs first tick
        IntegratedServer integratedServer = Minecraft.getMinecraft().getIntegratedServer();
        if (integratedServer == null) {
            return;
        }
        ICommandManager commandManager = integratedServer.getCommandManager();
        if (commandManager == null) {
            return;
        }
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

    public HypixelAPI getDataHandler() {
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


    public TPoseHandler gettPoseHandler() {
        return tPoseHandler;
    }

    public FortniteDefaultDance getFortniteDefaultDance() {
        return fortniteDefaultDance;
    }

    public HyperiumCommandHandler getHyperiumCommandHandler() {
        return commandHandler;
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

    public CapeHandler getCapeHandler() {
        return capeHandler;
    }

    public StatusHandler getStatusHandler() {
        return statusHandler;
    }

    public ReachDisplay getReachDisplay() {
        return reachDisplay;
    }

    public PerspectiveModifierHandler getPerspectiveHandler() {
        return perspectiveHandler;
    }

    public TPoseHandler getTPoseHandler() {
        return tPoseHandler;
    }
}
