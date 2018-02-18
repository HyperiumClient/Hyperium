package com.hcc.mixins.entity;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderPlayer.class)
public abstract class MixinRenderPlayer {

    @Shadow
    public abstract ModelPlayer getMainModel();

    /**
     * Fixes bug MC-1349
     *
     * @param clientPlayer
     * @param ci
     */
    @Inject(method = "renderRightArm", at = @At(value = "FIELD", ordinal = 3))
    private void onUpdateTimer(AbstractClientPlayer clientPlayer, CallbackInfo ci) {
        ModelPlayer modelplayer = this.getMainModel();
        modelplayer.isRiding = modelplayer.isSneak = false;
    }
}
