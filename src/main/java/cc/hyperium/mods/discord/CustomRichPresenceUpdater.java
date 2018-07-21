package cc.hyperium.mods.discord;

import cc.hyperium.config.Settings;
import com.jagrosh.discordipc.IPCClient;
import com.jagrosh.discordipc.entities.RichPresence;
import net.minecraft.client.Minecraft;

import java.time.OffsetDateTime;

public class CustomRichPresenceUpdater {
    static IPCClient client;

    public static void discordRPupdate() {
        String eIfiedUsername = Minecraft.getMinecraft().getSession().getUsername();
        eIfiedUsername = eIfiedUsername.replace('a', 'e');
        eIfiedUsername = eIfiedUsername.replace('i', 'e');
        eIfiedUsername = eIfiedUsername.replace('o', 'e');
        eIfiedUsername = eIfiedUsername.replace('u', 'e');
        eIfiedUsername = eIfiedUsername.replace('A', 'e');
        eIfiedUsername = eIfiedUsername.replace('I', 'e');
        eIfiedUsername = eIfiedUsername.replace('O', 'e');
        eIfiedUsername = eIfiedUsername.replace('U', 'e');

        String allEUsername = "";
        for (int x = 0; x < Minecraft.getMinecraft().getSession().getUsername().length(); ) {
            allEUsername = allEUsername.concat("e");
            x++;
        }

        if (Settings.CUSTOM_RP_MODE.equalsIgnoreCase("reset")) {
            RichPresence.Builder builder = new RichPresence.Builder();

            client.sendRichPresence(builder
                    .setSmallImage("compass")
                    .setLargeImage("hyperium", "Hyperium Client")
                    .setState("Playing a game")
                    .setDetails("CustomRP [Internal]")
                    .setStartTimestamp(OffsetDateTime.now())
                    .build());
        } else if (Settings.CUSTOM_RP_MODE.equalsIgnoreCase("eVowels")) {
            RichPresence.Builder builder = new RichPresence.Builder();

            client.sendRichPresence(builder
                    .setSmallImage("compass")
                    .setLargeImage("hyperium", "Hyperium Client")
                    .setState("eGN: " + eIfiedUsername)
                    .setDetails("CestemRP [enternel]")
                    .setStartTimestamp(OffsetDateTime.now())
                    .build());
        } else if (Settings.CUSTOM_RP_MODE.equalsIgnoreCase("eAll")) {
            RichPresence.Builder builder = new RichPresence.Builder();

            client.sendRichPresence(builder
                    .setSmallImage("compass")
                    .setLargeImage("hyperium", "Hyperium Client")
                    .setState("eee: " + allEUsername)
                    .setDetails("eeeeeeee [eeeeeeee]")
                    .setStartTimestamp(OffsetDateTime.now())
                    .build());
        } else if (Settings.CUSTOM_RP_MODE.equalsIgnoreCase("sellout")) {
            RichPresence.Builder builder = new RichPresence.Builder();

            client.sendRichPresence(builder
                    .setSmallImage("compass")
                    .setLargeImage("hyperium", "Hyperium Client")
                    .setState("Sub to " + Minecraft.getMinecraft().getSession().getUsername())
                    .setDetails("CustomRP [Internal]")
                    .setStartTimestamp(OffsetDateTime.now())
                    .build());
        } else if (Settings.CUSTOM_RP_MODE.equalsIgnoreCase("bestCoder")) {
            RichPresence.Builder builder = new RichPresence.Builder();

            client.sendRichPresence(builder
                    .setSmallImage("compass")
                    .setLargeImage("hyperium", "Hyperium Client")
                    .setState(Minecraft.getMinecraft().getSession().getUsername() + " best coder")
                    .setDetails("CustomRP [Internal]")
                    .setStartTimestamp(OffsetDateTime.now())
                    .build());
        } else if (Settings.CUSTOM_RP_MODE.equalsIgnoreCase("merch")) {
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
        } else if (Settings.CUSTOM_RP_MODE.equalsIgnoreCase("respects")) {
            RichPresence.Builder builder = new RichPresence.Builder();

            client.sendRichPresence(builder
                    .setSmallImage("compass")
                    .setLargeImage("hyperium", "Hyperium Client")
                    .setState("Press [F] to pay respects")
                    .setDetails("CustomRP [Internal]")
                    .setStartTimestamp(OffsetDateTime.now())
                    .build());
        } else if (Settings.CUSTOM_RP_MODE.equalsIgnoreCase("sleepy")) {
            RichPresence.Builder builder = new RichPresence.Builder();

            client.sendRichPresence(builder
                    .setSmallImage("compass")
                    .setLargeImage("hyperium", "Hyperium Client")
                    .setState(Minecraft.getMinecraft().getSession().getUsername() + " sleepy")
                    .setDetails("CustomRP [Internal]")
                    .setStartTimestamp(OffsetDateTime.now())
                    .build());
        } else {
            Settings.CUSTOM_RP_MODE = "reset";
            discordRPupdate();
        }
    }
}
