package cc.hyperium.mods.autogg;

import cc.hyperium.Hyperium;
import cc.hyperium.event.EventBus;
import cc.hyperium.handlers.handlers.HypixelDetector;
import cc.hyperium.mods.AbstractMod;
import cc.hyperium.mods.autogg.commands.GGCommand;
import cc.hyperium.mods.autogg.config.AutoGGConfig;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.utils.ChatColor;
import org.apache.commons.io.IOUtils;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The AutoGG mod by 2pi
 *
 * @author 2Pi, Amplifiable, boomboompower
 */
public class AutoGG extends AbstractMod {

    // Static woo
    private static List<String> triggers;
    private final Metadata meta;
    private AutoGGConfig config;
    private boolean running;

    public AutoGG() {
        Metadata metadata = new Metadata(this, "AutoGG", "2.0", "2Pi");
        metadata.setDisplayName(ChatColor.GOLD + "AutoGG");

        this.meta = metadata;

        this.running = false;
    }

    @Override
    public AbstractMod init() {
        this.config = new AutoGGConfig();

        EventBus.INSTANCE.register(new AutoGGListener(this));
        Hyperium.INSTANCE.getHandlers().getHyperiumCommandHandler()
            .registerCommand(new GGCommand(this));

        // The GetTriggers class
        Multithreading.POOL.submit(() -> {
            try {
                final String rawTriggers = IOUtils.toString(
                    new URL("https://raw.githubusercontent.com/HyperiumClient/Hyperium-Repo/master/files/triggers.txt"),
                    Charset.forName("UTF-8")
                );

                triggers = new ArrayList<>(Arrays.asList(rawTriggers.split("\n")));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return this;
    }

    @Override
    public Metadata getModMetadata() {
        return meta;
    }

    public AutoGGConfig getConfig() {
        return this.config;
    }

    public boolean isRunning() {
        return this.running;
    }

    public void setRunning(final boolean running) {
        this.running = running;
    }

    public List<String> getTriggers() {
        return triggers;
    }

    public void setTriggers(final ArrayList<String> triggersIn) {
        triggers = triggersIn;
    }

    public boolean isHypixel() {
        return HypixelDetector.getInstance().isHypixel();
    }
}
