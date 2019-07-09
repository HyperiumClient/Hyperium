package cc.hyperium.mixins.client.renderer.tileentity;

import cc.hyperium.mixinsimp.client.renderer.tileentity.HyperiumRenderItemFrame;
import net.minecraft.client.renderer.tileentity.RenderItemFrame;
import net.minecraft.entity.item.EntityItemFrame;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderItemFrame.class)
public class MixinRenderItemFrame {

    private HyperiumRenderItemFrame hyperiumRenderItemFrame = new HyperiumRenderItemFrame();

    @Inject(method = "doRender", at = @At("HEAD"), cancellable = true)
    public void doRender(EntityItemFrame entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci) {
        hyperiumRenderItemFrame.doRender(entity, x, y, z, entityYaw, partialTicks, ci);
    }
}
