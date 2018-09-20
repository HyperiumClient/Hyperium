package com.chattriggers.ctjs.minecraft.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import paulscode.sound.CommandThread;
import paulscode.sound.Library;
import paulscode.sound.SoundSystem;

@Mixin(value = SoundSystem.class, remap = false)
public interface MixinSoundSystem {
    @Accessor(remap = false)
    Library getSoundLibrary();

    @Accessor(remap = false)
    CommandThread getCommandThread();
}
