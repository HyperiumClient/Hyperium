package cc.hyperium.mixins.client.renderer.tileentity;

import cc.hyperium.mixinsimp.client.renderer.tileentity.HyperiumTileEntitySignRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySignRenderer;
import net.minecraft.tileentity.TileEntitySign;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TileEntitySignRenderer.class)
public class MixinTileEntitySignRenderer {

    private HyperiumTileEntitySignRenderer hyperiumTileEntitySignRenderer = new HyperiumTileEntitySignRenderer();

    @Inject(method = "renderTileEntityAt", at = @At("HEAD"), cancellable = true)
    public void renderTileEntityAt(TileEntitySign te, double x, double y, double z, float partialTicks, int destroyStage, CallbackInfo ci) {
        hyperiumTileEntitySignRenderer.renderTileEntityAt(te, x, y, z, partialTicks, destroyStage, ci);
    }
}
