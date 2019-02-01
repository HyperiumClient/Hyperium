package cc.hyperium.mixinsimp.client.renderer.tileentity;

import cc.hyperium.config.Settings;
import net.minecraft.tileentity.TileEntityBanner;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class HyperiumTileEntityBannerRenderer {

    public void renderTileEntityAt(TileEntityBanner te, double x, double y, double z, float partialTicks, int destroyStage, CallbackInfo ci) {
        if (Settings.DISABLE_BANNERS) {
            ci.cancel();
        }
    }
}
