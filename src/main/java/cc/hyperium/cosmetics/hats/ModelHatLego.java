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

package cc.hyperium.cosmetics.hats;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelHatLego extends ModelBase {

    private ModelRenderer brickBase;
    private ModelRenderer topLeftBrick;
    private ModelRenderer bottomLeftBrick;
    private ModelRenderer bottomRightBrick;
    private ModelRenderer topRightBrick;

    public ModelHatLego() {
        textureWidth = 64;
        textureHeight = 32;

        brickBase = new ModelRenderer(this, 1, 1);
        brickBase.addBox(-4.5F, 0F, -4.5F, 9, 4, 9);
        brickBase.setRotationPoint(0F, -4F, 0F);
        brickBase.setTextureSize(textureWidth, textureHeight);
        brickBase.mirror = true;
        setRotation(brickBase);

        topLeftBrick = new ModelRenderer(this, 33, 1);
        topLeftBrick.addBox(1F, -1F, -4F, 3, 1, 3);
        topLeftBrick.setRotationPoint(0F, -4F, 0F);
        topLeftBrick.setTextureSize(64, 32);
        topLeftBrick.mirror = true;
        setRotation(topLeftBrick);

        bottomLeftBrick = new ModelRenderer(this, 49, 1);
        bottomLeftBrick.addBox(1F, -1F, 1F, 3, 1, 3);
        bottomLeftBrick.setRotationPoint(0F, -4F, 0F);
        bottomLeftBrick.setTextureSize(64, 32);
        bottomLeftBrick.mirror = true;
        setRotation(bottomLeftBrick);

        bottomRightBrick = new ModelRenderer(this, 41, 9);
        bottomRightBrick.addBox(-4F, -1F, 1F, 3, 1, 3);
        bottomRightBrick.setRotationPoint(0F, -4F, 0F);
        bottomRightBrick.setTextureSize(64, 32);
        bottomRightBrick.mirror = true;
        setRotation(bottomRightBrick);

        topRightBrick = new ModelRenderer(this, 1, 17);
        topRightBrick.addBox(-4F, -1F, -4F, 3, 1, 3);
        topRightBrick.setRotationPoint(0F, -4F, 0F);
        topRightBrick.setTextureSize(64, 32);
        topRightBrick.mirror = true;
        setRotation(topRightBrick);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5);
        brickBase.render(f5);
        topLeftBrick.render(f5);
        bottomLeftBrick.render(f5);
        bottomRightBrick.render(f5);
        topRightBrick.render(f5);
    }

    private void setRotation(ModelRenderer model) {
        model.rotateAngleX = 0;
        model.rotateAngleY = 0;
        model.rotateAngleZ = 0;
    }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, null);
    }

}
