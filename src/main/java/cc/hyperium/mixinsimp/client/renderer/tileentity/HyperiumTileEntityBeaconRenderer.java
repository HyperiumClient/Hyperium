package cc.hyperium.mixinsimp.client.renderer.tileentity;

import cc.hyperium.config.Settings;
import net.minecraft.tileentity.TileEntityBeacon;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class HyperiumTileEntityBeaconRenderer {

    public void renderTileEntityAt(TileEntityBeacon te, double x, double y, double z, float partialTicks, int destroyStage, CallbackInfo ci) {
        if (Settings.DISABLE_BEACON_BEAM) {
            ci.cancel();
        }
    }
}
