/*
 *     Copyright (C) 2018-present Hyperium <https://hyperium.cc/>
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
import cc.hyperium.commands.defaults.CommandBrowse;
import cc.hyperium.commands.defaults.CommandClearChat;
import cc.hyperium.commands.defaults.CommandConfigGui;
import cc.hyperium.commands.defaults.CommandCoords;
import cc.hyperium.commands.defaults.CommandDebug;
import cc.hyperium.commands.defaults.CommandDisableCommand;
import cc.hyperium.commands.defaults.CommandGarbageCollect;
import cc.hyperium.commands.defaults.CommandGuild;
import cc.hyperium.commands.defaults.CommandKeybinds;
import cc.hyperium.commands.defaults.CommandLogs;
import cc.hyperium.commands.defaults.CommandMessage;
import cc.hyperium.commands.defaults.CommandNameHistory;
import cc.hyperium.commands.defaults.CommandParticleAuras;
import cc.hyperium.commands.defaults.CommandParty;
import cc.hyperium.commands.defaults.CommandPing;
import cc.hyperium.commands.defaults.CommandQuests;
import cc.hyperium.commands.defaults.CommandResize;
import cc.hyperium.commands.defaults.CommandStatistics;
import cc.hyperium.commands.defaults.CommandStats;
import cc.hyperium.commands.defaults.CommandUpdate;
import cc.hyperium.commands.defaults.CustomLevelheadCommand;
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
import cc.hyperium.gui.BlurHandler;
import cc.hyperium.gui.ColourOptions;
import cc.hyperium.gui.ConfirmationPopup;
import cc.hyperium.gui.NotificationCenter;
import cc.hyperium.handlers.HyperiumHandlers;
import cc.hyperium.handlers.handlers.purchase.ChargebackStopper;
import cc.hyperium.handlers.handlers.stats.PlayerStatsGui;
import cc.hyperium.integrations.watchdog.ThankWatchdog;
import cc.hyperium.mixinsimp.client.resources.HyperiumLocale;
import cc.hyperium.mixinsimp.renderer.FontFixValues;
import cc.hyperium.mods.HyperiumModIntegration;
import cc.hyperium.mods.autofriend.command.AutofriendCommand;
import cc.hyperium.mods.autogg.AutoGG;
import cc.hyperium.mods.common.ToggleSprintContainer;
import cc.hyperium.mods.discord.DiscordPresence;
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
import cc.hyperium.utils.LaunchUtil;
import cc.hyperium.utils.StaffUtils;
import cc.hyperium.utils.UpdateUtils;
import cc.hyperium.utils.mods.CompactChat;
import cc.hyperium.utils.mods.FPSLimiter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.crash.CrashReport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Contract;
import org.lwjgl.opengl.Display;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

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

    public static int BUILD_ID = -1;
    public static boolean IS_BETA;
    private static boolean updateQueue = false;
    private final GeneralStatisticsTracking statTrack = new GeneralStatisticsTracking();
    private final DiscordPresence richPresenceManager = new DiscordPresence();
    private final ConfirmationPopup confirmation = new ConfirmationPopup();
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
    private HyperiumScheduler scheduler;

    @InvokeEvent
    public void preinit(PreInitializationEvent event) {
        EventBus.INSTANCE.register(new AutoGG());

        /* register language files */
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
            Multithreading.runAsync(() -> new PlayerStatsGui(null)); // Don't remove, we need to generate some stuff with Gl context
            notification = new NotificationCenter();
            scheduler = new HyperiumScheduler();
            InputStream resourceAsStream = getClass().getResourceAsStream("/build.txt");
            try {
                if (resourceAsStream != null) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(resourceAsStream));
                    BUILD_ID = Integer.valueOf(br.readLine());
                    br.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println("[VERSION] Hyperium build ID: " + BUILD_ID);

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
            EventBus.INSTANCE.register(new CommandUpdate());
            EventBus.INSTANCE.register(new ThankWatchdog());

            // Register statistics tracking.
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

            // instance does not need to be saved as shit is static ^.^
            SplashProgress.setProgress(9, I18n.format("splashprogress.registeringconfiguration"));
            Settings.register();
            Hyperium.CONFIG.register(new ColourOptions());
            //Register commands.
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

            //Multithreading.runAsync(Spotify::load);

            Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));

            richPresenceManager.load();

            if (acceptedTos) {
                sk1erMod = new Sk1erMod("hyperium", Metadata.getVersion(), object -> {
                    //Callback
                    if (object.has("enabled") && !object.optBoolean("enabled")) {
                        //Disable stuff
                        getHandlers().getHyperiumCommandHandler().clear();
                    }
                });
                sk1erMod.checkStatus();
            }

            SplashProgress.setProgress(12, I18n.format("splashprogress.reloadingresourcemanager"));

            Minecraft.getMinecraft().refreshResources();

            SplashProgress.setProgress(13, I18n.format("splashprogress.finishing"));

            if (FontFixValues.INSTANCE == null) {
                FontFixValues.INSTANCE = new FontFixValues();
            }

            Multithreading.runAsync(() -> {
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
                    System.out.println("[Chat Handler] chat.txt not found, not restoring chat");
                }
            });

            Multithreading.runAsync(() -> {
                isLatestVersion = UpdateUtils.INSTANCE.isAbsoluteLatest();
                IS_BETA = UpdateUtils.INSTANCE.isBeta();
            });
            // Check if OptiFine is installed.
            try {
                Class.forName("optifine.OptiFineTweaker");
                optifineInstalled = true;
                System.out.println("Optifine is currently installed.");
            } catch (ClassNotFoundException e) {
                optifineInstalled = false;
            }
        } catch (Throwable t) {
            Minecraft.getMinecraft().crashed(new CrashReport("Hyperium Startup Failure", t));
        }
    }

    /**
     * register the commands
     */
    private void registerCommands() {
        HyperiumCommandHandler hyperiumCommandHandler = getHandlers().getHyperiumCommandHandler();
        hyperiumCommandHandler.registerCommand(new CommandConfigGui());
        hyperiumCommandHandler.registerCommand(new CustomLevelheadCommand());
        hyperiumCommandHandler.registerCommand(new CommandClearChat());
        hyperiumCommandHandler.registerCommand(new CommandBrowse());
        hyperiumCommandHandler.registerCommand(new CommandNameHistory());
        hyperiumCommandHandler.registerCommand(new CommandDebug());
        hyperiumCommandHandler.registerCommand(new CommandUpdate());
        hyperiumCommandHandler.registerCommand(new CommandCoords());
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
        hyperiumCommandHandler.registerCommand(new CommandStatistics());
        hyperiumCommandHandler.registerCommand(new CommandKeybinds());
    }

    /**
     * Called when Hyperium shuts down
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

    public String getLaunchCommand(boolean copyNatives) {
        StringBuilder cmd = new StringBuilder();
        String[] command = System.getProperty("sun.java.command").split(" ");

        String javaPath = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
        cmd.append(quoteSpaces(javaPath) + " ");

        ManagementFactory.getRuntimeMXBean().getInputArguments().forEach(s -> {
            if (s.contains("library.path")) {
                String nativePath = s.split("=")[1];
                File hyperiumNativeFolder = new File(Hyperium.folder.getPath() + File.separator + "natives");
                if (copyNatives) {
                    copyNatives(nativePath, hyperiumNativeFolder);
                }

                cmd.append(quoteSpaces("-Djava.library.path=" + hyperiumNativeFolder.getAbsolutePath())).append(" ");
            } else if (!s.contains("agent")) {
                cmd.append(quoteSpaces(s)).append(" ");
            }
        });

        if (command[0].endsWith(".jar")) {
            cmd.append("-jar ").append(quoteSpaces(new File(command[0]).getPath())).append(" ");
        } else {
            cmd.append("-cp ").append(quoteSpaces(System.getProperty("java.class.path"))).append(" ").append(command[0]).append(" ");
        }
        for (int i = 1; i < command.length; i++) {
            cmd.append(quoteSpaces(command[i])).append(" ");
        }

        return cmd.toString();
    }

    public void copyNatives(String nativePath, File newFolder) {
        if (!newFolder.exists()) {
            newFolder.mkdir();
        }

        File tempNatives = new File(nativePath);
        if (!tempNatives.exists()) {
            System.out.println("[Error] Natives are missing.");
        } else {
            System.out.println("[Hyperium] Copying natives to hyperium directory.");
            try {
                for (File fileEntry : tempNatives.listFiles()) {
                    Files.copy(fileEntry.toPath(), Paths.get(newFolder.getPath() + File.separator + fileEntry.getName()), StandardCopyOption.REPLACE_EXISTING);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Contract(pure = true)
    private String quoteSpaces(String argument) {
        if (argument.contains(" ")) {
            return "\"" + argument + "\"";
        } else {
            return argument;
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
}
