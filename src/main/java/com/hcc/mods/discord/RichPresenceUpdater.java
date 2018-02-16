package com.hcc.mods.discord;

import com.hcc.HCC;
import com.hcc.event.*;
import com.jagrosh.discordipc.IPCClient;
import com.jagrosh.discordipc.entities.RichPresence;
import net.minecraft.client.Minecraft;

import java.time.OffsetDateTime;

public class RichPresenceUpdater {

    private IPCClient client;

    RichPresenceUpdater(IPCClient client) {
        this.client = client;
        RichPresence.Builder builder = new RichPresence.Builder();
        client.sendRichPresence(builder
                .setSmallImage("compass")
                .setLargeImage("hcc", "Hypixel Community Client")
                .setState("IGN: " + Minecraft.getMinecraft().getSession().getUsername())
                .setStartTimestamp(OffsetDateTime.now())
                .build());
    }

    @InvokeEvent(priority = Priority.LOW)
    public void onServerJoin(ServerJoinEvent event) {
        RichPresence.Builder builder = new RichPresence.Builder();
        if (HCC.INSTANCE.getHandlers().getHypixelDetector().isHypixel()) {
            client.sendRichPresence(builder
                    .setSmallImage("compass")
                    .setLargeImage("16", "Hypixel Network")
                    .setState("IGN: " + Minecraft.getMinecraft().getSession().getUsername())
                    .setStartTimestamp(OffsetDateTime.now())
                    .build());
        } else {
            client.sendRichPresence(builder
                    .setSmallImage("compass")
                    .setLargeImage("16", "Hypixel Network")
                    .setState("IGN: " + Minecraft.getMinecraft().getSession().getUsername())
                    .setStartTimestamp(OffsetDateTime.now())
                    .build());
        }
    }

    @InvokeEvent(priority = Priority.LOW)
    public void onMinigameJoin(JoinMinigameEvent event) {
        RichPresence.Builder builder = new RichPresence.Builder();
        client.sendRichPresence(builder
                .setSmallImage("compass")
                .setLargeImage(String.valueOf(event.getMinigame().getId()), event.getMinigame().getScoreName())
                .setState("IGN: " + Minecraft.getMinecraft().getSession().getUsername())
                .setStartTimestamp(OffsetDateTime.now())
                .build());

    }

    @InvokeEvent(priority = Priority.LOW)
    public void onSinglePlayer(SingleplayerJoinEvent event) {
        RichPresence.Builder builder = new RichPresence.Builder();
        client.sendRichPresence(builder
                .setSmallImage("compass")
                .setLargeImage("hcc", "Hypixel Community Client")
                .setState("IGN: " + Minecraft.getMinecraft().getSession().getUsername())
                .setStartTimestamp(OffsetDateTime.now())
                .build());
    }
}
