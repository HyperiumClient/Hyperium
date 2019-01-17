package cc.hyperium.mixins.client.renderer.entity;

import cc.hyperium.mixinsimp.client.renderer.entity.HyperiumRenderLightningBolt;
import net.minecraft.client.renderer.entity.RenderLightningBolt;
import net.minecraft.entity.effect.EntityLightningBolt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderLightningBolt.class)
public class MixinRenderLightningBolt {

    private HyperiumRenderLightningBolt hyperiumRenderLightningBolt = new HyperiumRenderLightningBolt();

    @Inject(method = "doRender", at = @At("HEAD"), cancellable = true)
    public void doRender(EntityLightningBolt entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci) {
        hyperiumRenderLightningBolt.doRender(entity, x, y, z, entityYaw, partialTicks, ci);
    }
}
