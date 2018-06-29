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
    protected float particleGravity;
    @Shadow
    protected int particleMaxAge;

    public void setParticleGravity(float particleGravity) {
        this.particleGravity = particleGravity;
    }

    @Override
    public void setMaxAge(int age) {
        this.particleMaxAge = age;
    }


}
