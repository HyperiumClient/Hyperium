package cc.hyperium.handlers.handlers.particle;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.world.World;

/**
 * Created by mitchellkatz on 6/25/18. Designed for production use on Sk1er.club
 */
public interface IParticle {

    EntityFX spawn(World world, double x, double y, double z);

}
