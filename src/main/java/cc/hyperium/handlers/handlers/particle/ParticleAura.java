package cc.hyperium.handlers.handlers.particle;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Vec3;

import java.util.List;

/**
 * Created by mitchellkatz on 6/23/18. Designed for production use on Sk1er.club
 */
public class ParticleAura {

    private EnumParticleTypes type;
    private AbstractAnimation animation;
    private int particleMaxAge;

    public ParticleAura(EnumParticleTypes type, AbstractAnimation animation, int particleMaxAge) {

        this.type = type;
        this.animation = animation;
        this.particleMaxAge = particleMaxAge;

    }

    public int getParticleMaxAge() {
        return particleMaxAge;
    }

    public EnumParticleTypes getType() {
        return type;
    }

    public AbstractAnimation getAnimation() {
        return animation;
    }


    public List<Vec3> render(EntityPlayer entityPlayer, double x, double y, double z) {
        return animation.render(type, entityPlayer, x, y, z);

    }
}
