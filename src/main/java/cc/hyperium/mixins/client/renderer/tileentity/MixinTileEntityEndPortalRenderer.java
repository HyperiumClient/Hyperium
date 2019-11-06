package cc.hyperium.mixins.client.renderer.tileentity;

import cc.hyperium.config.Settings;
import net.minecraft.client.renderer.tileentity.TileEntityEndPortalRenderer;
import net.minecraft.tileentity.TileEntityEndPortal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TileEntityEndPortalRenderer.class)
public class MixinTileEntityEndPortalRenderer {

    @Inject(method = "renderTileEntityAt", at = @At("HEAD"), cancellable = true)
    private void preRender(TileEntityEndPortal te, double x, double y, double z, float partialTicks, int destroyStage, CallbackInfo ci) {
        if (Settings.DISABLE_END_PORTALS) {
            ci.cancel();
        }
    }
}
