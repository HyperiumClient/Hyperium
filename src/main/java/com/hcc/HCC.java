/*
 *     Hypixel Community Client, Client optimized for Hypixel Network
 *     Copyright (C) 2018  HCC Dev Team
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

package com.hcc;

import com.hcc.ac.AntiCheat;
import com.hcc.addons.HCCAddonBootstrap;
import com.hcc.addons.loader.DefaultAddonLoader;
import com.hcc.commands.defaults.CommandClearChat;
import com.hcc.commands.defaults.CommandPrivateMessage;
import com.hcc.config.DefaultConfig;
import com.hcc.event.*;
import com.hcc.event.minigames.Minigame;
import com.hcc.event.minigames.MinigameListener;
import com.hcc.gui.ModConfigGui;
import com.hcc.gui.NotificationCenter;
import com.hcc.gui.integrations.HypixelFriendsGui;
import com.hcc.handlers.HCCHandlers;
import com.hcc.commands.defaults.CommandConfigGui;
import com.hcc.handlers.handlers.keybinds.KeyBindHandler;
import com.hcc.mixins.MixinKeyBinding;
import com.hcc.mods.HCCModIntegration;
import com.hcc.mods.ToggleSprintContainer;
import com.hcc.mods.capturex.CaptureCore;
import com.hcc.mods.discord.RichPresenceManager;
import com.hcc.utils.ChatColor;
import com.hcc.tray.TrayManager;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;

import java.awt.*;
import java.io.File;
import java.util.regex.Pattern;

/**
 * Hypixel Community Client
 */
public class HCC {

    public static final HCC INSTANCE = new HCC();
    /**
     * Instance of the global mod LOGGER
     */
    public final static Logger LOGGER = LogManager.getLogger(Metadata.getModid());
    /**
     * Instance of default addons loader
     */
    private final DefaultAddonLoader addonLoader = new DefaultAddonLoader();
    private final NotificationCenter notification = new NotificationCenter();
    public static File folder = new File("hcc");
    /**
     * Instance of default CONFIG
     */
    public static final DefaultConfig CONFIG = new DefaultConfig(new File(folder, "CONFIG.json"));
    private HCCAddonBootstrap addonBootstrap;

    private RichPresenceManager richPresenceManager = new RichPresenceManager();

    private AntiCheat anticheat = new AntiCheat();

    private TrayManager trayManager;
    private HCCHandlers handlers;
    private HCCModIntegration modIntegration;
    private Minigame currentGame;
    private CaptureCore captureCore;

    private Pattern friendRequestPattern;
    private Pattern rankBracketPattern;
    private Pattern swKillMsg;
    private Pattern bwKillMsg;
    private Pattern bwFinalKillMsg;
    private Pattern duelKillMsg;

    /**
     * @param event initialize HCC
     */
    @InvokeEvent
    public void init(InitializationEvent event) {


        EventBus.INSTANCE.register(new MinigameListener());
        EventBus.INSTANCE.register(new ToggleSprintContainer());
        EventBus.INSTANCE.register(notification);
        EventBus.INSTANCE.register(anticheat);
        EventBus.INSTANCE.register(captureCore = new CaptureCore());

        friendRequestPattern = Pattern.compile("Friend request from .+?");
        rankBracketPattern = Pattern.compile("[\\^] ");
        swKillMsg = Pattern.compile(".+? was .+? by .+?\\.");
        bwKillMsg = Pattern.compile(".+? by .+?\\.");
        bwFinalKillMsg = Pattern.compile(".+? by .+?\\. FINAL KILL!");
        duelKillMsg = Pattern.compile(".+? was kill by .+?\\.");

        folder = new File(Minecraft.getMinecraft().mcDataDir, "hcc");
        LOGGER.info("HCC Started!");
        Display.setTitle("HCC " + Metadata.getVersion());

        handlers = new HCCHandlers();
        trayManager = new TrayManager();
        anticheat.init();
        try {
            trayManager.init();
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.warn("Failed to hookup TrayIcon");
        }

        //Register commands.
        registerCommands();


        modIntegration = new HCCModIntegration();
        richPresenceManager.init();
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
    }

