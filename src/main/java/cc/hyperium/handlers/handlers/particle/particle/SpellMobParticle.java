package cc.hyperium.handlers.handlers.particle.particle;

import cc.hyperium.handlers.handlers.particle.IParticle;
import cc.hyperium.mixinsimp.renderer.client.particle.IMixinEffectRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

import java.util.Map;

/**
 * Created by mitchellkatz on 6/25/18. Designed for production use on Sk1er.club
 */
public class SpellMobParticle implements IParticle {
    @Override
    public EntityFX spawn(World world, double x, double y, double z) {
        Map<Integer, IParticleFactory> particleMap = ((IMixinEffectRenderer) Minecraft.getMinecraft().effectRenderer).getParticleMap();
        IParticleFactory iParticleFactory = particleMap.get(EnumParticleTypes.SPELL_MOB.getParticleID());
        return iParticleFactory.getEntityFX(EnumParticleTypes.SPELL_MOB.getParticleID(), world, x, y, z, 0.0F, -0.1F, 0.0F, 0);
    }
}
