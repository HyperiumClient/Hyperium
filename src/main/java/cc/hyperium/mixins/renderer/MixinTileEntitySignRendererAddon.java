package cc.hyperium.mixins.renderer;

import cc.hyperium.addons.morefps.gui.Overlay;
import net.minecraft.client.renderer.tileentity.TileEntitySignRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntitySign;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({TileEntitySignRenderer.class})
public abstract class MixinTileEntitySignRendererAddon extends TileEntitySpecialRenderer<TileEntitySign> {

    @Inject(method = "renderTileEntityAt", at = {@At("HEAD")}, cancellable = true)
    public void renderTileEntityAt(TileEntitySign te, double x, double y, double z, float partialTicks, int destroyStage, CallbackInfo ci) {
        if (Overlay.hideSigns) {
            ci.cancel();
        }
    }
}
