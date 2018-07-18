package cc.hyperium.mods.glintcolorizer;

import cc.hyperium.Hyperium;
import cc.hyperium.config.ConfigOpt;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.TickEvent;
import cc.hyperium.mods.AbstractMod;

import java.awt.*;

public class GlintColorizer extends AbstractMod {

    @ConfigOpt
    public static boolean enabled = false;

    @Override
    public AbstractMod init() {
        Hyperium.CONFIG.register(new Colors());
        Hyperium.CONFIG.register(this);
        EventBus.INSTANCE.register(this);
        return this;
    }

    @InvokeEvent
    public void onTick(TickEvent e) {
        if (!enabled) {
            if (Colors.onepoint8glintcolorI != -8372020) {
                Colors.onepoint8glintcolorI = -8372020;
            }
            return;
        }
        if (Colors.chroma) {
            Colors.onepoint8glintcolorI = Color
                    .HSBtoRGB(System.currentTimeMillis() % (10000L / Colors.chromaSpeed) / (10000.0f / Colors.chromaSpeed), 0.8f, 0.8f);
        }
    }

    @Override
    public Metadata getModMetadata() {
        return new Metadata(this, "Glint Colorizer", "1.0", "powns");
    }

}
