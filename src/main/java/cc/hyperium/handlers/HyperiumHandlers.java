/*
 *       Copyright (C) 2018-present Hyperium <https://hyperium.cc/>
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU Lesser General Public License as published
 *       by the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU Lesser General Public License for more details.
 *
 *       You should have received a copy of the GNU Lesser General Public License
 *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.handlers;

import cc.hyperium.Hyperium;
import cc.hyperium.commands.HyperiumCommandHandler;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.client.TickEvent;
import cc.hyperium.event.network.server.hypixel.minigames.MinigameListener;
import cc.hyperium.gui.BlurHandler;
import cc.hyperium.gui.ScoreboardRenderer;
import cc.hyperium.handlers.handlers.*;
import cc.hyperium.handlers.handlers.animation.*;
import cc.hyperium.handlers.handlers.chat.*;
import cc.hyperium.handlers.handlers.data.HypixelAPI;
import cc.hyperium.handlers.handlers.fov.FovModifier;
import cc.hyperium.handlers.handlers.hud.VanillaEnhancementsHud;
import cc.hyperium.handlers.handlers.hypixel.HypixelGuiAugmenter;
import cc.hyperium.handlers.handlers.keybinds.KeyBindHandler;
import cc.hyperium.handlers.handlers.mixin.LayerDeadmau5HeadHandler;
import cc.hyperium.handlers.handlers.particle.ParticleAuraHandler;
import cc.hyperium.handlers.handlers.reach.ReachDisplay;
import cc.hyperium.handlers.handlers.stats.StatsHandler;
import cc.hyperium.integrations.watchdog.ThankWatchdog;
import cc.hyperium.internal.MemoryHelper;
import cc.hyperium.internal.UpdateChecker;
import cc.hyperium.mods.common.PerspectiveModifierHandler;
import cc.hyperium.mods.sk1ercommon.ResolutionUtil;
import cc.hyperium.utils.MouseListener;
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
    private HypixelGuiAugmenter hypixelGuiAugmenter;
    private TwerkDance twerkDance;
    private StatsHandler statsHandler;
    private BroadcastEvents broadcastEvents;
    private SettingsHandler settingsHandler;
    private YeetHandler yeetHandler;
    private BlurHandler blurHandler;
    private ThankWatchdog thankWatchdog;
    private MemoryHelper memoryHelper;
    private MinigameListener minigameListener;
    private UpdateChecker updateChecker;
    private MouseListener mouseListener;
    private FovModifier fovModifier;

    public HyperiumHandlers() {
        Hyperium.LOGGER.info("Loading handlers");
        register(network = new HyperiumNetwork());
        settingsHandler = new SettingsHandler();
        chatHandlers = new ArrayList<>();
        register(configOptions = new OtherConfigOptions());
        register(generalChatHandler = new GeneralChatHandler(chatHandlers));
        register(perspectiveHandler = new PerspectiveModifierHandler());
        register(keybindHandler = new KeyBindHandler());
        register(hypixelDetector = new HypixelDetector());
        register(flipHandler = new FlipHandler());
        register(reachDisplay = new ReachDisplay());
        register(locationHandler = new LocationHandler());
        register(vanillaEnhancementsHud = new VanillaEnhancementsHud());
        register(layerDeadmau5HeadHandler = new LayerDeadmau5HeadHandler());
        register(resolutionUtil = new ResolutionUtil());
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
        register(statsHandler = new StatsHandler());
        register(broadcastEvents = new BroadcastEvents());
        register(blurHandler = new BlurHandler());
        register(thankWatchdog = new ThankWatchdog());
        register(memoryHelper = new MemoryHelper());
        register(minigameListener = new MinigameListener());
        register(updateChecker = new UpdateChecker());
        register(mouseListener = new MouseListener());
        register(fovModifier = new FovModifier());
        commandQueue = new CommandQueue();
        dataHandler = new HypixelAPI();
        // Chat Handlers
        Hyperium.LOGGER.info("Loading Chat Handlers");
        registerChatHandler(new DMChatHandler());
        registerChatHandler(questTracking = new QuestTrackingChatHandler());
        registerChatHandler(new WinTrackingChatHandler());
        registerChatHandler(new FriendRequestChatHandler());
        registerChatHandler(new PartyInviteChatHandler());
        Hyperium.LOGGER.info("Registering Events");
        EventBus.INSTANCE.register(this);
        Hyperium.LOGGER.info("Finished Loading Handlers");

        // Command Handler
        register(commandHandler = new HyperiumCommandHandler());
    }

    public void postInit() {
        generalChatHandler.post();
    }

    private void registerChatHandler(HyperiumChatHandler chatHandler) {
        register(chatHandler);
        chatHandlers.add(chatHandler);
    }

    @InvokeEvent
    public void tick(TickEvent event) {
        // Runs first tick
        IntegratedServer integratedServer = Minecraft.getMinecraft().getIntegratedServer();
        if (integratedServer != null) {
            ICommandManager commandManager = integratedServer.getCommandManager();
            if (commandManager != null) {
                EventBus.INSTANCE.unregister(HyperiumHandlers.class);
            }
        }
    }

    private void register(Object object) {
        Hyperium.CONFIG.register(object);
        EventBus.INSTANCE.register(object);
    }

    public YeetHandler getYeetHandler() {
        return yeetHandler;
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
    public LocationHandler getLocationHandler() {
        return locationHandler;
    }
    public HypixelDetector getHypixelDetector() {
        return hypixelDetector;
    }
    public CommandQueue getCommandQueue() {
        return commandQueue;
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
    public TPoseHandler getTPoseHandler() {
        return tPoseHandler;
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
    public StatusHandler getStatusHandler() {
        return statusHandler;
    }
    public ReachDisplay getReachDisplay() {
        return reachDisplay;
    }
    public PerspectiveModifierHandler getPerspectiveHandler() {
        return perspectiveHandler;
    }
    public BlurHandler getBlurHandler() {
        return blurHandler;
    }
    public ThankWatchdog getThankWatchdog() {
        return thankWatchdog;
    }
    public MemoryHelper getMemoryHelper() {
        return memoryHelper;
    }
    public MinigameListener getMinigameListener() {
        return minigameListener;
    }
    private UpdateChecker getUpdateChecker() { return updateChecker; }
    public MouseListener getMouseListener() {
        return mouseListener;
    }
    public FovModifier getFovModifier() {
        return fovModifier;
    }
}
