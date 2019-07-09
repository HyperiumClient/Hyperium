package cc.hyperium.mixinsimp.renderer.model;

import net.minecraft.client.model.ModelRenderer;

public interface IMixinModelBox {

    void offsetTextureQuad(ModelRenderer renderer, int quadId, float xOffset, float yOffset);

}
