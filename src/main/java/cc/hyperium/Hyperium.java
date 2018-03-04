/*
 * Hyperium Client, Free client with huds and popular mod
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

package cc.hyperium;


import cc.hyperium.commands.defaults.*;
import cc.hyperium.config.DefaultConfig;
import cc.hyperium.event.*;
import cc.hyperium.event.minigames.MinigameListener;
import cc.hyperium.gui.NameHistoryGui;
import cc.hyperium.gui.NotificationCenter;
import cc.hyperium.gui.integrations.HypixelFriendsGui;
import cc.hyperium.gui.settings.items.AnimationSettings;
import cc.hyperium.gui.settings.items.BackgroundSettings;
import cc.hyperium.gui.settings.items.GeneralSetting;
import cc.hyperium.handlers.HyperiumHandlers;
import cc.hyperium.handlers.handlers.keybinds.KeyBindHandler;
import cc.hyperium.integrations.spotify.Spotify;
import cc.hyperium.integrations.spotify.impl.SpotifyInformation;
import cc.hyperium.mods.HyperiumModIntegration;
import cc.hyperium.mods.capturex.CaptureCore;
import cc.hyperium.mods.crosshair.CrosshairMod;
import cc.hyperium.mods.discord.RichPresenceManager;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.mods.statistics.GeneralStatisticsTracking;
import cc.hyperium.tray.TrayManager;
import cc.hyperium.utils.ChatColor;
import cc.hyperium.utils.mods.CompactChat;
import cc.hyperium.utils.mods.FPSLimiter;
import cc.hyperium.utils.mods.PerspectiveModifierContainer;
import cc.hyperium.utils.mods.ToggleSprintContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Hypixel Community Client
 */
public class Hyperium {

    public static final Hyperium INSTANCE = new Hyperium();
    /**
     * Instance of the global mod LOGGER
     */
    public final static Logger LOGGER = LogManager.getLogger(Metadata.getModid());
    public static File folder = new File("hyperium");
    /**
     * Instance of default CONFIG
     */
    public static final DefaultConfig CONFIG = new DefaultConfig(new File(folder, "CONFIG.json"));
    public static PerspectiveModifierContainer perspective;
    public static GeneralStatisticsTracking statTrack = new GeneralStatisticsTracking();
    private final NotificationCenter notification = new NotificationCenter();

    private RichPresenceManager richPresenceManager = new RichPresenceManager();

    private TrayManager trayManager;
    private HyperiumHandlers handlers;
    private HyperiumModIntegration modIntegration;
    
    private CaptureCore captureCore;
    
    private boolean acceptedTos = false;
    
