package cc.hyperium.mixins.client;

import net.minecraft.client.LoadingScreenRenderer;
import net.minecraft.client.renderer.GlStateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LoadingScreenRenderer.class)
public class MixinLoadingScreen {

    @Inject(method = "setLoadingProgress", at = @At("HEAD"))
    public void setLoadingProgress(int progress, CallbackInfo ci) {
        GlStateManager.disableTexture2D();
        GlStateManager.enableTexture2D();
    }

}
