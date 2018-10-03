package cc.hyperium.mixins.renderer;

import cc.hyperium.addons.morefps.gui.Overlay;
import net.minecraft.client.renderer.tileentity.TileEntityBannerRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntityBanner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ TileEntityBannerRenderer.class })
public abstract class MixinTileEntityBannerRendererAddon extends TileEntitySpecialRenderer<TileEntityBanner> {

    @Inject(method = "renderTileEntityAt", at = {@At("HEAD")}, cancellable = true)
    public void renderTileEntityAt(TileEntityBanner te, double x, double y, double z, float partialTicks, int destroyStage, CallbackInfo ci) {
        if (Overlay.hideBanners) {
            ci.cancel();
        }
    }
}
