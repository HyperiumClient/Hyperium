package cc.hyperium.mixinsimp.client.renderer.tileentity;

import cc.hyperium.config.Settings;
import net.minecraft.tileentity.TileEntitySign;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class HyperiumTileEntitySignRenderer {

    public void renderTileEntityAt(TileEntitySign te, double x, double y, double z, float partialTicks, int destroyStage, CallbackInfo ci) {
        if (Settings.DISABLE_SIGNS) {
            ci.cancel();
        }
    }
}
