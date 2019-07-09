package cc.hyperium.mixins.renderer.model;

import cc.hyperium.mixinsimp.renderer.model.IMixinModelRenderer;
import net.minecraft.client.model.ModelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ModelRenderer.class)
public class MixinModelRenderer implements IMixinModelRenderer {
    @Shadow
    private boolean compiled;

    public void reset() {
        compiled = false;
    }

}
