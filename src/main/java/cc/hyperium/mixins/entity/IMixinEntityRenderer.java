package cc.hyperium.mixins.entity;

import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(EntityRenderer.class)
public interface IMixinEntityRenderer {
    @Accessor
    void setCloudFog(boolean cloudFog);

    @Invoker
    void callLoadShader(ResourceLocation resourceLocation);
}
