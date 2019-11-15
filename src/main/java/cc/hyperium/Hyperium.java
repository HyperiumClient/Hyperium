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
import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.Priority;
import cc.hyperium.event.client.GameShutDownEvent;
import cc.hyperium.event.client.InitializationEvent;
import cc.hyperium.event.client.PreInitializationEvent;
import cc.hyperium.gui.ColourOptions;
import cc.hyperium.gui.ConfirmationPopup;
import cc.hyperium.gui.NotificationCenter;
import cc.hyperium.handlers.HyperiumHandlers;
import cc.hyperium.handlers.handlers.purchase.ChargebackStopper;
import cc.hyperium.internal.addons.AddonBootstrap;
import cc.hyperium.internal.addons.AddonManifest;
import cc.hyperium.internal.addons.AddonMinecraftBootstrap;
import cc.hyperium.mixins.client.MixinMinecraft;
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
import cc.hyperium.utils.StaffUtils;
import cc.hyperium.utils.UpdateUtils;
import cc.hyperium.utils.mods.AddonCheckerUtil;
import cc.hyperium.utils.mods.CompactChat;
import cc.hyperium.utils.mods.FPSLimiter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.crash.CrashReport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.*;
import java.util.List;

@SuppressWarnings("all")
public class Hyperium {

    // Create an instance so that anything in this class can be used from outside of it
    public static final Hyperium INSTANCE = new Hyperium();

    // Create a logger to differentiate errors
    public static final Logger LOGGER = LogManager.getLogger(Metadata.getModid());

    // Create a folder instance to store everything inside of
    public static final File folder = new File("hyperium");

    // Create a config file to store all settings inside of
    public static final DefaultConfig CONFIG = new DefaultConfig(new File(folder, "CONFIG.json"));

    // Sent to the server
    public static int BUILD_ID = -1;

    // Used to determine if the user is on a beta version
    public static boolean IS_BETA;

    // Is the user on the latest version?
    public boolean isLatestVersion;

    // Has the user accepted the TOS?
    private boolean acceptedTos;

    // Is Optifine installed?
    private boolean optifineInstalled;

    // Is the user in a developer environment?
    public boolean isDevEnv;

    // Is it the users first launch?
    private boolean firstLaunch;

    // Track the users amount of Hypixel coins to be used somewhere (ChromaHUD module for example)
    private final GeneralStatisticsTracking statTrack = new GeneralStatisticsTracking();

    // Discord Rich Presence, displays information such as the server, the username, etc
    private final DiscordPresence richPresenceManager = new DiscordPresence();

    // Ingame popups for events such as Friend Requests or Party requests on Hypixel
    private final ConfirmationPopup confirmation = new ConfirmationPopup();

    // Hyperium's custom Notification system
    private NotificationCenter notification;

    // Hyperium's Cosmetics system
    private HyperiumCosmetics cosmetics;

    // A class used to get common handlers
    private HyperiumHandlers handlers;

    // Hyperium's Mod Integration system
    private HyperiumModIntegration modIntegration;

    // Hyperium's Addon Integration system
    private InternalAddons internalAddons;

    // Hyperium's network system, used to send things such as emotes across servers to be displayed on other users screens
    private NetworkHandler networkHandler;

    // Hyperium's netty system
    private NettyClient client;

    // Common utilities for Sk1er's mods
    private Sk1erMod sk1erMod;

    // Expose the updateUtils
    private UpdateUtils updateUtils = UpdateUtils.INSTANCE;

    private long launchTime;

    /**
     * Register things such as Languages to be used throughout the game.
     *
     * @param event Fired on startup, before screen is displayed {@link PreInitializationEvent}
     */
    @InvokeEvent
    public void preinit(PreInitializationEvent event) {
        EventBus.INSTANCE.register(new AutoGG());

        HyperiumLocale.registerHyperiumLang("af_ZA");
        HyperiumLocale.registerHyperiumLang("ar_SA");
        HyperiumLocale.registerHyperiumLang("bs_BA");
        HyperiumLocale.registerHyperiumLang("de_DE");
        HyperiumLocale.registerHyperiumLang("en_US");
        HyperiumLocale.registerHyperiumLang("ga_IE");
        HyperiumLocale.registerHyperiumLang("ja_JP");
    }

