package cc.hyperium.mixins.renderer;

import cc.hyperium.mods.nickhider.NickHider;
import net.minecraft.client.gui.FontRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(FontRenderer.class)
public abstract class MixinFontRenderer {

    @ModifyVariable(method = "renderString", at = @At(value = "HEAD"))
    private String mod(String in) {
        if (NickHider.instance == null) { // When mod hasn't initialized yet.
            return in;
        }
        return NickHider.instance.apply(in);
    }

    @ModifyVariable(method = "getStringWidth", at = @At(value = "HEAD"))
    private String modWidth(String in) {
        if (NickHider.instance == null) { // When mod hasn't initialized yet.
            return in;
        }
        return NickHider.instance.apply(in);
    }
}
