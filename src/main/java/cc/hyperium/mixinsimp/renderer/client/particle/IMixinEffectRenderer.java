package cc.hyperium.mixinsimp.renderer.client.particle;

import net.minecraft.client.particle.IParticleFactory;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by mitchellkatz on 6/24/18. Designed for production use on Sk1er.club
 */
public interface IMixinEffectRenderer {
    Map<Integer, IParticleFactory> getParticleMap();

    AtomicInteger getConcurrentParticleInt();
}
