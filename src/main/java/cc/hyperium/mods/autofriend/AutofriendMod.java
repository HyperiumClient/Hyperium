package cc.hyperium.mods.autofriend;

import cc.hyperium.config.Settings;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.HypixelFriendRequestEvent;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.mods.AbstractMod;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class AutofriendMod extends AbstractMod {

    private Minecraft mc = Minecraft.getMinecraft();
    public static List<String> blacklist = new ArrayList<>();
    public static final List<String> recent = new ArrayList<>();

    @Override
    public AbstractMod init() {
        EventBus.INSTANCE.register(this);
        try {
            getBlacklist();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public Metadata getModMetadata() {
        return new Metadata(this, "Autofriend Mod", "1.0", "ConorTheDev & 2PI");
    }

    @InvokeEvent
    public void friendRequestEvent(final HypixelFriendRequestEvent event) {
        String name = event.getFrom();
        if (Settings.AUTOFRIEND_TOGGLE && blacklist.stream().noneMatch(name::equalsIgnoreCase)) {
            this.mc.thePlayer.sendChatMessage("/friend accept " + name);
            recent.add(name);
        }
    }

    private static void getBlacklist() throws IOException {
        final File blacklistFile = new File("config/autofriend.cfg");
        if (blacklistFile.exists()) {
            blacklist = Files.readAllLines(Paths.get(blacklistFile.toURI()));
            if (blacklist.get(0).equals("true") || blacklist.get(0).equals("false")) {
                Settings.AUTOFRIEND_MESSAGES = Boolean.parseBoolean(blacklist.get(0));
                blacklist.remove(0);
            } else {
                writeBlacklist();
            }
        } else {
            writeBlacklist();
        }
    }

    public static void writeBlacklist() {
        try {
            final File blacklistFile = new File("config/autofriend.cfg");

            if (!blacklistFile.getParentFile().exists()) {
                if (!blacklistFile.getParentFile().mkdirs()) {
                    return;
                }
            }

            if (!blacklistFile.exists()) {
                if (!blacklistFile.createNewFile()) {
                    return;
                }
            }

            final FileWriter writer = new FileWriter(blacklistFile);
            writer.write(Boolean.toString(Settings.AUTOFRIEND_MESSAGES));
            for (final String str : blacklist) {
                writer.write(System.lineSeparator() + str);
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
