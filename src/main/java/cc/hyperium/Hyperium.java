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

package cc.hyperium;

import cc.hyperium.addons.InternalAddons;
import cc.hyperium.commands.HyperiumCommandHandler;
import cc.hyperium.commands.defaults.*;
import cc.hyperium.config.DefaultConfig;
import cc.hyperium.config.Settings;
import cc.hyperium.cosmetics.HyperiumCosmetics;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.GameShutDownEvent;
import cc.hyperium.event.InitializationEvent;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.PreInitializationEvent;
import cc.hyperium.event.Priority;
import cc.hyperium.event.minigames.MinigameListener;
import cc.hyperium.gui.BlurDisableFallback;
import cc.hyperium.gui.ColourOptions;
import cc.hyperium.gui.ConfirmationPopup;
import cc.hyperium.gui.NotificationCenter;
import cc.hyperium.handlers.HyperiumHandlers;
import cc.hyperium.handlers.handlers.purchase.ChargebackStopper;
import cc.hyperium.handlers.handlers.stats.PlayerStatsGui;
import cc.hyperium.integrations.spotify.Spotify;
import cc.hyperium.mixinsimp.renderer.FontFixValues;
import cc.hyperium.mods.HyperiumModIntegration;
import cc.hyperium.mods.autofriend.command.AutofriendCommand;
import cc.hyperium.mods.autogg.AutoGG;
import cc.hyperium.mods.common.ToggleSprintContainer;
import cc.hyperium.mods.discord.RichPresenceManager;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.mods.sk1ercommon.Sk1erMod;
import cc.hyperium.mods.statistics.GeneralStatisticsTracking;
import cc.hyperium.netty.NettyClient;
import cc.hyperium.netty.UniversalNetty;
import cc.hyperium.network.LoginReplyHandler;
import cc.hyperium.network.NetworkHandler;
import cc.hyperium.purchases.PurchaseApi;
import cc.hyperium.tray.TrayManager;
import cc.hyperium.utils.HyperiumScheduler;
import cc.hyperium.utils.InstallerUtils;
import cc.hyperium.utils.LaunchUtil;
import cc.hyperium.utils.StaffUtils;
import cc.hyperium.utils.UpdateUtils;
import cc.hyperium.utils.mods.CompactChat;
import cc.hyperium.utils.mods.FPSLimiter;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Hyperium Client
 */
public class Hyperium {

    /**
     * The hyperium instance
     */
    public static final Hyperium INSTANCE = new Hyperium();
    /**
     * Instance of the global mod LOGGER
     */
    public final static Logger LOGGER = LogManager.getLogger(Metadata.getModid());
    /**
     * The Hyperium configuration folder
     */
    public static final File folder = new File("hyperium");
    /**
     * Instance of default CONFIG
     */

    public static final DefaultConfig CONFIG = new DefaultConfig(new File(folder, "CONFIG.json"));
    public static String BUILD_ID = "RELEASE " + Metadata.getVersionID();
    private static boolean updateQueue = false;
    private final GeneralStatisticsTracking statTrack = new GeneralStatisticsTracking();
    private final NotificationCenter notification = new NotificationCenter();
    private final RichPresenceManager richPresenceManager = new RichPresenceManager();
    private final ConfirmationPopup confirmation = new ConfirmationPopup();
    public boolean isLatestVersion;
    private HyperiumCosmetics cosmetics;
    private HyperiumHandlers handlers;
    private HyperiumModIntegration modIntegration;
    private MinigameListener minigameListener;
    private boolean acceptedTos = false;
    private boolean fullScreen = false;
    private boolean checkedForUpdate = false;
    private boolean optifineInstalled = false;
    private boolean isDevEnv;
    private Sk1erMod sk1erMod;
    private NettyClient client;
    private InternalAddons internalAddons;
    private NetworkHandler networkHandler;
    /**
     * @param event initialize Hyperium
     */
    private boolean firstLaunch = false;
    private HyperiumScheduler scheduler;


    @InvokeEvent
    public void preinit(PreInitializationEvent event) {
        EventBus.INSTANCE.register(new AutoGG());
    }


