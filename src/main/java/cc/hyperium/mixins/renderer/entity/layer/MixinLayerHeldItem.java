package cc.hyperium.mixins.renderer.entity.layer;

import cc.hyperium.mods.oldanimations.OldBlocking;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LayerHeldItem.class)
public class MixinLayerHeldItem {

    private OldBlocking oldBlocking = new OldBlocking();

    @Shadow
    @Final
    private RendererLivingEntity<?> livingEntityRenderer;

    /**
     * @author SiroQ
     * @reason Adds compatibility for the 1.7 blocking animation
     */
    @Overwrite
    public void doRenderLayer(EntityLivingBase entity, float f, float f2, float f3, float partialTicks, float f4, float f5, float scale) {
        oldBlocking.doRenderLayer(entity, this.livingEntityRenderer);
    }
}