    /**
     * Initialize all local variables
     * <p>
     * Create / check for important things that need to be loaded
     * before the client officially allows the player to use it.
     *
     * @param event Fired on startup, after screen is displayed {@link InitializationEvent}
     */
    @InvokeEvent(priority = Priority.HIGH)
    public void init(InitializationEvent event) {
        try {
            // Create the network handler, register it in config, then check for a LoginReply
            Multithreading.runAsync(() -> {
                networkHandler = new NetworkHandler();
                CONFIG.register(networkHandler);
                this.client = new NettyClient(networkHandler);
                UniversalNetty.getInstance().getPacketManager().register(new LoginReplyHandler());
            });

            // Initialize notifications
            notification = new NotificationCenter();

            // Get the build id
            createBuildId();
            Hyperium.LOGGER.info("Hyperium Build ID: {}", BUILD_ID);

            // Check for if the user is in a developers environment
            checkForDevEnvironment();

            // Initialize cosmetics
            cosmetics = new HyperiumCosmetics();

            // If it's the users first launch, create a folder to store their lock file in
            firstLaunch = new File(folder.getAbsolutePath() + "/accounts").mkdirs();

            // Determine if the users ever charge backed, if they have, they won't be allowed to launch
            new ChargebackStopper();

            // Create a lock file if the user accepts the TOS
            this.acceptedTos = new File(
                folder.getAbsolutePath() + "/accounts/" + Minecraft.getMinecraft().getSession()
                    .getPlayerID() + ".lck").exists();

            SplashProgress.setProgress(5, I18n.format("splashprogress.loadinghandlers"));

            // Initialize handlers
            handlers = new HyperiumHandlers();
            handlers.postInit();

            SplashProgress.setProgress(6, I18n.format("splashprogress.registeringlisteners"));

            // Register events
            EventBus.INSTANCE.register(new ToggleSprintContainer());
            EventBus.INSTANCE.register(notification);
            EventBus.INSTANCE.register(CompactChat.getInstance());
            EventBus.INSTANCE.register(CONFIG.register(FPSLimiter.getInstance()));
            EventBus.INSTANCE.register(confirmation);
            EventBus.INSTANCE.register(statTrack);
            CONFIG.register(statTrack);
            CONFIG.register(new ToggleSprintContainer());

            SplashProgress.setProgress(7, I18n.format("splashprogress.startinghyperium"));
            LOGGER.info("[Hyperium] Started!");

            // Set the window title
            Display.setTitle("Hyperium " + Metadata.getVersion());

            SplashProgress.setProgress(8, I18n.format("splashprogress.registeringconfiguration"));

            // Register the settings
            Settings.register();
            CONFIG.register(new ColourOptions());
            SplashProgress.setProgress(9, I18n.format("splashprogress.registeringcommands"));

            // Register all the default commands
            registerCommands();

            // Initialize the Purchase API
            EventBus.INSTANCE.register(PurchaseApi.getInstance());

            SplashProgress.setProgress(10, I18n.format("splashprogress.loadingintegrations"));

            // Register mods & addons
            modIntegration = new HyperiumModIntegration();
            internalAddons = new InternalAddons();

            // Fetch Hyperium staff members
            fetchStaffMembers();

            // Add a thread for shutdowns
            Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));

            // Load
            richPresenceManager.load();

            // Check if the user has accepted the TOS, if they have, check the Hyperium status
            if (acceptedTos) {
                sk1erMod = new Sk1erMod("hyperium", Metadata.getVersion(), object -> {
                    if (object.has("enabled") && !object.optBoolean("enabled")) {
                        handlers.getHyperiumCommandHandler().clear();
                    }
                });
                sk1erMod.checkStatus();
            }

            SplashProgress.setProgress(11, I18n.format("splashprogress.finishing"));

            // Load the previous chat session
            loadPreviousChatFile();

            // Fetch the current version
            fetchVersion();

            // Check if the user is running Optifine
            if (AddonCheckerUtil.isUsingOptifine()) {
                // 🦀
                optifineInstalled = true;
            }

            // Print every loaded addon
            collectAddons();