    @InvokeEvent(priority = Priority.HIGH)
    public void init(InitializationEvent event) {
        new PlayerStatsGui(null); //Don't remove, we need to generate some stuff with Gl context
        scheduler = new HyperiumScheduler();
        InputStream resourceAsStream = getClass().getResourceAsStream("/build.txt");
        try {
            if (resourceAsStream != null) {
                BufferedReader br = new BufferedReader(new InputStreamReader(resourceAsStream));
                BUILD_ID = br.readLine();
                br.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Hyperium running build " + BUILD_ID);
        try {
            Class.forName("net.minecraft.dispenser.BehaviorProjectileDispense"); // check for random MC class
            isDevEnv = true;
        } catch (ClassNotFoundException e) {
            isDevEnv = false;
        }
        cosmetics = new HyperiumCosmetics();

        // Creates the accounts dir
        firstLaunch = new File(folder.getAbsolutePath() + "/accounts").mkdirs();
        new ChargebackStopper();

        // Has the user accepted the TOS of the client?
        this.acceptedTos = new File(
                folder.getAbsolutePath() + "/accounts/" + Minecraft.getMinecraft().getSession()
                        .getPlayerID() + ".lck").exists();
        SplashProgress.PROGRESS = 5;
        SplashProgress.CURRENT = "Loading handlers";
        SplashProgress.update();
        handlers = new HyperiumHandlers();
        handlers.postInit();

        SplashProgress.PROGRESS = 6;
        SplashProgress.CURRENT = "Registering listeners";
        SplashProgress.update();
        minigameListener = new MinigameListener();
        EventBus.INSTANCE.register(minigameListener);
        EventBus.INSTANCE.register(new ToggleSprintContainer());
        EventBus.INSTANCE.register(notification);

        EventBus.INSTANCE.register(CompactChat.getInstance());
        EventBus.INSTANCE.register(CONFIG.register(FPSLimiter.getInstance()));
        EventBus.INSTANCE.register(confirmation);
        EventBus.INSTANCE.register(new BlurDisableFallback());
        EventBus.INSTANCE.register(new CommandUpdate());


        // Register statistics tracking.
        EventBus.INSTANCE.register(statTrack);
        CONFIG.register(statTrack);
        CONFIG.register(new ToggleSprintContainer());

        SplashProgress.PROGRESS = 7;
        SplashProgress.CURRENT = "Starting Hyperium";
        SplashProgress.update();
        LOGGER.info("Hyperium Started!");
        Display.setTitle("Hyperium " + Metadata.getVersion());

        TrayManager trayManager = new TrayManager();

        SplashProgress.PROGRESS = 8;
        SplashProgress.CURRENT = "Initializing tray icon";
        SplashProgress.update();
        try {
            trayManager.init();
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.warn("Failed to hookup TrayIcon");
        }

        // instance does not need to be saved as shit is static ^.^
        SplashProgress.PROGRESS = 9;
        SplashProgress.CURRENT = "Registering config";
        SplashProgress.update();
        Settings.register();
        Hyperium.CONFIG.register(new ColourOptions());
        //Register commands.
        SplashProgress.PROGRESS = 10;
        SplashProgress.CURRENT = "Registering commands";
        SplashProgress.update();
        registerCommands();
        EventBus.INSTANCE.register(PurchaseApi.getInstance());

        SplashProgress.PROGRESS = 11;
        SplashProgress.CURRENT = "Loading integrations";
        SplashProgress.update();
        modIntegration = new HyperiumModIntegration();
        internalAddons = new InternalAddons();
        try {
            StaffUtils.clearCache();
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.warn("Failed to fetch staff");
        }

        Multithreading.runAsync(Spotify::load);

        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));

        richPresenceManager.init();

        if (acceptedTos) {
            sk1erMod = new Sk1erMod("hyperium", Metadata.getVersion(), object -> {
                //Callback

            });
            sk1erMod.checkStatus();
        }

        SplashProgress.PROGRESS = 12;
        SplashProgress.CURRENT = "Reloading resource manager";
        SplashProgress.update();

        Minecraft.getMinecraft().refreshResources();

        SplashProgress.PROGRESS = 13;
        SplashProgress.CURRENT = "Finishing";
        SplashProgress.update();


        Multithreading.runAsync(() -> {

            networkHandler = new NetworkHandler();
            CONFIG.register(networkHandler);
            this.client = new NettyClient(networkHandler);
            UniversalNetty.getInstance().getPacketManager().register(new LoginReplyHandler());
        });
        EventBus.INSTANCE.register(FontFixValues.INSTANCE);
        if (Settings.PERSISTENT_CHAT) {
            File file = new File(folder, "chat.txt");

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
            System.out.println("Not restoring chat");
        }

        isLatestVersion = UpdateUtils.INSTANCE.isAbsoluteLatest();

        // Check if Optifine is installed.
        try {
            Class.forName("optifine.OptiFineTweaker");
            optifineInstalled = true;
            System.out.println("Optifine installation detected!");
        } catch (ClassNotFoundException e) {
            optifineInstalled = false;
        }
    }

