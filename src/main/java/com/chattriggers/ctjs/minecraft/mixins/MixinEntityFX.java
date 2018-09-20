package com.chattriggers.ctjs.minecraft.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

//#if MC<=10809
import net.minecraft.client.particle.EntityFX;
//#else
//$$ import net.minecraft.client.particle.Particle;
//#endif

//#if MC<=10809
@Mixin(EntityFX.class)
//#else
//$$ @Mixin(Particle.class)
//#endif
public interface MixinEntityFX {
    @Accessor
    int getParticleMaxAge();

    @Accessor
    void setParticleMaxAge(int particleMaxAge);
}
