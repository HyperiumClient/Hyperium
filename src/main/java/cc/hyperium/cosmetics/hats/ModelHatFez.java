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

public class ModelHatFez extends ModelBase {

    private ModelRenderer baseLayer;
    private ModelRenderer topLayer;
    private ModelRenderer stringLayer;
    private ModelRenderer danglingStringLayer;
    private ModelRenderer otherDanglingStringLayer;

    public ModelHatFez() {
        textureWidth = 64;
        textureHeight = 32;

        baseLayer = new ModelRenderer(this, 1, 1);
        baseLayer.addBox(-3F, 0F, -3F, 6, 4, 6);
        baseLayer.setRotationPoint(0F, -4F, 0F);
        baseLayer.setTextureSize(textureWidth, textureHeight);
        baseLayer.mirror = true;
        setRotation(baseLayer, 0F, 0.12217305F, 0F);

        topLayer = new ModelRenderer(this, 1, 1);
        topLayer.addBox(0F, 0F, 0F, 1, 1, 1);
        topLayer.setRotationPoint(-0.5F, -4.75F, -0.5F);
        topLayer.setTextureSize(textureWidth, textureHeight);
        topLayer.mirror = true;
        setRotation(topLayer, 0F, 0F, 0F);

        stringLayer = new ModelRenderer(this, 25, 1);
        stringLayer.addBox(-0.5F, -0.5F, -0.5F, 3, 1, 1);
        stringLayer.setRotationPoint(0.5F, -3.75F, 0F);
        stringLayer.setTextureSize(textureWidth, textureHeight);
        stringLayer.mirror = true;
        setRotation(stringLayer, 0.78539816F, 0F, 0F);

        danglingStringLayer = new ModelRenderer(this, 41, 1);
        danglingStringLayer.addBox(-0.5f, -0.5f, -0.5f, 3, 1, 1);
        danglingStringLayer.setRotationPoint(3F, -3.5F, 0F);
        danglingStringLayer.setTextureSize(textureWidth, textureHeight);
        danglingStringLayer.mirror = true;
        setRotation(danglingStringLayer, 0.2268928F, 0.78539816F, 1.20427718F);

        otherDanglingStringLayer = new ModelRenderer(this, 33, 9);
        otherDanglingStringLayer.addBox(-0.5f, -0.5F, -0.5F, 3, 1, 1);
        otherDanglingStringLayer.setRotationPoint(3F, -3.5F, 0F);
        otherDanglingStringLayer.setTextureSize(textureWidth, textureHeight);
        otherDanglingStringLayer.mirror = true;
        setRotation(otherDanglingStringLayer, 0.2268928F, -0.9250245F, 1.20427718F);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5);
        baseLayer.render(f5);
        topLayer.render(f5);
        stringLayer.render(f5);
        danglingStringLayer.render(f5);
        otherDanglingStringLayer.render(f5);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, null);
    }

}
