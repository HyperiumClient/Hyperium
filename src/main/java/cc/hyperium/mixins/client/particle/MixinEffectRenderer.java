package cc.hyperium.mixins.client.particle;

import cc.hyperium.config.Settings;
import cc.hyperium.mixinsimp.client.particle.HyperiumEffectRenderer;
import cc.hyperium.mixinsimp.renderer.client.particle.IMixinEffectRenderer;
import com.google.common.collect.Lists;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.EntityParticleEmitter;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by mitchellkatz on 6/24/18. Designed for production use on Sk1er.club
 */
@Mixin(EffectRenderer.class)
public abstract class MixinEffectRenderer implements IMixinEffectRenderer {

    @Shadow
    @Final
    private static ResourceLocation particleTextures;
    @Shadow
    private Map<Integer, IParticleFactory> particleTypes;
    @Shadow
    private List<EntityFX>[][] fxLayers;
    @Shadow
    private List<EntityParticleEmitter> particleEmitters;
    private ExecutorService service = Executors.newFixedThreadPool(9);
    private HyperiumEffectRenderer hyperiumEffectRenderer = new HyperiumEffectRenderer();
    private AtomicInteger integer = new AtomicInteger(-1);

    @Shadow
    protected abstract void tickParticle(EntityFX p_178923_1_);

    @Shadow
    protected abstract void updateEffectAlphaLayer(List<EntityFX> p_178925_1_);

    @Override
    public AtomicInteger getConcurrentParticleInt() {
        return integer;
    }

    /**
     * @author Sk1er
     * @reason add concurrency
     */
    @Overwrite
    private void updateEffectLayer(int p_178922_1_) {
        for (int i = 0; i < 2; ++i) {
            int finalI = i;
            if (Settings.IMPROVE_PARTICLE_RUN) {
                service.execute(() -> {
                    this.updateEffectAlphaLayer(this.fxLayers[p_178922_1_][finalI]);
                    integer.getAndIncrement();
                });
            } else {
                this.updateEffectAlphaLayer(this.fxLayers[p_178922_1_][finalI]);
            }
        }
    }

    /**
     * @author Sk1er
     * @reason add concurrency
     */
    @Overwrite
    public void updateEffects() {
        integer.set(0);
        for (int i = 0; i < 4; ++i) {
            this.updateEffectLayer(i);
        }
        List<EntityParticleEmitter> list = Lists.newArrayList();

        for (EntityParticleEmitter entityparticleemitter : this.particleEmitters) {
            entityparticleemitter.onUpdate();
            if (entityparticleemitter.isDead) {
                list.add(entityparticleemitter);
            }
        }
        this.particleEmitters.removeAll(list);
    }


    @Inject(method = "addEffect", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/particle/EntityFX;getAlpha()F", ordinal = 0))
    public void addEffectBefore(EntityFX effect, CallbackInfo ci) {
        hyperiumEffectRenderer.addEffectBefore(this.fxLayers, effect);
    }

    @Inject(method = "addEffect", at = @At(value = "INVOKE_ASSIGN", target = "Ljava/util/List;remove(I)Ljava/lang/Object;", ordinal = 0))
    public void addEffectAfter(EntityFX effect, CallbackInfo ci) {
        hyperiumEffectRenderer.addEffectAfter(this.fxLayers, effect);
    }


    @Override
    public Map<Integer, IParticleFactory> getParticleMap() {
        return particleTypes;
    }
}
