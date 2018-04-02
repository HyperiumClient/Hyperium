package cc.hyperium.mods.autogg;

import cc.hyperium.Hyperium;
import cc.hyperium.event.EventBus;
import cc.hyperium.handlers.handlers.HypixelDetector;
import cc.hyperium.mods.AbstractMod;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AutoGG extends AbstractMod {
    public static final ExecutorService THREAD_POOL;
    private static AutoGG instance;
    public final Minecraft mc;
    private boolean toggle;
    private int delay;
    private List<String> triggers;
    private Boolean running;

    private final Metadata meta;

    public AutoGG() {
        Metadata metadata = new Metadata(this, "AutoGG", "2.0", "2Pi");
        metadata.setDisplayName("§6AutoGG");
        this.meta = metadata;
        this.mc = Minecraft.getMinecraft();
        this.toggle = true;
        this.delay = 1;
        this.running = false;
    }
    public static AutoGG getInstance() {
        return instance;
    }

    public boolean isHypixel() {
        return HypixelDetector.getInstance().isHypixel();
    }

    public List getTriggers() {
        return this.triggers;
    }

    public void setTriggers(final ArrayList<String> triggers) {
        this.triggers = triggers;
    }

    public boolean isToggled() {
        return this.toggle;
    }

    public void setToggled() {
        this.toggle = !this.toggle;
    }

    public int getDelay() {
        return this.delay;
    }

    public void setDelay(final int delay) {
        this.delay = delay;
    }

    public Minecraft getMinecraft() {
        return this.mc;
    }

    public boolean isRunning() {
        return this.running;
    }

    public void setRunning(final boolean running) {
        this.running = running;
    }

    static {
        THREAD_POOL = Executors.newCachedThreadPool(new AutoGGThreadFactory());
    }

    @Override
    public AbstractMod init() {
        AutoGG.instance = this;
        EventBus.INSTANCE.register(new GGListener());
        Hyperium.INSTANCE.getHandlers().getHyperiumCommandHandler().registerCommand(new GGCommand());
        AutoGG.THREAD_POOL.submit(new GetTriggers());
        this.delay = ConfigUtil.getConfigDelay();
        return this;
    }

    @Override
    public Metadata getModMetadata() {
        return meta;
    }
}
