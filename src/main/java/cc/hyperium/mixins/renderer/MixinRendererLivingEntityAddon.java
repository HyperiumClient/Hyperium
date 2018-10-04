package cc.hyperium.mixins.renderer;

import cc.hyperium.addons.morefps.gui.MoreFPSOverlay;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({RendererLivingEntity.class})
public abstract class MixinRendererLivingEntityAddon<T extends EntityLivingBase> extends Render<T> {

    protected MixinRendererLivingEntityAddon(RenderManager renderManager) {
        super(renderManager);
    }

    @Inject(method = "canRenderName", at = {@At("HEAD")}, cancellable = true)
    protected void onCanRenderName(T entity, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof EntityPlayer && MoreFPSOverlay.hidePlayerNames) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }
}
