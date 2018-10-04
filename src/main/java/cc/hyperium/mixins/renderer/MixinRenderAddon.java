package cc.hyperium.mixins.renderer;

import cc.hyperium.addons.morefps.gui.MoreFPSOverlay;
import cc.hyperium.addons.morefps.utils.MoreFPSUtils;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityItemFrame;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({Render.class})
public abstract class MixinRenderAddon<T extends Entity> {

    @Inject(method = "shouldRender", at = {@At("HEAD")}, cancellable = true)
    public void onShouldRender(T livingEntity, ICamera camera, double camX, double camY, double camZ, CallbackInfoReturnable<Boolean> cir) {
        if ((livingEntity instanceof EntityArmorStand && MoreFPSOverlay.hideArmorStands) ||
                (livingEntity instanceof EntityOtherPlayerMP && MoreFPSOverlay.hideNPCs && MoreFPSUtils.isNPC(livingEntity)) ||
                (livingEntity instanceof EntityItemFrame && MoreFPSOverlay.hideItemFrames) ||
                (livingEntity instanceof EntityLeashKnot && MoreFPSOverlay.hideBalloons && MoreFPSUtils.inSkywarsGame())) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }
}