            LOGGER.info("Hyperium loaded in {} seconds", (System.currentTimeMillis() - launchTime) / 1000F);
        } catch (Throwable t) {
            // If an issue is caught, crash the game
            Minecraft.getMinecraft().crashed(new CrashReport("Hyperium Startup Failure", t));
        }
    }

    /**
     * Register Hyperium commands
     */
    private void registerCommands() {
        HyperiumCommandHandler hyperiumCommandHandler = handlers.getHyperiumCommandHandler();
        hyperiumCommandHandler.registerCommand(new CommandBossbarGui());
        hyperiumCommandHandler.registerCommand(new CommandClearChat());
        hyperiumCommandHandler.registerCommand(new CommandConfigGui());
        hyperiumCommandHandler.registerCommand(new CommandCoords());
        hyperiumCommandHandler.registerCommand(new CommandDebug());
        hyperiumCommandHandler.registerCommand(new CommandDisableCommand());
        hyperiumCommandHandler.registerCommand(new CommandGarbageCollect());
        hyperiumCommandHandler.registerCommand(new CommandGuild());
        hyperiumCommandHandler.registerCommand(new CommandKeybinds());
        hyperiumCommandHandler.registerCommand(new CommandLogs());
        hyperiumCommandHandler.registerCommand(new CommandMessage());
        hyperiumCommandHandler.registerCommand(new CommandNameHistory());
        hyperiumCommandHandler.registerCommand(new CommandParticleAuras());
        hyperiumCommandHandler.registerCommand(new CommandParty());
        hyperiumCommandHandler.registerCommand(new CommandPing());
        hyperiumCommandHandler.registerCommand(new CommandStats());
        hyperiumCommandHandler.registerCommand(new CustomLevelheadCommand());
    }

    /**
     * Called on {@link MixinMinecraft#shutdown(CallbackInfo)}
     */
    private void shutdown() {
        // Close the Netty client
        client.close();

        // Save config
        CONFIG.save();

        // Close Discord Rich Presence
        richPresenceManager.shutdown();

        // Create the previous chat file
        createPreviousChatFile();

        // Post an event telling the client that it's shutting down
        EventBus.INSTANCE.post(new GameShutDownEvent());
        LOGGER.info("Shutting down Hyperium..");
    }

    /**
     * Create the lock file
     */
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

    /**
     * Official game uses Notch mappings making class names to appear as "a", "b", etc,
     * deobfuscated environment uses Searge mappings making class names to appear as a readable
     * / understandable name, so check the client to see if a deobfuscated class name can be found
     */
    private void checkForDevEnvironment() {
        try {
            Class.forName("net.minecraft.dispenser.BehaviorProjectileDispense");
            isDevEnv = true;
        } catch (ClassNotFoundException e) {
            isDevEnv = false;
        }
    }

    /**
     * Hyperium allows for custom dot colors for staff, so fetch the JSON file containing
     * all the staff members
     */
    private void fetchStaffMembers() {
        Multithreading.runAsync(() -> {
            try {
                StaffUtils.clearCache();
            } catch (IOException e) {
                e.printStackTrace();
                LOGGER.warn("[Staff] Failed to fetch staff");
            }
        });
    }

    /**
     * Check the version and determine if they're on either a Beta version or the Latest version
     */
    private void fetchVersion() {
        Multithreading.runAsync(() -> {
            isLatestVersion = updateUtils.isAbsoluteLatest();
            IS_BETA = updateUtils.isBeta();
        });
    }

    /**
     * Check the build id stored in build.txt, then apply it
     */
    private void createBuildId() {
        InputStream resourceAsStream = getClass().getResourceAsStream("/build.txt");
        try {
            if (resourceAsStream != null) {
                BufferedReader br = new BufferedReader(new InputStreamReader(resourceAsStream));
                BUILD_ID = Integer.parseInt(br.readLine());
                br.close();
                resourceAsStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Load the previous sessions chat file if it's found on startup
     */
    private void loadPreviousChatFile() {
        Multithreading.runAsync(() -> {
            if (Settings.PERSISTENT_CHAT) {
                File file = new File(folder, "chat.txt");
                if (file.exists()) {
                    try {
                        FileReader fr = new FileReader(file);
                        BufferedReader bufferedReader = new BufferedReader(fr);
                        bufferedReader.lines().forEach(line -> Minecraft.getMinecraft().ingameGUI.getChatGUI().addToSentMessages(line));
                        fr.close();
                        bufferedReader.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                Hyperium.LOGGER.debug("chat.txt not found, not restoring chat");
            }
        });
    }

    /**
     * Create the current sessions chat file on game close, to be loaded later
     * when the client is started.
     */
    private void createPreviousChatFile() {
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
    }

    /**
     * Collect all the addons currently loaded for debug purposes
     */
    private void collectAddons() {
        // check if the array is empty
        if (!AddonMinecraftBootstrap.getLoadedAddons().isEmpty()) {
            // collect manifest
            List<AddonManifest> addons = AddonBootstrap.INSTANCE.getAddonManifests();

            // for all the addons
            for (AddonManifest addon : addons) {
                // print the addon as being loaded
                LOGGER.warn("User has the addon: " + addon.getName() + ", version: " + addon.getVersion());
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

    public long getLaunchTime() {
        return launchTime;
    }

    public void setLaunchTime(long launchTime) {
        this.launchTime = launchTime;
    }
}
