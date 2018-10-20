package cc.hyperium.mixins.client.particle;

import cc.hyperium.config.Settings;
import cc.hyperium.mixinsimp.client.particle.HyperiumEffectRenderer;
import cc.hyperium.mixinsimp.renderer.client.particle.IMixinEffectRenderer;
import com.google.common.collect.Lists;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.EntityParticleEmitter;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.util.ReportedException;
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
    private TextureManager renderer;

    /**
     * @author Sk1er
     * @reason Fix NPE
     */
    @Overwrite
    private void tickParticle(EntityFX p_178923_1_) {
        if (p_178923_1_ == null)
            return;
        try {
            p_178923_1_.onUpdate();
        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Ticking Particle");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Particle being ticked");
            final int i = p_178923_1_.getFXLayer();
            crashreportcategory.addCrashSectionCallable("Particle", p_178923_1_::toString);
            crashreportcategory.addCrashSectionCallable("Particle Type", () -> i == 0 ? "MISC_TEXTURE" : (i == 1 ? "TERRAIN_TEXTURE" : (i == 3 ? "ENTITY_PARTICLE_TEXTURE" : "Unknown - " + i)));
            throw new ReportedException(crashreport);
        }
    }

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
                    try {
                        this.updateEffectAlphaLayer(this.fxLayers[p_178922_1_][finalI]);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
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

    /**
     * @author Sk1er
     * @Reason fix NPE
     */
    @Overwrite
    public void renderParticles(Entity entityIn, float partialTicks) {
        float f = ActiveRenderInfo.getRotationX();
        float f1 = ActiveRenderInfo.getRotationZ();
        float f2 = ActiveRenderInfo.getRotationYZ();
        float f3 = ActiveRenderInfo.getRotationXY();
        float f4 = ActiveRenderInfo.getRotationXZ();
        EntityFX.interpPosX = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * (double) partialTicks;
        EntityFX.interpPosY = entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * (double) partialTicks;
        EntityFX.interpPosZ = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * (double) partialTicks;
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.alphaFunc(516, 0.003921569F);

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 2; ++j) {
                final int i_f = i;

                if (!this.fxLayers[i][j].isEmpty()) {
                    switch (j) {
                        case 0:
                            GlStateManager.depthMask(false);
                            break;
                        case 1:
                            GlStateManager.depthMask(true);
                    }

                    switch (i) {
                        case 0:
                        default:
                            this.renderer.bindTexture(particleTextures);
                            break;
                        case 1:
                            this.renderer.bindTexture(TextureMap.locationBlocksTexture);
                    }

                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    Tessellator tessellator = Tessellator.getInstance();
                    WorldRenderer worldrenderer = tessellator.getWorldRenderer();
                    worldrenderer.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);

                    for (int k = 0; k < this.fxLayers[i][j].size(); ++k) {
                        final EntityFX entityfx = (EntityFX) this.fxLayers[i][j].get(k);

                        try {
                            if (entityfx == null)
                                continue;
                            entityfx.renderParticle(worldrenderer, entityIn, partialTicks, f, f4, f1, f2, f3);
                        } catch (Throwable throwable) {
                            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Rendering Particle");
                            CrashReportCategory crashreportcategory = crashreport.makeCategory("Particle being rendered");
                            crashreportcategory.addCrashSectionCallable("Particle", entityfx::toString);
                            crashreportcategory.addCrashSectionCallable("Particle Type", () -> i_f == 0 ? "MISC_TEXTURE" : (i_f == 1 ? "TERRAIN_TEXTURE" : (i_f == 3 ? "ENTITY_PARTICLE_TEXTURE" : "Unknown - " + i_f)));
                            throw new ReportedException(crashreport);
                        }
                    }

                    tessellator.draw();
                }
            }
        }

        GlStateManager.depthMask(true);
        GlStateManager.disableBlend();
        GlStateManager.alphaFunc(516, 0.1F);
    }

    @Override
    public Map<Integer, IParticleFactory> getParticleMap() {
        return particleTypes;
    }
}
