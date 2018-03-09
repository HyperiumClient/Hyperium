package cc.hyperium.mixins.gui;

import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Gui.class)
public class MixinGui {
    @Shadow
    protected float zLevel;

    public float getzLevel() {
        return zLevel;
    }
}
