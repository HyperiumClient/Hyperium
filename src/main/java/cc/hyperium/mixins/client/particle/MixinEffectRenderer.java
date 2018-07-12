package cc.hyperium.mixins.client.particle;

import cc.hyperium.mixinsimp.client.particle.HyperiumEffectRenderer;
import cc.hyperium.mixinsimp.renderer.client.particle.IMixinEffectRenderer;
import java.util.List;
import java.util.Map;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.IParticleFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

/**
 * Created by mitchellkatz on 6/24/18. Designed for production use on Sk1er.club
 */
@Mixin(EffectRenderer.class)
public class MixinEffectRenderer implements IMixinEffectRenderer {

    @Shadow
    private Map<Integer, IParticleFactory> particleTypes;
    @Shadow
    private List<EntityFX>[][] fxLayers;

    private HyperiumEffectRenderer hyperiumEffectRenderer = new HyperiumEffectRenderer();

    @Overwrite
    public void addEffect(EntityFX effect) {
       hyperiumEffectRenderer.addEffect(this.fxLayers,effect);
    }

    @Override
    public Map<Integer, IParticleFactory> getParticleMap() {
        return particleTypes;
    }
}
