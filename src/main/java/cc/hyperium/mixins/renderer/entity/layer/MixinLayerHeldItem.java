package cc.hyperium.mixins.renderer.entity.layer;

import cc.hyperium.animations.OldBlocking;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LayerHeldItem.class)
public class MixinLayerHeldItem {

    @Shadow
    @Final
    private RendererLivingEntity<?> livingEntityRenderer;

    @Overwrite
    public void doRenderLayer(EntityLivingBase entity, float f, float f2, float f3, float partialTicks, float f4, float f5, float scale) {
        OldBlocking.doRenderLayer(entity, f, f2, f3, partialTicks, f4, f5, scale, this.livingEntityRenderer);
    }
}
