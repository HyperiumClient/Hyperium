package cc.hyperium.mixins.renderer;

import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(RendererLivingEntity.class)
public interface IMixinRenderLivingEntity<T extends EntityLivingBase> {

    @Invoker
    boolean callSetBrightness(T entitylivingbaseIn, float partialTicks, boolean combineTextures);

    @Invoker
    void callUnsetBrightness();

    @Invoker
    float callGetDeathMaxRotation(T entityLivingBaseIn);

    @Invoker
    boolean callCanRenderName(T entity);

    @Invoker
    <V extends EntityLivingBase, U extends LayerRenderer<V>> boolean callAddLayer(U layer);
}
