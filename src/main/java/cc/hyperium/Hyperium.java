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


import cc.hyperium.commands.defaults.CommandChromaHUD;
import cc.hyperium.commands.defaults.CommandClearChat;
import cc.hyperium.commands.defaults.CommandConfigGui;
import cc.hyperium.commands.defaults.CommandPrivateMessage;
import cc.hyperium.config.DefaultConfig;
import cc.hyperium.event.*;
import cc.hyperium.event.minigames.Minigame;
import cc.hyperium.event.minigames.MinigameListener;
import cc.hyperium.gui.NotificationCenter;
import cc.hyperium.gui.integrations.HypixelFriendsGui;
import cc.hyperium.gui.settings.items.AnimationSettings;
import cc.hyperium.gui.settings.items.GeneralSetting;
import cc.hyperium.handlers.HyperiumHandlers;
import cc.hyperium.handlers.handlers.keybinds.KeyBindHandler;
import cc.hyperium.integrations.spotify.Spotify;
import cc.hyperium.mixins.MixinKeyBinding;
import cc.hyperium.mods.HyperiumModIntegration;
import cc.hyperium.mods.ToggleSprintContainer;
import cc.hyperium.mods.capturex.CaptureCore;
import cc.hyperium.mods.crosshair.CrosshairMod;
import cc.hyperium.mods.discord.RichPresenceManager;
import cc.hyperium.mods.levelhead.commands.LevelHeadCommand;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.mods.statistics.GeneralStatisticsTracking;
import cc.hyperium.tray.TrayManager;
import cc.hyperium.utils.ChatColor;
import cc.hyperium.utils.mods.CompactChat;
import cc.hyperium.utils.mods.FPSLimiter;
import cc.hyperium.utils.mods.GeneralStatisticsTracking;
import cc.hyperium.utils.mods.PerspectiveModifierContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

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
    public static PerspectiveModifierContainer perspective;
    public static GeneralStatisticsTracking statTrack = new GeneralStatisticsTracking();

    /**
     * Instance of default CONFIG
     */
    public static final DefaultConfig CONFIG = new DefaultConfig(new File(folder, "CONFIG.json"));

    private final NotificationCenter notification = new NotificationCenter();

    private RichPresenceManager richPresenceManager = new RichPresenceManager();

    private TrayManager trayManager;
    private HyperiumHandlers handlers;
    private HyperiumModIntegration modIntegration;
    private Minigame currentGame;
    private CaptureCore captureCore;

    private Pattern friendRequestPattern;
    private Pattern rankBracketPattern;
    private Pattern swKillMsg;
    private Pattern bwKillMsg;
    private Pattern bwFinalKillMsg;
    private Pattern duelKillMsg;

    /**
     * @param event initialize Hyperium
     */
    private boolean acceptedTos = false;

    @InvokeEvent
    public void init(InitializationEvent event) {
        new File(folder.getAbsolutePath() + "/accounts").mkdirs();

        acceptedTos = new File(folder.getAbsolutePath() + "/accounts/" + Minecraft.getMinecraft().getSession().getPlayerID() + ".lck").exists();

        EventBus.INSTANCE.register(new MinigameListener());
        EventBus.INSTANCE.register(new ToggleSprintContainer());
        EventBus.INSTANCE.register(notification);
        EventBus.INSTANCE.register(captureCore = new CaptureCore());
        EventBus.INSTANCE.register(CompactChat.getInstance());
        EventBus.INSTANCE.register(CrosshairMod.getInstance());
        EventBus.INSTANCE.register(CONFIG.register(FPSLimiter.getInstance()));
        EventBus.INSTANCE.register(perspective = new PerspectiveModifierContainer());

        // Register statistics tracking.
        EventBus.INSTANCE.register(statTrack);
        CONFIG.register(statTrack);

        friendRequestPattern = Pattern.compile("Friend request from .+?");
        rankBracketPattern = Pattern.compile("[\\^] ");
        swKillMsg = Pattern.compile(".+? was .+? by .+?\\.");
        bwKillMsg = Pattern.compile(".+? by .+?\\.");
        bwFinalKillMsg = Pattern.compile(".+? by .+?\\. FINAL KILL!");
        duelKillMsg = Pattern.compile(".+? was kill by .+?\\.");

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

        //Register commands.
        registerCommands();

        modIntegration = new HyperiumModIntegration();
        richPresenceManager.init();
        // spotify thread (>^.^)>
        Multithreading.runAsync(() -> {
            try {
                Spotify spotify = new Spotify();
                //Commented by Sk1er because Kevin didn't include half of the files
//                spotify.addListener(new Spotify.SpotifyListener() {
//                    @Override
//                    public void onPlay(SpotifyInformation info) {
//                        notification.display("Spotify", "Now playing " + info.getTrack().getAlbumResource().getName(), 8);
//                    }
//                });
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
//       Hyperium.INSTANCE.getHandlers().getHyperiumCommandHandler().registerCommand(new TestCommand());

        Hyperium.INSTANCE.getHandlers().getHyperiumCommandHandler().registerCommand(new CommandConfigGui());
        Hyperium.INSTANCE.getHandlers().getHyperiumCommandHandler().registerCommand(new CommandPrivateMessage());
        Hyperium.INSTANCE.getHandlers().getHyperiumCommandHandler().registerCommand(new LevelHeadCommand());
        Hyperium.INSTANCE.getHandlers().getHyperiumCommandHandler().registerCommand(new CommandClearChat());
        Hyperium.INSTANCE.getHandlers().getHyperiumCommandHandler().registerCommand(new CommandChromaHUD());
    }

    /**
     * @param event called when someone sent a chat message
     */
    @InvokeEvent
    public void onChat(ChatEvent event) {
        if (friendRequestPattern.matcher(ChatColor.stripColor(event.getChat().getUnformattedText())).matches()) {
            String withoutRank = ChatColor.stripColor(event.getChat().getUnformattedText());
            withoutRank = withoutRank.replaceAll("Friend request from ", "");
            withoutRank = withoutRank.replaceAll(rankBracketPattern.pattern(), "");
            EventBus.INSTANCE.post(new HypixelFriendRequestEvent(withoutRank));
        }
        String msg = ChatColor.stripColor(event.getChat().getUnformattedText());
        if (getHandlers().getHypixelDetector().isHypixel()) {
            if (currentGame == null) {
                return;
            }
            switch (currentGame) {
                case SKYWARS:
                    if (swKillMsg.matcher(msg).matches())
                        if (msg.endsWith(Minecraft.getMinecraft().thePlayer.getName() + "."))
                            EventBus.INSTANCE.post(new HypixelKillEvent(Minigame.SKYWARS, msg.split(" ")[0]));
                    break;
                case BEDWARS:
                    if (bwKillMsg.matcher(msg).matches() || bwFinalKillMsg.matcher(msg).matches())
                        msg = msg.replace(" FINAL KILL!", "");
                    if (msg.endsWith(Minecraft.getMinecraft().thePlayer.getName() + "."))
                        EventBus.INSTANCE.post(new HypixelKillEvent(Minigame.BEDWARS, msg.split(" ")[0]));
                    break;
                case DUELS:
                    if (duelKillMsg.matcher(msg).matches())
                        if (msg.endsWith(Minecraft.getMinecraft().thePlayer.getName() + "."))
                            EventBus.INSTANCE.post(new HypixelKillEvent(Minigame.DUELS, msg.split(" ")[0]));
            }
        }


    }

    @InvokeEvent
    public void onMinigameJoin(JoinMinigameEvent event) {
        currentGame = event.getMinigame();
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
        if ((KeyBindHandler.toggleSprint.isActivated())) {
            if (event.getKey() == Minecraft.getMinecraft().gameSettings.keyBindForward.getKeyCode() || event.getKey() == KeyBindHandler.toggleSprint.getKey()) {
                ((MixinKeyBinding) Minecraft.getMinecraft().gameSettings.keyBindSprint).setPressed(true);
            }
        }
        if ((KeyBindHandler.debug.isPressed())) {
            Minecraft.getMinecraft().displayGuiScreen(new HypixelFriendsGui());
            KeyBindHandler.debug.onPress();
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
        JOptionPane.showMessageDialog(popup, "Hyperium Client", "Hyperium - About", JOptionPane.PLAIN_MESSAGE);
    }

    public boolean isAcceptedTos() {
        return acceptedTos;
    }

    public void acceptTos() {
        acceptedTos=true;
        try {
            new File(folder.getAbsolutePath() + "/accounts/" + Minecraft.getMinecraft().getSession().getPlayerID() + ".lck").createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
