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

import cc.hyperium.commands.BaseCommand;
import cc.hyperium.commands.CommandException;
import cc.hyperium.commands.defaults.*;
import cc.hyperium.config.DefaultConfig;
import cc.hyperium.cosmetics.HyperiumCosmetics;
import cc.hyperium.cosmetics.WingCosmetic;
import cc.hyperium.event.*;
import cc.hyperium.event.minigames.MinigameListener;
import cc.hyperium.gui.ConfirmationPopup;
import cc.hyperium.gui.NotificationCenter;
import cc.hyperium.gui.settings.items.AnimationSettings;
import cc.hyperium.gui.settings.items.BackgroundSettings;
import cc.hyperium.gui.settings.items.GeneralSetting;
import cc.hyperium.handlers.HyperiumHandlers;
import cc.hyperium.installer.InstallerFrame;
import cc.hyperium.integrations.spotify.Spotify;
import cc.hyperium.integrations.spotify.impl.SpotifyInformation;
import cc.hyperium.mods.HyperiumModIntegration;
import cc.hyperium.mods.autogg.AutoGG;
import cc.hyperium.mods.common.PerspectiveModifierContainer;
import cc.hyperium.mods.common.ToggleSprintContainer;
import cc.hyperium.mods.crosshair.CrosshairMod;
import cc.hyperium.mods.discord.RichPresenceManager;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.mods.sk1ercommon.Sk1erMod;
import cc.hyperium.mods.statistics.GeneralStatisticsTracking;
import cc.hyperium.netty.NettyClient;
import cc.hyperium.network.NetworkHandler;
import cc.hyperium.tray.TrayManager;
import cc.hyperium.utils.mods.CompactChat;
import cc.hyperium.utils.mods.FPSLimiter;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Hypixel Community Client
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
    public static File folder = new File("hyperium");

    /**
     * Instance of default CONFIG
     */
    public static final DefaultConfig CONFIG = new DefaultConfig(new File(folder, "CONFIG.json"));
    public static boolean updateQueue = false;

    private final GeneralStatisticsTracking statTrack = new GeneralStatisticsTracking();
    private final NotificationCenter notification = new NotificationCenter();
    private PerspectiveModifierContainer perspective;
    private RichPresenceManager richPresenceManager = new RichPresenceManager();
    private ConfirmationPopup confirmation = new ConfirmationPopup();
    private HyperiumCosmetics cosmetics;
    private TrayManager trayManager;
    private HyperiumHandlers handlers;
    private HyperiumModIntegration modIntegration;

    private MinigameListener minigameListener;
    private boolean acceptedTos = false;
    private boolean fullScreen = false;
    private boolean checkedForUpdate = false;
    private Sk1erMod sk1erMod;
    private NettyClient client;
    private NetworkHandler networkHandler;

    public MinigameListener getMinigameListener() {
        return minigameListener;
    }

    @InvokeEvent
    public void preinit(PreInitializationEvent event) {
        EventBus.INSTANCE.register(new AutoGG());
    }

    /**
     * @param event initialize Hyperium
     */

    @InvokeEvent(priority = Priority.HIGH)
    public void init(InitializationEvent event) {
        Minecraft.getMinecraft().mcProfiler.profilingEnabled = false;

        // Creates the accounts dir
        new File(folder.getAbsolutePath() + "/accounts").mkdirs();

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
        EventBus.INSTANCE.register(CrosshairMod.getInstance());
        EventBus.INSTANCE.register(CONFIG.register(FPSLimiter.getInstance()));
        //EventBus.INSTANCE.register(perspective = new PerspectiveModifierContainer());
        EventBus.INSTANCE.register(new WingCosmetic());
        EventBus.INSTANCE.register(confirmation = new ConfirmationPopup());

        // Register statistics tracking.
        EventBus.INSTANCE.register(statTrack);
        CONFIG.register(statTrack);

        SplashProgress.PROGRESS = 7;
        SplashProgress.CURRENT = "Starting Hyperium";
        SplashProgress.update();
        LOGGER.info("Hyperium Started!");
        Display.setTitle("Hyperium " + Metadata.getVersion());

        trayManager = new TrayManager();

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
        CONFIG.register(new GeneralSetting(null));
        CONFIG.register(new AnimationSettings(null));
        BackgroundSettings backgroundSettings = new BackgroundSettings(null);
        backgroundSettings.rePack();
        CONFIG.register(backgroundSettings);

        //Register commands.
        SplashProgress.PROGRESS = 10;
        SplashProgress.CURRENT = "Registering commands";
        SplashProgress.update();
        registerCommands();

        SplashProgress.PROGRESS = 11;
        SplashProgress.CURRENT = "Loading integrations";
        SplashProgress.update();
        modIntegration = new HyperiumModIntegration();
        richPresenceManager.init();

        // spotify thread (>^.^)>
        Multithreading.runAsync(() -> {
            try {
                Spotify spotify = new Spotify();
                // Uncommented by Kevin because he added the file ^.^
                spotify.addListener(new Spotify.SpotifyListener() {
                    @Override
                    public void onPlay(SpotifyInformation info) {
                        // This is on a different thread, so we need to use the static getter
                        Hyperium.INSTANCE.getNotification()
                                .display("Spotify",
                                        "Now playing " + info.getTrack().getTrackResource().getName(),
                                        8
                                ).setClickedCallback(() -> {
                                    /*try {
                                        String path = new File(Hyperium.folder, "openSpotify.vbs").getAbsolutePath();

                                        Runtime.getRuntime().exec(path);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }*/
                        });
                    }
                });
                spotify.start();
            } catch (Exception e) {
                LOGGER.warn("Failed to connect to spotify");
            }
        });

        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));

        if (acceptedTos) {
            sk1erMod = new Sk1erMod("hyperium", Metadata.getVersion(), object -> {
                //Callback
                if (object.has("enabled") && !object.optBoolean("enabled")) {
                    //Disable stuff
                    // EventBus.INSTANCE.disable(); dont think this is needed?
                    getHandlers().getHyperiumCommandHandler().clear();
                }
            });
            sk1erMod.checkStatus();
        }
        SplashProgress.PROGRESS = 12;
        SplashProgress.CURRENT = "Finished";
        SplashProgress.update();
        cosmetics = new HyperiumCosmetics();

        Multithreading.runAsync(() -> {

            networkHandler = new NetworkHandler();
            this.client = new NettyClient(networkHandler);

            while (true) {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (this.client.isAdmin()) {
                    getHandlers().getHyperiumCommandHandler().registerCommand(new BaseCommand() {
                        @Override
                        public String getName() {
                            return "hyperiumadmin";
                        }

                        @Override
                        public String getUsage() {
                            return "/hyperiumadmin";
                        }

                        @Override
                        public void onExecute(String[] args) throws CommandException {
                            StringBuilder builder = new StringBuilder();
                            Iterator<String> iterator = Arrays.stream(args).iterator();
                            while (iterator.hasNext()) {
                                builder.append(iterator.next());
                                if (iterator.hasNext())
                                    builder.append(" ");
                            }
                            client.dispatchCommand(builder.toString());

                        }
                    });
                    break;
                }
            }
        });
    }

    /**
     * register the commands
     */
    private void registerCommands() {
        getHandlers().getHyperiumCommandHandler().registerCommand(new CommandConfigGui());
        getHandlers().getHyperiumCommandHandler().registerCommand(new CommandPrivateMessage());
        getHandlers().getHyperiumCommandHandler().registerCommand(new CommandClearChat());
        getHandlers().getHyperiumCommandHandler().registerCommand(new CommandNameHistory());
        getHandlers().getHyperiumCommandHandler().registerCommand(new CommandPlayGame());
        getHandlers().getHyperiumCommandHandler().registerCommand(new CommandBrowser());
        getHandlers().getHyperiumCommandHandler().registerCommand(new CommandUpdate());
    }

    /**
     * called when Hyperium shutdown
     */
    private void shutdown() {
        CONFIG.save();
        richPresenceManager.shutdown();

        // Tell the modules the game is shutting down
        EventBus.INSTANCE.post(new GameShutDownEvent());

        LOGGER.info("Shutting down Hyperium..");

        if (updateQueue) {
            try {
                boolean windows = InstallerFrame.OsCheck.getOperatingSystemType() == InstallerFrame.OsCheck.OSType.Windows;
                //Class<?> c = getClass();
                //String n = c.getName().replace('.', '/');
                String cs = "";
                for (URL u : ((URLClassLoader) getClass().getClassLoader()).getURLs())
                    if (u.getPath().contains("Hyperium"))
                        cs = u.getPath();
                System.out.println("cs=" + cs);
                Runtime.getRuntime().exec(new String[]{
                        windows ? "cmd" : "bash",
                        windows ? "/c" : "-c",
                        "java",
                        "-jar",
                        cs,
                        Minecraft.getMinecraft().mcDataDir.getAbsolutePath()
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
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

    public PerspectiveModifierContainer getPerspective() {
        return this.perspective;
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
            new File(folder.getAbsolutePath() + "/accounts/" + Minecraft.getMinecraft().getSession().getPlayerID() + ".lck").createNewFile();
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