    /**
     * load addons
     */
    public void registerAddons() {
        try {
            addonBootstrap = new HCCAddonBootstrap();
            addonBootstrap.loadInternalAddon();
            addonBootstrap.loadAddons(addonLoader);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("Failed to load addon(s) from addons folder");
        }
    }

    /**
     * register the commands
     */
    private void registerCommands() {
//       HCC.INSTANCE.getHandlers().getHCCCommandHandler().registerCommand(new TestCommand());

        HCC.INSTANCE.getHandlers().getHCCCommandHandler().registerCommand(new CommandConfigGui());
        HCC.INSTANCE.getHandlers().getHCCCommandHandler().registerCommand(new CommandPrivateMessage());
        HCC.INSTANCE.getHandlers().getHCCCommandHandler().registerCommand(new CommandClearChat());
    }

    /**
     * @param event called when someone sent a chat message
     */
    @InvokeEvent
    public void onChat(ChatEvent event) {
        if (event.getChat().getUnformattedText().contains("configgui")) {
            event.setCancelled(true);
            Minecraft.getMinecraft().displayGuiScreen(new ModConfigGui());
            notification.display("Settings", "opened settings gui", 2);
        }
        if (friendRequestPattern.matcher(ChatColor.stripColor(event.getChat().getUnformattedText())).matches()) {
            String withoutRank = ChatColor.stripColor(event.getChat().getUnformattedText());
            withoutRank = withoutRank.replaceAll("Friend request from ", "");
            withoutRank = withoutRank.replaceAll(rankBracketPattern.pattern(), "");
            EventBus.INSTANCE.post(new HypixelFriendRequestEvent(withoutRank));
        }
        String msg = ChatColor.stripColor(event.getChat().getUnformattedText());
        if(getHandlers().getHypixelDetector().isHypixel()){
            switch (currentGame){
                case SKYWARS:
                    if(swKillMsg.matcher(msg).matches())
                        if(msg.endsWith(Minecraft.getMinecraft().thePlayer.getName()+"."))
                            EventBus.INSTANCE.post(new HypixelKillEvent(Minigame.SKYWARS, msg.split(" ")[0]));
                    break;
                case BEDWARS:
                    if(bwKillMsg.matcher(msg).matches() || bwFinalKillMsg.matcher(msg).matches())
                        msg = msg.replace(" FINAL KILL!", "");
                        if(msg.endsWith(Minecraft.getMinecraft().thePlayer.getName()+"."))
                            EventBus.INSTANCE.post(new HypixelKillEvent(Minigame.BEDWARS, msg.split(" ")[0]));
                    break;
                case DUELS:
                    if(duelKillMsg.matcher(msg).matches())
                        if(msg.endsWith(Minecraft.getMinecraft().thePlayer.getName()+"."))
                            EventBus.INSTANCE.post(new HypixelKillEvent(Minigame.DUELS, msg.split(" ")[0]));
            }
        }


    }

    @InvokeEvent
    public void onMinigameJoin(JoinMinigameEvent event){
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
        // i got u - coal
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
     * called when HCC shutdown
     */
    private void shutdown() {
        CONFIG.save();
        richPresenceManager.shutdown();
        captureCore.shutdown();
        LOGGER.info("Shutting down HCC..");
    }


    public HCCHandlers getHandlers() {
        return handlers;
    }

    public HCCModIntegration getModIntegration() {
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
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(ChatColor.RED + "[HCC] " + ChatColor.WHITE + msg));
    }
    
    public void trayDisplayAboutInfo() {
        JOptionPane popup = new JOptionPane();
        JOptionPane.showMessageDialog(popup, "HypixelCommunityClient", "HCC - About", JOptionPane.PLAIN_MESSAGE);
    }
}
