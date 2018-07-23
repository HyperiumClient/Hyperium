package cc.hyperium.addons.customrp.utils;

import cc.hyperium.config.Settings;
import com.jagrosh.discordipc.IPCClient;
import com.jagrosh.discordipc.entities.RichPresence;
import cc.hyperium.addons.customrp.config.Config;
import net.minecraft.client.Minecraft;

import java.time.OffsetDateTime;

public class RichPresenceUpdater {
    public static IPCClient client;

    public static void callCustomRPUpdate() {
        if (!Settings.DISCORD_RP || !Config.ENABLED)
            return;

        if (client != null) {
            if (Config.CUSTOM_RP_MODE.equalsIgnoreCase("addon")) {
                RichPresence.Builder builder = new RichPresence.Builder();

                client.sendRichPresence(builder
                        .setSmallImage("compass")
                        .setLargeImage("hyperium", "Hyperium Client")
                        .setState("Playing a game")
                        .setDetails("CustomRP [Internal]")
                        .setStartTimestamp(OffsetDateTime.now())
                        .build());
            } else if (Config.CUSTOM_RP_MODE.equalsIgnoreCase("E-Vowels")) {
                RichPresence.Builder builder = new RichPresence.Builder();

                client.sendRichPresence(builder
                        .setSmallImage("compass")
                        .setLargeImage("hyperium", "Hyperium Client")
                        .setState("EGN: " + EUtils.geteIfiedUsername())
                        .setDetails("CestemRP [Enternel]")
                        .setStartTimestamp(OffsetDateTime.now())
                        .build());
            } else if (Config.CUSTOM_RP_MODE.equalsIgnoreCase("E-All")) {
                RichPresence.Builder builder = new RichPresence.Builder();

                client.sendRichPresence(builder
                        .setSmallImage("compass")
                        .setLargeImage("hyperium", "Hyperium Client")
                        .setState("EEE: " + EUtils.getAllEUsername())
                        .setDetails("EeeeeeEE [Eeeeeeee]")
                        .setStartTimestamp(OffsetDateTime.now())
                        .build());
            } else if (Config.CUSTOM_RP_MODE.equalsIgnoreCase("sellout")) {
                RichPresence.Builder builder = new RichPresence.Builder();

                client.sendRichPresence(builder
                        .setSmallImage("compass")
                        .setLargeImage("hyperium", "Hyperium Client")
                        .setState("Sub to " + Minecraft.getMinecraft().getSession().getUsername())
                        .setDetails("CustomRP [Internal]")
                        .setStartTimestamp(OffsetDateTime.now())
                        .build());
            } else if (Config.CUSTOM_RP_MODE.equalsIgnoreCase("bestCoder")) {
                RichPresence.Builder builder = new RichPresence.Builder();

                client.sendRichPresence(builder
                        .setSmallImage("compass")
                        .setLargeImage("hyperium", "Hyperium Client")
                        .setState(Minecraft.getMinecraft().getSession().getUsername() + " best coder")
                        .setDetails("CustomRP [Internal]")
                        .setStartTimestamp(OffsetDateTime.now())
                        .build());
            } else if (Config.CUSTOM_RP_MODE.equalsIgnoreCase("merch")) {
                if (Minecraft.getMinecraft().getSession().getUsername().endsWith("s")) {
                    RichPresence.Builder builder = new RichPresence.Builder();

                    client.sendRichPresence(builder
                            .setSmallImage("compass")
                            .setLargeImage("hyperium", "Hyperium Client")
                            .setState("Buy " + Minecraft.getMinecraft().getSession().getUsername() + "' merch")
                            .setDetails("CustomRP [Internal]")
                            .setStartTimestamp(OffsetDateTime.now())
                            .build());
                } else {
                    RichPresence.Builder builder = new RichPresence.Builder();

                    client.sendRichPresence(builder
                            .setSmallImage("compass")
                            .setLargeImage("hyperium", "Hyperium Client")
                            .setState("Buy " + Minecraft.getMinecraft().getSession().getUsername() + "'s merch")
                            .setDetails("CustomRP [Internal]")
                            .setStartTimestamp(OffsetDateTime.now())
                            .build());
                }
            } else if (Config.CUSTOM_RP_MODE.equalsIgnoreCase("respects")) {
                RichPresence.Builder builder = new RichPresence.Builder();

                client.sendRichPresence(builder
                        .setSmallImage("compass")
                        .setLargeImage("hyperium", "Hyperium Client")
                        .setState("Press [F] to pay respects")
                        .setDetails("CustomRP [Internal]")
                        .setStartTimestamp(OffsetDateTime.now())
                        .build());
            } else if (Config.CUSTOM_RP_MODE.equalsIgnoreCase("sleepy")) {
                RichPresence.Builder builder = new RichPresence.Builder();

                client.sendRichPresence(builder
                        .setSmallImage("compass")
                        .setLargeImage("hyperium", "Hyperium Client")
                        .setState(Minecraft.getMinecraft().getSession().getUsername() + " sleepy")
                        .setDetails("CustomRP [Internal]")
                        .setStartTimestamp(OffsetDateTime.now())
                        .build());
            } else {
                Config.CUSTOM_RP_MODE = "addon";
                callCustomRPUpdate();
            }
        }
    }

    public RichPresenceUpdater(IPCClient client) {
        RichPresenceUpdater.client = client;
        if (Settings.DISCORD_RP) {
            callCustomRPUpdate();
        }
    }
}
