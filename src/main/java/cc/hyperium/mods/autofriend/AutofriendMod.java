package cc.hyperium.mods.autofriend;

import cc.hyperium.Hyperium;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.HypixelFriendRequestEvent;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.mods.AbstractMod;
import cc.hyperium.mods.autofriend.confog.AutofriendConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandHandler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class AutofriendMod extends AbstractMod {

    private Minecraft mc = Minecraft.getMinecraft();
    private boolean hypixel = false;
    public static List<String> blacklist;
    public static List<String> recent;
    private Pattern friend;

    public AutofriendMod() {
        blacklist = new ArrayList<String>();
        recent = new ArrayList<String>();
        friend = Pattern.compile("§m----------------------------------------------------Friend request from (?<name>.+)\\[ACCEPT\\] - \\[DENY\\] - \\[IGNORE\\].*");
    }

    @Override
    public AbstractMod init() {
        Hyperium.CONFIG.register(new AutofriendConfig());
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
        if (blacklist.stream().noneMatch((Predicate<? super String>) name::equalsIgnoreCase) && AutofriendConfig.toggle) {
            if (!name.equalsIgnoreCase(blacklist.toString())) {
                System.out.println("Friending " + name);
            }
            this.mc.thePlayer.sendChatMessage("/friend accept " + name);
            recent.add(name);
        }
    }

    private static void getBlacklist() throws IOException {
        final File blacklistFile = new File("config/autofriend.cfg");
        if (blacklistFile.exists()) {
            blacklist = Files.readAllLines(Paths.get(blacklistFile.toURI()));
            if (blacklist.get(0).equals("true") || blacklist.get(0).equals("false")) {
                AutofriendConfig.messages = Boolean.parseBoolean(blacklist.get(0));
                blacklist.remove(0);
            }
            else {
                writeBlacklist();
            }
        }
        else {
            writeBlacklist();
        }
    }

    public static void writeBlacklist() {
        try {
            final File blacklistFile = new File("config/autofriend.cfg");
            final FileWriter writer = new FileWriter(blacklistFile);
            writer.write(Boolean.toString(AutofriendConfig.messages));
            for (final String str : blacklist) {
                writer.write(System.lineSeparator() + str);
            }
            writer.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
