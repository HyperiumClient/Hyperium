package cc.hyperium.mixins.renderer;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ItemRenderer.class)
public interface IMixinItemRenderer {
    @Invoker
    void callFunc_178101_a(float angle, float p_178101_2_);

    @Invoker
    void callFunc_178109_a(AbstractClientPlayer clientPlayer);

    @Invoker
    void callFunc_178110_a(EntityPlayerSP entityplayerspIn, float partialTicks);

    @Invoker
    void callRenderItemMap(AbstractClientPlayer clientPlayer, float p_178097_2_, float p_178097_3_, float p_178097_4_);

    @Invoker
    void callFunc_178104_a(AbstractClientPlayer clientPlayer, float p_178104_2_);

    @Invoker
    void callFunc_178103_d();

    @Invoker
    void callFunc_178098_a(float p_178098_1_, AbstractClientPlayer clientPlayer);

    @Invoker
    void callFunc_178105_d(float p_178105_1_);

    @Invoker
    void callRenderItem(EntityLivingBase entityIn, ItemStack heldStack, ItemCameraTransforms.TransformType transform);

    @Invoker
    void callFunc_178095_a(AbstractClientPlayer clientPlayer, float p_178095_2_, float p_178095_3_);

}
