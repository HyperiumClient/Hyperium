package cc.hyperium.mixinsimp.client.renderer.entity.layers;

import cc.hyperium.config.Settings;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class HyperiumLayerArmorBase<T extends ModelBase> implements LayerRenderer<EntityLivingBase> {

    private final RendererLivingEntity<?> renderer;

    public HyperiumLayerArmorBase(RendererLivingEntity<?> rendererIn) {
        this.renderer = rendererIn;
    }

    public void renderEffect(EntityLivingBase entitylivingbaseIn, T modelbaseIn, float p_177183_3_, float p_177183_4_, float p_177183_5_, float p_177183_6_, float p_177183_7_, float p_177183_8_, float p_177183_9_, CallbackInfo ci) {
        if (Settings.DISABLE_ENCHANT_GLINT) {
            ci.cancel();
        }
    }

    @Override
    public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale) {

    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
