package cc.hyperium.mixins.entity;

import net.minecraft.client.particle.EntityFX;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityFX.class)
public interface IMixinEntityFx {
    @Accessor
    void setParticleGravity(float particleGravity);

    @Accessor
    void setParticleMaxAge(int particleMaxAge);
}
