package com.hcc.mixins.entity;

import com.hcc.event.EventBus;
import com.hcc.event.RenderEvent;
import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static org.spongepowered.asm.mixin.injection.At.Shift.AFTER;
@Mixin(EntityRenderer.class)
public class MixinEntityRenderer {

    @Inject(method = "updateCameraAndRender", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiIngame;renderGameOverlay(F)V", shift = AFTER))
    private void updateCameraAndRender(float partialTicks, long nano, CallbackInfo ci) {
        EventBus.INSTANCE.post(new RenderEvent());
    }

}