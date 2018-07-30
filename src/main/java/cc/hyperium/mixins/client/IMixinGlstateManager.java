package cc.hyperium.mixins.client;

import net.minecraft.client.renderer.GlStateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GlStateManager.class)
public interface IMixinGlstateManager {

    @Accessor
    int getActiveTextureUnit();
}
