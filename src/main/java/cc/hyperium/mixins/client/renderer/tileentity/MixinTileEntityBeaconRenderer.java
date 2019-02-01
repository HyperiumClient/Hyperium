package cc.hyperium.mixins.client.renderer.tileentity;

import cc.hyperium.mixinsimp.client.renderer.tileentity.HyperiumTileEntityBeaconRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityBeaconRenderer;
import net.minecraft.tileentity.TileEntityBeacon;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TileEntityBeaconRenderer.class)
public class MixinTileEntityBeaconRenderer {

    private HyperiumTileEntityBeaconRenderer hyperiumTileEntityBeaconRenderer = new HyperiumTileEntityBeaconRenderer();

    @Inject(method = "renderTileEntityAt", at = @At("HEAD"), cancellable = true)
    public void renderTileEntityAt(TileEntityBeacon te, double x, double y, double z, float partialTicks, int destroyStage, CallbackInfo ci) {
        hyperiumTileEntityBeaconRenderer.renderTileEntityAt(te, x, y, z, partialTicks, destroyStage, ci);
    }
}
