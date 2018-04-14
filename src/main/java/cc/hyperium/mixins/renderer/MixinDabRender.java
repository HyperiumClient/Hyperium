package cc.hyperium.mixins.renderer;

import cc.hyperium.Hyperium;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ModelBiped.class)
public class MixinDabRender extends ModelBase {

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;pushMatrix()V"))
    public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale, CallbackInfo i) {
        if (entityIn instanceof AbstractClientPlayer) {
            ModelBase modelBase = this;
            if (modelBase instanceof ModelPlayer) {
                ModelPlayer player = (ModelPlayer) modelBase;
                Hyperium.INSTANCE.getHandlers().getDabHandler().modify(((AbstractClientPlayer) entityIn), player);
            } else {
                if (modelBase instanceof ModelBiped)
                    Hyperium.INSTANCE.getHandlers().getDabHandler().modify(((AbstractClientPlayer) entityIn), ((ModelBiped) modelBase));

            }
        }
    }
}
