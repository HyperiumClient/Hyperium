package cc.hyperium.handlers.handlers.hypixel;

import cc.hyperium.Hyperium;
import cc.hyperium.config.Settings;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.network.chat.ChatEvent;
import cc.hyperium.event.network.server.hypixel.PlayerJoinHypixelEvent;
import cc.hyperium.event.network.server.hypixel.PlayerLeaveHypixelEvent;
import cc.hyperium.handlers.handlers.data.HypixelAPI;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import net.minecraft.client.Minecraft;
import org.lwjgl.Sys;

import java.awt.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HypixelConnectionMessage {

    @InvokeEvent
    public void playerJoin(PlayerJoinHypixelEvent event) {
        if (event.getUsername() != null && Settings.CUSTOM_JOIN_LEAVE_MESSAGES
                && Hyperium.INSTANCE.getHandlers().getHypixelDetector().isHypixel()) {
            Multithreading.runAsync(() -> {
                try {
                    boolean isInGuild = false;
                    String myGuild = HypixelAPI.INSTANCE.getGuildFromPlayer(Minecraft.getMinecraft().thePlayer.getName()).get().getName();
                    String theirGuild = HypixelAPI.INSTANCE.getGuildFromPlayer(event.getUsername()).get().getName();
                    System.out.println(myGuild + " " + theirGuild+" "+ myGuild.equalsIgnoreCase(theirGuild));
                    isInGuild = myGuild.equals(theirGuild);
                    Color colour = isInGuild ? new Color(34, 189, 23) : new Color(255, 255, 85);
                    System.out.println(colour.getRGB());
                    Hyperium.INSTANCE.getNotification().display(event.getUsername(),
                            "has joined the server.\nClick to say hello to them.",
                            5,
                            null,
                            () -> Minecraft.getMinecraft().thePlayer.sendChatMessage("/msg " + event.getUsername() + " Hey!"),
                            colour
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

        }
    }

    @InvokeEvent
    public void playerLeave(PlayerLeaveHypixelEvent event) {
        if (event.getUsername() != null && Settings.CUSTOM_JOIN_LEAVE_MESSAGES
                && Hyperium.INSTANCE.getHandlers().getHypixelDetector().isHypixel()) {
            Multithreading.runAsync(() -> {
                try {
                    boolean isInGuild = false;
                    String myGuild = HypixelAPI.INSTANCE.getGuildFromPlayer(Minecraft.getMinecraft().thePlayer.getName()).get().getName();
                    String theirGuild = HypixelAPI.INSTANCE.getGuildFromPlayer(event.getUsername()).get().getName();
                    System.out.println(myGuild + " " + theirGuild+" "+ myGuild.equalsIgnoreCase(theirGuild));
                    isInGuild = myGuild.equals(theirGuild);
                    Color colour = isInGuild ? new Color(34, 189, 23) : new Color(255, 255, 85);
                    System.out.println(colour.getRGB());
                    Hyperium.INSTANCE.getNotification().display(event.getUsername(),
                            "has left the server.",
                            5,
                            null,
                            null,
                            colour
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

        }
    }

    @InvokeEvent
    public void chat(ChatEvent event) {
        if (Settings.CUSTOM_JOIN_LEAVE_MESSAGES && Hyperium.INSTANCE.getHandlers().getHypixelDetector().isHypixel()) {

            Pattern hypixelLeaveMessage = Pattern.compile("^(?<player>\\S{1,16}) left\\.$");
            Matcher leaveMessageMatcher = hypixelLeaveMessage.matcher(event.getChat().getUnformattedText());

            if (leaveMessageMatcher.find()) {
                event.setCancelled(true);
            }

            Pattern hypixelJoinMessage = Pattern.compile("^(?<player>\\S{1,16}) joined\\.$");
            Matcher joinMessageMatcher = hypixelJoinMessage.matcher(event.getChat().getUnformattedText());

            if (joinMessageMatcher.find()) {
                event.setCancelled(true);
            }
        }
    }
}
