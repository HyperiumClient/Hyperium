package cc.hyperium.mixins.client.particle;

import cc.hyperium.mixinsimp.client.particle.HyperiumEffectRenderer;
import cc.hyperium.mixinsimp.renderer.client.particle.IMixinEffectRenderer;
import java.util.List;
import java.util.Map;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.IParticleFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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

    @Inject(method = "addEffect",at=@At(value = "INVOKE_ASSIGN",target = "Lnet/minecraft/client/particle/EntityFX;getAlpha()F",ordinal = 0))
    public void addEffectBefore(EntityFX effect, CallbackInfo ci) {
       hyperiumEffectRenderer.addEffectBefore(this.fxLayers,effect);
    }

    @Inject(method = "addEffect",at=@At(value = "INVOKE_ASSIGN",target = "Ljava/util/List;remove(I)Ljava/lang/Object;",ordinal = 0))
    public void addEffectAfter(EntityFX effect, CallbackInfo ci) {
        hyperiumEffectRenderer.addEffectAfter(this.fxLayers,effect);
    }

    @Override
    public Map<Integer, IParticleFactory> getParticleMap() {
        return particleTypes;
    }
}
