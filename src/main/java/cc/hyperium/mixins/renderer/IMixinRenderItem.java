package cc.hyperium.mixins.renderer;

import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.model.IBakedModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(RenderItem.class)
public interface IMixinRenderItem {

    @Accessor
    TextureManager getTextureManager();

    @Accessor
    ItemModelMesher getItemModelMesher();

    @Invoker
    void callSetupGuiTransform(int xPosition, int yPosition, boolean isGui3d);

    @Invoker
    void callRenderModel(IBakedModel model, int e);




}
