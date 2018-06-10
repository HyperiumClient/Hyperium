package cc.hyperium.mixins.renderer.model;

import cc.hyperium.mixinsimp.renderer.model.IMixinModelBox;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.TexturedQuad;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ModelBox.class)
public class MixinModelBox implements IMixinModelBox {

    @Shadow
    private TexturedQuad[] quadList;

    @Override
    public void offsetTextureQuad(ModelRenderer renderer, int quadId, float xOffset, float yOffset) {
        if (quadId >= 0 & quadId < this.quadList.length) {
            this.quadList[quadId].vertexPositions[0].texturePositionX += xOffset / renderer.textureWidth;
            this.quadList[quadId].vertexPositions[1].texturePositionX += xOffset / renderer.textureWidth;
            this.quadList[quadId].vertexPositions[2].texturePositionX += xOffset / renderer.textureWidth;
            this.quadList[quadId].vertexPositions[3].texturePositionX += xOffset / renderer.textureWidth;
            this.quadList[quadId].vertexPositions[0].texturePositionY += yOffset / renderer.textureHeight;
            this.quadList[quadId].vertexPositions[1].texturePositionY += yOffset / renderer.textureHeight;
            this.quadList[quadId].vertexPositions[2].texturePositionY += yOffset / renderer.textureHeight;
            this.quadList[quadId].vertexPositions[3].texturePositionY += yOffset / renderer.textureHeight;
        }
    }
}
