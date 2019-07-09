package cc.hyperium.mods.discord;

import cc.hyperium.Hyperium;
import cc.hyperium.config.Settings;
import cc.hyperium.event.*;
import com.jagrosh.discordipc.IPCClient;
import com.jagrosh.discordipc.entities.RichPresence;
import net.minecraft.client.Minecraft;

import java.time.OffsetDateTime;

public class RPCUpdater {

    private final IPCClient client;

    RPCUpdater(IPCClient client) {
        this.client = client;

        if (!Settings.DISCORD_RP) {
            return;
        }

        RichPresence.Builder builder = new RichPresence.Builder();

        client.sendRichPresence(builder
            .setSmallImage("compass")
            .setLargeImage("hyperium", "Hyperium Client")
            .setState("IGN: " + Minecraft.getMinecraft().getSession().getUsername())
            .setDetails("On the main menu")
            .setStartTimestamp(OffsetDateTime.now())
            .build());
    }

    @InvokeEvent
    public void joinServer(ServerJoinEvent event) {
        if (Settings.DISCORD_RP_SERVER) {
            RichPresence.Builder builder = new RichPresence.Builder();

            if (Hyperium.INSTANCE.getHandlers().getHypixelDetector().isHypixel()) {
                client.sendRichPresence(builder
                    .setSmallImage("compass")
                    .setLargeImage("16", "Hypixel Network")
                    .setState("IGN: " + Minecraft.getMinecraft().getSession().getUsername())
                    .setDetails("In the lobby on hypixel.net")
                    .setStartTimestamp(OffsetDateTime.now())
                    .build());
            } else {
                client.sendRichPresence(builder
                    .setSmallImage("compass")
                    .setLargeImage("16", "On a server")
                    .setState("IGN: " + Minecraft.getMinecraft().getSession().getUsername())
                    .setDetails("On a Minecraft server")
                    .setStartTimestamp(OffsetDateTime.now())
                    .build());
            }
        }
    }

    @InvokeEvent
    public void joinHypixelMinigame(JoinMinigameEvent event) {
        if (Settings.DISCORD_RP_SERVER) {
            RichPresence.Builder builder = new RichPresence.Builder();

            client.sendRichPresence(builder
                .setSmallImage("compass")
                .setLargeImage(String.valueOf(event.getMinigame().getId()), event.getMinigame().getScoreName())
                .setState("IGN: " + Minecraft.getMinecraft().getSession().getUsername())
                .setDetails("Playing " + event.getMinigame().getScoreName() + " on hypixel.net")
                .setStartTimestamp(OffsetDateTime.now())
                .build());
        }
    }

    @InvokeEvent
    public void joinSingleplayer(SingleplayerJoinEvent event) {
        RichPresence.Builder builder = new RichPresence.Builder();

        client.sendRichPresence(builder
            .setSmallImage("compass")
            .setLargeImage("hyperium", "Hyperium Client")
            .setState("IGN: " + Minecraft.getMinecraft().getSession().getUsername())
            .setDetails("Playing Singleplayer")
            .setStartTimestamp(OffsetDateTime.now())
            .build());
    }

    @InvokeEvent
    public void leaveServer(ServerLeaveEvent event) {
        RichPresence.Builder builder = new RichPresence.Builder();

        client.sendRichPresence(builder
            .setSmallImage("compass")
            .setLargeImage("hyperium", "Hyperium Client")
            .setState("IGN: " + Minecraft.getMinecraft().getSession().getUsername())
            .setDetails("On the main menu")
            .setStartTimestamp(OffsetDateTime.now())
            .build());
    }
}
