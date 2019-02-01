package cc.hyperium.mods.guiscale;

import cc.hyperium.Hyperium;
import cc.hyperium.config.Settings;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.WorldLoadEvent;
import cc.hyperium.event.WorldUnloadEvent;
import cc.hyperium.gui.hyperium.HyperiumMainGui;
import cc.hyperium.mods.AbstractMod;
import cc.hyperium.mods.guiscale.command.GuiscaleCommand;
import net.minecraft.client.Minecraft;

public class Guiscale extends AbstractMod {

    private static boolean worldLoaded;
    private static Minecraft mc = Minecraft.getMinecraft();

    @Override
    public AbstractMod init() {
        EventBus.INSTANCE.register(this);
        Hyperium.INSTANCE.getHandlers().getCommandHandler().registerCommand(new GuiscaleCommand());
        return this;
    }

    @Override
    public Metadata getModMetadata() {
        return new Metadata(this, "Guiscale", "1.0", "canalex");
    }

    @InvokeEvent
    public void onWorldLoad(WorldLoadEvent event) {
        worldLoaded = true;
    }

    @InvokeEvent
    public void onWorldLeave(WorldUnloadEvent event) {
        worldLoaded = false;
    }

    public static double getGuiScale() {
        if (mc.currentScreen instanceof HyperiumMainGui) {
            return 0.0;
        }

        if (mc.inGameHasFocus) {
            worldLoaded = true;
        }

        if (mc.theWorld != null && worldLoaded){
            return Settings.GUISCALEBONUS;
        }
        return 0.0;
    }
}
