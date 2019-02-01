package cc.hyperium.mixins.client.renderer.tileentity;

import cc.hyperium.mixinsimp.client.renderer.tileentity.HyperiumTileEntityBannerRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityBannerRenderer;
import net.minecraft.tileentity.TileEntityBanner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TileEntityBannerRenderer.class)
public class MixinTileEntityBannerRenderer {

    private HyperiumTileEntityBannerRenderer hyperiumTileEntityBannerRenderer = new HyperiumTileEntityBannerRenderer();

    @Inject(method = "renderTileEntityAt", at = @At("HEAD"), cancellable = true)
    public void renderTileEntityAt(TileEntityBanner te, double x, double y, double z, float partialTicks, int destroyStage, CallbackInfo ci) {
        hyperiumTileEntityBannerRenderer.renderTileEntityAt(te, x, y, z, partialTicks, destroyStage, ci);
    }
}
