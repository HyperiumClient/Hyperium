package cc.hyperium.handlers.handlers.particle.particle;

import cc.hyperium.handlers.handlers.particle.IParticle;
import cc.hyperium.mixinsimp.renderer.IMixinEntityFx;
import cc.hyperium.mixinsimp.renderer.client.particle.IMixinEffectRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

import java.awt.*;
import java.util.Map;

/**
 * Created by mitchellkatz on 6/25/18. Designed for production use on Sk1er.club
 */
public class ChromaRedstoneParticle implements IParticle {
    @Override
    public EntityFX spawn(World world, double x, double y, double z) {
        Map<Integer, IParticleFactory> particleMap = ((IMixinEffectRenderer) Minecraft.getMinecraft().effectRenderer).getParticleMap();
        IParticleFactory iParticleFactory = particleMap.get(EnumParticleTypes.CLOUD.getParticleID());
        EntityFX entityFX = iParticleFactory.getEntityFX(EnumParticleTypes.HEART.getParticleID(), world, x, y, z, 0.0F,-0.01F,0.0F, 0);
        int i = Color.HSBtoRGB(System.currentTimeMillis() % 1000L / 1000.0f, 0.8f, 0.8f);
        Color color = new Color(i);
        IMixinEntityFx e = (IMixinEntityFx) entityFX;
        e.setParticleRed(color.getRed());
        e.setParticleGreen(color.getGreen());
        e.setParticleBlue(color.getBlue());
        return entityFX;
    }
}
