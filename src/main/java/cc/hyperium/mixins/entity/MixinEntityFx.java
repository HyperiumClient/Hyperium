package cc.hyperium.mixins.entity;

import cc.hyperium.mixinsimp.renderer.IMixinEntityFx;
import net.minecraft.client.particle.EntityFX;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * Created by mitchellkatz on 6/25/18. Designed for production use on Sk1er.club
 */
@Mixin(EntityFX.class)
public class MixinEntityFx implements IMixinEntityFx {
    @Shadow
    protected int particleMaxAge;
    @Shadow
    protected float particleRed;
    @Shadow
    protected float particleGreen;
    @Shadow
    protected float particleBlue;

    @Override
    public void setMaxAge(int age) {
        this.particleMaxAge = age;
    }

    public void setParticleRed(float particleRed) {
        this.particleRed = particleRed;
    }

    public void setParticleGreen(float particleGreen) {
        this.particleGreen = particleGreen;
    }

    public void setParticleBlue(float particleBlue) {
        this.particleBlue = particleBlue;
    }
}
