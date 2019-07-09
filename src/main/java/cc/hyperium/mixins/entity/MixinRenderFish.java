package cc.hyperium.mixins.entity;

import cc.hyperium.mixinsimp.entity.HyperiumRenderFish;
import net.minecraft.client.renderer.entity.RenderFish;
import net.minecraft.entity.projectile.EntityFishHook;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderFish.class)
public class MixinRenderFish {

    private HyperiumRenderFish hyperiumRenderFish = new HyperiumRenderFish((RenderFish) (Object) this);

    @Inject(method = "doRender", at = @At("HEAD"))
    public void doRender(EntityFishHook entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci) {
        hyperiumRenderFish.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
}
