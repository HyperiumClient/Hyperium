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

package cc.hyperium;

import cc.hyperium.addons.InternalAddons;
import cc.hyperium.commands.HyperiumCommandHandler;
import cc.hyperium.commands.defaults.*;
import cc.hyperium.config.DefaultConfig;
import cc.hyperium.config.Settings;
import cc.hyperium.cosmetics.HyperiumCosmetics;
import cc.hyperium.event.*;
import cc.hyperium.event.minigames.MinigameListener;
import cc.hyperium.gui.BlurHandler;
import cc.hyperium.gui.ColourOptions;
import cc.hyperium.gui.ConfirmationPopup;
import cc.hyperium.gui.NotificationCenter;
import cc.hyperium.handlers.HyperiumHandlers;
import cc.hyperium.handlers.handlers.purchase.ChargebackStopper;
import cc.hyperium.handlers.handlers.stats.PlayerStatsGui;
import cc.hyperium.integrations.watchdog.ThankWatchdog;
import cc.hyperium.internal.MemoryHelper;
import cc.hyperium.mixins.IMixinMinecraft;
import cc.hyperium.mixinsimp.client.resources.HyperiumLocale;
import cc.hyperium.mods.HyperiumModIntegration;
import cc.hyperium.mods.autogg.AutoGG;
import cc.hyperium.mods.common.ToggleSprintContainer;
import cc.hyperium.mods.discord.DiscordPresence;
import cc.hyperium.mods.levelhead.command.CustomLevelheadCommand;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.mods.sk1ercommon.Sk1erMod;
import cc.hyperium.mods.statistics.GeneralStatisticsTracking;
import cc.hyperium.netty.NettyClient;
import cc.hyperium.netty.UniversalNetty;
import cc.hyperium.network.LoginReplyHandler;
import cc.hyperium.network.NetworkHandler;
import cc.hyperium.purchases.PurchaseApi;
import cc.hyperium.tray.TrayManager;
import cc.hyperium.utils.StaffUtils;
import cc.hyperium.utils.UpdateUtils;
import cc.hyperium.utils.mods.CompactChat;
import cc.hyperium.utils.mods.FPSLimiter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.crash.CrashReport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;

