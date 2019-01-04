package cc.hyperium.mixins.client.renderer.entity.layers;

import cc.hyperium.mixinsimp.client.renderer.entity.layers.HyperiumLayerArmorBase;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LayerArmorBase.class)
public abstract class MixinLayerArmorBase<T extends ModelBase> implements LayerRenderer<EntityLivingBase> {

    @Shadow
    @Final
    private RendererLivingEntity<?> renderer;
    private HyperiumLayerArmorBase<ModelBase> hyperiumLayerArmorBase = new HyperiumLayerArmorBase<>(renderer);

    /**
     * @author asbyth
     * @reason Disable Enchantment Glint on worn Armor pieces
     */
    @Inject(method = "func_177183_a", at = @At("HEAD"), cancellable = true)
    public void func_177183_a(EntityLivingBase entitylivingbaseIn, T modelbaseIn, float p_177183_3_, float p_177183_4_, float p_177183_5_, float p_177183_6_, float p_177183_7_, float p_177183_8_, float p_177183_9_, CallbackInfo ci) {
        hyperiumLayerArmorBase.renderEffect(entitylivingbaseIn, modelbaseIn, p_177183_3_, p_177183_4_, p_177183_5_, p_177183_6_, p_177183_7_, p_177183_8_, p_177183_9_, ci);
    }
}
