package cc.hyperium.mixins.entity;

import cc.hyperium.event.EntityRenderEvent;
import cc.hyperium.event.EventBus;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ModelBiped.class)
public class MixinModelBiped {

    @Inject(method = "render", at = @At("HEAD") ,cancellable = true)
    private void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale, CallbackInfo ci)
    {
        ModelBiped model = (ModelBiped) (Object) this;
        EntityRenderEvent event = new EntityRenderEvent(entityIn, model, p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale);
        EventBus.INSTANCE.post(event);
        if(event.isCancelled()) {
            ci.cancel();
        }
    }
}
