package com.hcc.mixins;

import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SoundManager;
import org.lwjgl.opengl.Display;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SoundManager.class)
public class MixinSoundManager {

    /**
     * Sound will not play unless the window is active
     *
     * @param sound
     * @param ci
     */
    @Inject(method = "playSound", at = @At("HEAD"), cancellable = true)
    private void playSound(ISound sound, CallbackInfo ci) {
        if (!Display.isActive()) { // todo: settings to enable / disable
            ci.cancel();
        }
    }

}