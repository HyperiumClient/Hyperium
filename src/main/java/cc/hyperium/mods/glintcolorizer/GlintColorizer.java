package cc.hyperium.mods.glintcolorizer;

import cc.hyperium.Hyperium;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.TickEvent;
import cc.hyperium.mods.AbstractMod;
import net.minecraft.client.Minecraft;

import java.awt.Color;

public class GlintColorizer extends AbstractMod {

    private static Minecraft mc = Minecraft.getMinecraft();

    @Override
    public AbstractMod init() {
        Hyperium.CONFIG.register(new Colors());
        EventBus.INSTANCE.register(this);
        return this;
    }

    @InvokeEvent
    public void onTick(TickEvent e) {
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