    /**
     * register the commands
     */
    private void registerCommands() {
        HyperiumCommandHandler hyperiumCommandHandler = getHandlers().getHyperiumCommandHandler();
        hyperiumCommandHandler.registerCommand(new CommandConfigGui());
        hyperiumCommandHandler.registerCommand(new CustomLevelheadCommand());
        hyperiumCommandHandler.registerCommand(new CommandPrivateMessage());
        hyperiumCommandHandler.registerCommand(new CommandClearChat());
        hyperiumCommandHandler.registerCommand(new CommandNameHistory());
        hyperiumCommandHandler.registerCommand(new CommandPlayGame());
        hyperiumCommandHandler.registerCommand(new CommandDebug());
        hyperiumCommandHandler.registerCommand(new CommandUpdate());
        hyperiumCommandHandler.registerCommand(new CommandCoords());
        hyperiumCommandHandler.registerCommand(new DevTestCommand());
        hyperiumCommandHandler.registerCommand(new CommandLogs());
        hyperiumCommandHandler.registerCommand(new CommandPing());
        hyperiumCommandHandler.registerCommand(new CommandStats());
        hyperiumCommandHandler.registerCommand(new CommandParty());
        hyperiumCommandHandler.registerCommand(new CommandResize());
        hyperiumCommandHandler.registerCommand(new CommandGarbageCollect());
        hyperiumCommandHandler.registerCommand(new CommandMessage());
        hyperiumCommandHandler.registerCommand(new CommandParticleAuras());
        hyperiumCommandHandler.registerCommand(new CommandDisableCommand());
        hyperiumCommandHandler.registerCommand(new AutofriendCommand());
        hyperiumCommandHandler.registerCommand(new CommandQuests());
        hyperiumCommandHandler.registerCommand(new CommandGuild());
    }


    /**
     * called when Hyperium shuts down
     */
    private void shutdown() {
        CONFIG.save();
        richPresenceManager.shutdown();
        if (Settings.PERSISTENT_CHAT) {
            File file = new File(folder, "chat.txt");
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
        // Tell the modules the game is shutting down
        EventBus.INSTANCE.post(new GameShutDownEvent());

        LOGGER.info("Shutting down Hyperium..");

        if (updateQueue) {
            LaunchUtil.launch();
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

    public boolean isAcceptedTos() {
        return acceptedTos;
    }

    public void acceptTos() {
        acceptedTos = true;
        if (sk1erMod == null) {
            sk1erMod = new Sk1erMod("hyperium", Metadata.getVersion());
            sk1erMod.checkStatus();
        }
        try {
            new File(folder.getAbsolutePath() + "/accounts/" + Minecraft.getMinecraft().getSession()
                    .getPlayerID() + ".lck").createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public boolean isFirstLaunch() {
        return firstLaunch;
    }

    public boolean isOptifineInstalled() {
        return optifineInstalled;
    }

    public MinigameListener getMinigameListener() {
        return minigameListener;
    }

    public boolean isDevEnv() {
        return this.isDevEnv;
    }

    public HyperiumScheduler getScheduler() {
        return scheduler;
    }

    // Does not appear to be used
//    public void toggleFullscreen() {
//        boolean windowed = GeneralSetting.windowedFullScreen;
//        boolean lastStateWindowed = false;
//        if (System.getProperty("org.lwjgl.opengl.Window.undecorated") == null) {
//            System.setProperty("org.lwjgl.opengl.Window.undecorated", "false");
//        }
//        if (Display.isFullscreen()) {
//            fullScreen = true;
//        }
//        if (System.getProperty("org.lwjgl.opengl.Window.undecorated").equals("true")) {
//            fullScreen = true;
//            lastStateWindowed = true;
//        }
//
//        fullScreen = !fullScreen;
//        if (!lastStateWindowed) {
//            Minecraft.getMinecraft().toggleFullscreen();
//            return;
//        }
//
//        if (fullScreen) {
//
//        } else {
//
//        }
//    }
}