    /**
     * @param event initialize Hyperium
     */
    @InvokeEvent
    public void init(InitializationEvent event) {
        Minecraft.getMinecraft().mcProfiler.profilingEnabled = true;
        new File(folder.getAbsolutePath() + "/accounts").mkdirs();
        acceptedTos = new File(folder.getAbsolutePath() + "/accounts/" + Minecraft.getMinecraft().getSession().getPlayerID() + ".lck").exists();

        EventBus.INSTANCE.register(new MinigameListener());
        EventBus.INSTANCE.register(new ToggleSprintContainer());
        EventBus.INSTANCE.register(notification);
        EventBus.INSTANCE.register(captureCore = new CaptureCore(this));
        EventBus.INSTANCE.register(CompactChat.getInstance());
        EventBus.INSTANCE.register(CrosshairMod.getInstance());
        EventBus.INSTANCE.register(CONFIG.register(FPSLimiter.getInstance()));
        EventBus.INSTANCE.register(perspective = new PerspectiveModifierContainer());

        // Register statistics tracking.
        EventBus.INSTANCE.register(statTrack);
        CONFIG.register(statTrack);
        

        LOGGER.info("Hyperium Started!");
        Display.setTitle("Hyperium " + Metadata.getVersion());

        handlers = new HyperiumHandlers();
        trayManager = new TrayManager();
        try {
            trayManager.init();
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.warn("Failed to hookup TrayIcon");
        }

        // instance does not need to be saved as shit is static ^.^
        CONFIG.register(new GeneralSetting(null));
        CONFIG.register(new AnimationSettings(null));
        CONFIG.register(new BackgroundSettings(null));

        //Register commands.
        registerCommands();

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
                        Hyperium.INSTANCE.getNotification().display("Spotify", "Now playing " + info.getTrack().getTrackResource().getName(), 8);
                    }
                });
                spotify.start();
            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.warn("Failed to connect to spotify");
            }
        });
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
    }

    /**
     * register the commands
     */
    private void registerCommands() {
        Hyperium.INSTANCE.getHandlers().getHyperiumCommandHandler().registerCommand(new CommandConfigGui());
        Hyperium.INSTANCE.getHandlers().getHyperiumCommandHandler().registerCommand(new CommandPrivateMessage());
        Hyperium.INSTANCE.getHandlers().getHyperiumCommandHandler().registerCommand(new CommandClearChat());
        Hyperium.INSTANCE.getHandlers().getHyperiumCommandHandler().registerCommand(new CommandNameHistory());
    }
    
    /**
     * called when key is pressed on the keyboard
     *
     * @param event the event
     */
    @InvokeEvent
    public void onKeyPress(KeypressEvent event) {
        if (!Minecraft.getMinecraft().inGameHasFocus)
            return;
        if ((KeyBindHandler.debug.isPressed())) {
            Minecraft.getMinecraft().displayGuiScreen(new HypixelFriendsGui());
            KeyBindHandler.debug.onPress();
        }
    }

    /**
     * called when key bind is pressed
     *
     * @param event the event
     */

    @InvokeEvent
    public void onKeyBindPress(KeyBindPressEvent event) {
        if (event.getKeyCode() == KeyBindHandler.nameHistory.getKey()) {
            new NameHistoryGui().show();
        }
    }

    /**
     * called when receive friend request
     *
     * @param event the event
     */
    @InvokeEvent
    public void onFriendRequest(HypixelFriendRequestEvent event) {
        if (trayManager.getTray() != null)
            trayManager.getTray().displayMessage("Hypixel", "Friend request from " + event.getFrom(), TrayIcon.MessageType.NONE);
    }

    /**
     * called when Hyperium shutdown
     */
    private void shutdown() {
        CONFIG.save();
        richPresenceManager.shutdown();
        captureCore.shutdown();
        LOGGER.info("Shutting down Hyperium..");
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

    /**
     * adds a message into players chat
     *
     * @param msg message to add
     */
    public void sendMessage(String msg) {
        if (Minecraft.getMinecraft().thePlayer == null) return;
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(ChatColor.RED + "[Hyperium] " + ChatColor.WHITE + msg));
    }

    public void trayDisplayAboutInfo() {
        JOptionPane popup = new JOptionPane();
        JOptionPane.showMessageDialog(popup, "Hyperium is a Hypixel Based 1.8 Client developed by Sk1er, CoalOres, Cubixity, KevinPriv and boomboompower. Version: " + Metadata.getVersion() , "Hyperium - About", JOptionPane.PLAIN_MESSAGE);
    }

    public boolean isAcceptedTos() {
        return acceptedTos;
    }

    public void acceptTos() {
        acceptedTos = true;
        try {
            new File(folder.getAbsolutePath() + "/accounts/" + Minecraft.getMinecraft().getSession().getPlayerID() + ".lck").createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean fullScreen = false;
    public void toggleFullscreen(){
        boolean windowed = GeneralSetting.windowedFullScreen;
        boolean lastStateWindowed = false;
        if(System.getProperty("org.lwjgl.opengl.Window.undecorated") == null)
            System.setProperty("org.lwjgl.opengl.Window.undecorated", "false");
        if(Display.isFullscreen())
            fullScreen = true;
        if(System.getProperty("org.lwjgl.opengl.Window.undecorated").equals("true")) {
            fullScreen = true;
            lastStateWindowed = true;
        }

        fullScreen = !fullScreen;
        if(!lastStateWindowed){
            Minecraft.getMinecraft().toggleFullscreen();
            return;
        }

        if(fullScreen){

        }else{

        }


    }

}
