package cc.hyperium.mixins.client.particle;

import cc.hyperium.mixinsimp.renderer.client.particle.IMixinEffectRenderer;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.IParticleFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

/**
 * Created by mitchellkatz on 6/24/18. Designed for production use on Sk1er.club
 */
@Mixin(EffectRenderer.class)
public class MixinEffectRenderer implements IMixinEffectRenderer {

    @Shadow
    private Map<Integer, IParticleFactory> particleTypes;

    @Override
    public Map<Integer, IParticleFactory> getParticleMap() {
        return particleTypes;
    }
}
