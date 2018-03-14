package cc.hyperium.mixins.renderer;

import cc.hyperium.Hyperium;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Created by mitchellkatz on 3/14/18. Designed for production use on Sk1er.club
 */
@Mixin(Render.class)
public abstract class MixinRender<T extends Entity> {

    @Inject(method = "renderLivingLabel", at = @At("HEAD"), cancellable = true)
    public void renderLivingLabel(T entityIn, String str, double x, double y, double z, int maxDistance, CallbackInfo ci) {
        if(Hyperium.INSTANCE.getHandlers().getConfigOptions().hideNameTags)
            ci.cancel();
    }
}
