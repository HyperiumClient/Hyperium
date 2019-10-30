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

public class ModelHatTophat extends ModelBase {

    private ModelRenderer bottomLayer;
    private ModelRenderer topLayer;

    public ModelHatTophat() {
        textureWidth = 64;
        textureHeight = 64;

        bottomLayer = new ModelRenderer(this, 1, 1);
        bottomLayer.addBox(0F, 0F, 0F, 10, 1, 10);
        bottomLayer.setRotationPoint(-5F, -1F, -5F);
        bottomLayer.setTextureSize(textureWidth, textureHeight);
        bottomLayer.mirror = true;
        setRotation(bottomLayer);

        topLayer = new ModelRenderer(this, 1, 17);
        topLayer.addBox(0F, 0F, 0F, 8, 8, 8);
        topLayer.setRotationPoint(-4F, -9F, -4F);
        topLayer.setTextureSize(textureWidth, textureHeight);
        topLayer.mirror = true;
        setRotation(topLayer);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5);
        bottomLayer.render(f5);
        topLayer.render(f5);
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
