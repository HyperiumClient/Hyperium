package cc.hyperium.mixins.renderer;

import cc.hyperium.addons.morefps.gui.Overlay;
import cc.hyperium.addons.morefps.utils.Utils;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.tileentity.TileEntityBannerRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.tileentity.TileEntityBanner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({Render.class})
public abstract class MixinRenderAddon<T extends Entity> {

    @Inject(method = "shouldRender", at = {@At("HEAD")}, cancellable = true)
    public void onShouldRender(T livingEntity, ICamera camera, double camX, double camY, double camZ, CallbackInfoReturnable<Boolean> cir) {
        if ((livingEntity instanceof EntityArmorStand && Overlay.hideArmorStands) ||
                (livingEntity instanceof EntityOtherPlayerMP && Overlay.hideNPCs && Utils.isNPC(livingEntity)) ||
                (livingEntity instanceof EntityItemFrame && Overlay.hideItemFrames) ||
                (livingEntity instanceof EntityLeashKnot && Overlay.hideBalloons && Utils.inSkywarsGame())) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }
}
