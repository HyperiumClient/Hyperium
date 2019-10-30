/*
 *       Copyright (C) 2018-present Hyperium <https://hyperium.cc/>
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU Lesser General Public License as published
 *       by the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU Lesser General Public License for more details.
 *
 *       You should have received a copy of the GNU Lesser General Public License
 *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.mixins.client.model;

import cc.hyperium.mixinsimp.client.model.IMixinModelBox;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.TexturedQuad;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ModelBox.class)
public class MixinModelBox implements IMixinModelBox {

    @Shadow private TexturedQuad[] quadList;

    @Override
    public void offsetTextureQuad(ModelRenderer renderer, int quadId, float xOffset, float yOffset) {
        if (quadId >= 0 & quadId < quadList.length) {
            quadList[quadId].vertexPositions[0].texturePositionX += xOffset / renderer.textureWidth;
            quadList[quadId].vertexPositions[1].texturePositionX += xOffset / renderer.textureWidth;
            quadList[quadId].vertexPositions[2].texturePositionX += xOffset / renderer.textureWidth;
            quadList[quadId].vertexPositions[3].texturePositionX += xOffset / renderer.textureWidth;
            quadList[quadId].vertexPositions[0].texturePositionY += yOffset / renderer.textureHeight;
            quadList[quadId].vertexPositions[1].texturePositionY += yOffset / renderer.textureHeight;
            quadList[quadId].vertexPositions[2].texturePositionY += yOffset / renderer.textureHeight;
            quadList[quadId].vertexPositions[3].texturePositionY += yOffset / renderer.textureHeight;
        }
    }
}