import java.io.*;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class Hyperium {

    /**
     * The Hyperium instance
     * anything in this class that isn't static or private can be accessed through calling Hyperium.INSTANCE
     */
    public static final Hyperium INSTANCE = new Hyperium();

    /**
     * The Hyperium logger
     * used to print any information, debug information, or errors that would occur due to Hyperium.
     */
    public static final Logger LOGGER = LogManager.getLogger(Metadata.getModid());

    /**
     * The Hyperium folder
     * typically located in .minecraft/hyperium, stores all files related to Hyperium.
     */
    public static final File FOLDER = new File("hyperium");

    /**
     * The Hyperium configuration file
     * anything registered to Hyperium.CONFIG.register(Object) is going to end up in this file.
     */
    public static final DefaultConfig CONFIG = new DefaultConfig(new File(FOLDER, "CONFIG.json"));

    /**
     * The current Hyperium build ID
     * -1 if it's the dev environment, otherwise it grabs the current version.
     */
    public static int BUILD_ID = -1;

    /**
     * By default, this is set to false. If the build version doesn't match up to the latest stable release,
     * then it's assumed to be a beta version.
     */
    public static boolean IS_BETA;

    /** Used to track stats made ingame, such as Daily/Monthly/Lifetime Coins. */
    private final GeneralStatisticsTracking statTrack = new GeneralStatisticsTracking();

    /**
     * Hyperium's Discord Rich Presence, which sends information to your Discord client,
     * such as if you're on Hypixel and what minigame you may be playing.
     */
    private final DiscordPresence richPresenceManager = new DiscordPresence();

    /**
     * If you're on Hypixel and you receive a party/duel/friend request, a popup is going to
     * appear asking if you'd like to accept or deny said request.
     */
    private final ConfirmationPopup confirmation = new ConfirmationPopup();

    /** Refer to boolean IS_BETA */
    public boolean isLatestVersion;
    private NotificationCenter notification;
    private HyperiumCosmetics cosmetics;
    private HyperiumHandlers handlers;
    private HyperiumModIntegration modIntegration;
    private MinigameListener minigameListener;
    private boolean acceptedTos = false;
    private boolean optifineInstalled = false;
    public boolean isDevEnv;
    private Sk1erMod sk1erMod;
    private NettyClient client;
    private InternalAddons internalAddons;
    private NetworkHandler networkHandler;
    private boolean firstLaunch = false;

    @InvokeEvent
    public void preinit(PreInitializationEvent event) {
        EventBus.INSTANCE.register(new AutoGG());
        HyperiumLocale.registerHyperiumLang("af_ZA");
        HyperiumLocale.registerHyperiumLang("ar_SA");
        HyperiumLocale.registerHyperiumLang("bs_BA");
        HyperiumLocale.registerHyperiumLang("en_US");
        HyperiumLocale.registerHyperiumLang("ja_JP");
    }

    @InvokeEvent(priority = Priority.HIGH)
    public void init(InitializationEvent event) {

        try {
            Multithreading.runAsync(() -> {
                networkHandler = new NetworkHandler();
                CONFIG.register(networkHandler);
                this.client = new NettyClient(networkHandler);
                UniversalNetty.getInstance().getPacketManager().register(new LoginReplyHandler());
            });

            Multithreading.runAsync(() -> new PlayerStatsGui(null));
            notification = new NotificationCenter();
            InputStream resourceAsStream = getClass().getResourceAsStream("/build.txt");
            try {
                if (resourceAsStream != null) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(resourceAsStream));
                    BUILD_ID = Integer.parseInt(br.readLine());
                    br.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println("[VERSION] Hyperium build ID: " + BUILD_ID);

            try {
                Class.forName("net.minecraft.dispenser.BehaviorProjectileDispense");
                isDevEnv = true;
            } catch (ClassNotFoundException e) {
                isDevEnv = false;
            }

            cosmetics = new HyperiumCosmetics();

            firstLaunch = new File(FOLDER.getAbsolutePath() + "/accounts").mkdirs();
            new ChargebackStopper();

            this.acceptedTos = new File(
                FOLDER.getAbsolutePath() + "/accounts/" + Minecraft.getMinecraft().getSession()
                    .getPlayerID() + ".lck").exists();

            SplashProgress.setProgress(5, I18n.format("splashprogress.loadinghandlers"));
            handlers = new HyperiumHandlers();
            handlers.postInit();

            SplashProgress.setProgress(6, I18n.format("splashprogress.registeringlisteners"));
            minigameListener = new MinigameListener();
            EventBus.INSTANCE.register(minigameListener);
            EventBus.INSTANCE.register(new ToggleSprintContainer());
            EventBus.INSTANCE.register(notification);
            EventBus.INSTANCE.register(CompactChat.getInstance());
            EventBus.INSTANCE.register(CONFIG.register(FPSLimiter.getInstance()));
            EventBus.INSTANCE.register(confirmation);
            EventBus.INSTANCE.register(new BlurHandler());
            EventBus.INSTANCE.register(new ThankWatchdog());
            EventBus.INSTANCE.register(new MemoryHelper());

            EventBus.INSTANCE.register(statTrack);
            CONFIG.register(statTrack);
            CONFIG.register(new ToggleSprintContainer());

            SplashProgress.setProgress(7, I18n.format("splashprogress.startinghyperium"));
            LOGGER.info("[Hyperium] Started!");
            Display.setTitle("Hyperium " + Metadata.getVersion());

            TrayManager trayManager = new TrayManager();

            SplashProgress.setProgress(8, I18n.format("splashprogress.initializingtrayicon"));
            try {
                trayManager.init();
            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.warn("[Tray] Failed to hookup TrayIcon");
            }

            SplashProgress.setProgress(9, I18n.format("splashprogress.registeringconfiguration"));
            Settings.register();
            Hyperium.CONFIG.register(new ColourOptions());
            SplashProgress.setProgress(10, I18n.format("splashprogress.registeringcommands"));
            registerCommands();
            EventBus.INSTANCE.register(PurchaseApi.getInstance());

            SplashProgress.setProgress(11, I18n.format("splashprogress.loadingintegrations"));
            modIntegration = new HyperiumModIntegration();
            internalAddons = new InternalAddons();

            Multithreading.runAsync(() -> {
                try {
                    StaffUtils.clearCache();
                } catch (IOException e) {
                    e.printStackTrace();
                    LOGGER.warn("[Staff] Failed to fetch staff");
                }

            });

            Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));

            richPresenceManager.load();

            if (acceptedTos) {
                sk1erMod = new Sk1erMod("hyperium", Metadata.getVersion(), object -> {
                    if (object.has("enabled") && !object.optBoolean("enabled")) {
                        getHandlers().getHyperiumCommandHandler().clear();
                    }
                });
                sk1erMod.checkStatus();
            }

            SplashProgress.setProgress(12, I18n.format("splashprogress.finishing"));

            Multithreading.runAsync(() -> {
                if (Settings.PERSISTENT_CHAT) {
                    File file = new File(FOLDER, "chat.txt");

                    if (file.exists()) {
                        try {
                            FileReader fr = new FileReader(file);
                            BufferedReader bufferedReader = new BufferedReader(fr);
                            String line;
                            while ((line = bufferedReader.readLine()) != null) {
                                Minecraft.getMinecraft().ingameGUI.getChatGUI().addToSentMessages(line);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    System.out.println("[Chat Handler] chat.txt not found, not restoring chat");
                }
            });

            Multithreading.runAsync(() -> {
                isLatestVersion = UpdateUtils.INSTANCE.isAbsoluteLatest();
                IS_BETA = UpdateUtils.INSTANCE.isBeta();
            });

            try {
                Class.forName("optifine.OptiFineTweaker");
                optifineInstalled = true;
                System.out.println("Optifine is currently installed.");
            } catch (ClassNotFoundException e) {
                optifineInstalled = false;
            }

            if (Settings.GL_ERROR_CHECKING) {
                ((IMixinMinecraft) Minecraft.getMinecraft()).setEnableGLErrorChecking(false);
            }

        } catch (Throwable t) {
            Minecraft.getMinecraft().crashed(new CrashReport("Hyperium Startup Failure", t));
        }
    }

    private void registerCommands() {
        HyperiumCommandHandler hyperiumCommandHandler = getHandlers().getHyperiumCommandHandler();
        hyperiumCommandHandler.registerCommand(new CommandConfigGui());
        hyperiumCommandHandler.registerCommand(new CustomLevelheadCommand());
        hyperiumCommandHandler.registerCommand(new CommandClearChat());
        hyperiumCommandHandler.registerCommand(new CommandBrowse());
        hyperiumCommandHandler.registerCommand(new CommandNameHistory());
        hyperiumCommandHandler.registerCommand(new CommandDebug());
        hyperiumCommandHandler.registerCommand(new CommandCoords());
        hyperiumCommandHandler.registerCommand(new CommandLogs());
        hyperiumCommandHandler.registerCommand(new CommandPing());
        hyperiumCommandHandler.registerCommand(new CommandStats());
        hyperiumCommandHandler.registerCommand(new CommandParty());
        hyperiumCommandHandler.registerCommand(new CommandGarbageCollect());
        hyperiumCommandHandler.registerCommand(new CommandMessage());
        hyperiumCommandHandler.registerCommand(new CommandParticleAuras());
        hyperiumCommandHandler.registerCommand(new CommandDisableCommand());
        hyperiumCommandHandler.registerCommand(new CommandGuild());
        hyperiumCommandHandler.registerCommand(new CommandStatistics());
        hyperiumCommandHandler.registerCommand(new CommandKeybinds());
    }

    private void shutdown() {
        client.close();
        CONFIG.save();
        richPresenceManager.shutdown();
        if (Settings.PERSISTENT_CHAT) {
            File file = new File(FOLDER, "chat.txt");
            try {
                file.createNewFile();
                FileWriter fileWriter = new FileWriter(file);
                BufferedWriter bw = new BufferedWriter(fileWriter);
                for (String s : Minecraft.getMinecraft().ingameGUI.getChatGUI().getSentMessages()) {
                    bw.write(s + "\n");
                }
                bw.close();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        EventBus.INSTANCE.post(new GameShutDownEvent());

        LOGGER.info("Shutting down Hyperium..");
    }

    public void acceptTos() {
        acceptedTos = true;
        if (sk1erMod == null) {
            sk1erMod = new Sk1erMod("hyperium", Metadata.getVersion());
            sk1erMod.checkStatus();
        }
        try {
            new File(FOLDER.getAbsolutePath() + "/accounts/" + Minecraft.getMinecraft().getSession()
                .getPlayerID() + ".lck").createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public GeneralStatisticsTracking getStatTrack() {
        return this.statTrack;
    }
    public HyperiumHandlers getHandlers() {
        return handlers;
    }
    public HyperiumModIntegration getModIntegration() {
        return modIntegration;
    }
    public NotificationCenter getNotification() {
        return notification;
    }
    public ConfirmationPopup getConfirmation() {
        return confirmation;
    }
    public HyperiumCosmetics getCosmetics() {
        return cosmetics;
    }
    public InternalAddons getInternalAddons() {
        return internalAddons;
    }
    public NetworkHandler getNetworkHandler() {
        return networkHandler;
    }
    public MinigameListener getMinigameListener() {
        return minigameListener;
    }
    public boolean isAcceptedTos() {
        return acceptedTos;
    }
    public boolean isFirstLaunch() {
        return firstLaunch;
    }
    public boolean isOptifineInstalled() {
        return optifineInstalled;
    }
    public boolean isDevEnv() {
        return this.isDevEnv;
    }
}
