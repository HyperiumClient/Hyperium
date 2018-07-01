package cc.hyperium.mixins.client.particle;

import cc.hyperium.config.Settings;
import cc.hyperium.mixinsimp.renderer.client.particle.IMixinEffectRenderer;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.IParticleFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.Map;

/**
 * Created by mitchellkatz on 6/24/18. Designed for production use on Sk1er.club
 */
@Mixin(EffectRenderer.class)
public class MixinEffectRenderer implements IMixinEffectRenderer {

    @Shadow
    private Map<Integer, IParticleFactory> particleTypes;
    @Shadow
    private List<EntityFX>[][] fxLayers;

    @Overwrite
    public void addEffect(EntityFX effect) {
        int i = effect.getFXLayer();
        int j = effect.getAlpha() != 1.0F ? 0 : 1;

        if (this.fxLayers[i][j].size() >= Settings.MAX_WORLD_PARTICLES_INT) {
            this.fxLayers[i][j].remove(0);
        }

        this.fxLayers[i][j].add(effect);
    }

    @Override
    public Map<Integer, IParticleFactory> getParticleMap() {
        return particleTypes;
    }
}
