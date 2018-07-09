package cc.hyperium.mixins.renderer;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Render.class)
public interface IMixinRender<T extends Entity> {

    @Invoker
    void callRenderOffsetLivingLabel(T entityIn, double x, double y, double z, String str, float p_177069_9_, double p_177069_10_);

    @Invoker
    boolean callCanRenderName(T entity);

    @Invoker
    void callRenderLivingLabel(T entityIn, String str, double x, double y, double z, int maxDistance);
}
