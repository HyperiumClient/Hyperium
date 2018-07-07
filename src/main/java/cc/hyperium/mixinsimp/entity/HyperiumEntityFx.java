package cc.hyperium.mixinsimp.entity;

import cc.hyperium.mixins.entity.IMixinEntityFx;
import net.minecraft.client.particle.EntityFX;

public class HyperiumEntityFx {
    private EntityFX parent;

    public HyperiumEntityFx(EntityFX parent){
        this.parent = parent;
    }

    public void setParticleGravity(float particleGravity) {
        ((IMixinEntityFx) parent).setParticleGravity(particleGravity);
    }

    public void setMaxAge(int age){
        ((IMixinEntityFx) parent).setParticleMaxAge(age);
    }
}
