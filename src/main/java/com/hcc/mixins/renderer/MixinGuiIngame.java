package com.hcc.mixins.renderer;

import com.hcc.event.EventBus;
import com.hcc.event.RenderHUDEvent;
import net.minecraft.client.gui.GuiIngame;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiIngame.class)
public class MixinGuiIngame {

    @Inject(method = "renderGameOverlay", at = @At("RETURN"))
    private void renderGameOverlay(float partialTicks, CallbackInfo ci) {
        EventBus.INSTANCE.post(new RenderHUDEvent(partialTicks));
    }
}
