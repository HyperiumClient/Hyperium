/*
 *     Copyright (C) 2018  Hyperium <https://hyperium.cc/>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.cosmetics.hats;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelHatFez extends ModelBase {

    ModelRenderer Part_0; // Box_0
    ModelRenderer Part_1; // Box_1
    ModelRenderer Part_2; // Box_2
    ModelRenderer Part_3; // Box_3
    ModelRenderer Part_4; // Box_4

    public ModelHatFez() {
        textureWidth = 64;
        textureHeight = 32;

        Part_0 = new ModelRenderer(this, 1, 1); // Box_0
        Part_0.addBox(-3F, 0F, -3F, 6, 4, 6);
        Part_0.setRotationPoint(0F, -4F, 0F);
        Part_0.setTextureSize(64, 32);
        Part_0.mirror = true;
        setRotation(Part_0, 0F, 0.12217305F, 0F);
        Part_1 = new ModelRenderer(this, 1, 1); // Box_2
        Part_1.addBox(0F, 0F, 0F, 1, 1, 1);
        Part_1.setRotationPoint(-0.5F, -4.75F, -0.5F);
        Part_1.setTextureSize(64, 32);
        Part_1.mirror = true;
        setRotation(Part_1, 0F, 0F, 0F);
        Part_2 = new ModelRenderer(this, 25, 1); // Box_4
        Part_2.addBox(-0.5F, -0.5F, -0.5F, 3, 1, 1);
        Part_2.setRotationPoint(0.5F, -3.75F, 0F);
        Part_2.setTextureSize(64, 32);
        Part_2.mirror = true;
        setRotation(Part_2, 0.78539816F, 0F, 0F);
        Part_3 = new ModelRenderer(this, 41, 1); // Box_5
        Part_3.addBox(-0.5F, -0.5F, -0.5F, 3, 1, 1);
        Part_3.setRotationPoint(3F, -3.5F, 0F);
        Part_3.setTextureSize(64, 32);
        Part_3.mirror = true;
        setRotation(Part_3, 0.2268928F, 0.78539816F, 1.20427718F);
        Part_4 = new ModelRenderer(this, 33, 9); // Box_6
        Part_4.addBox(-0.5F, -0.5F, -0.5F, 3, 1, 1);
        Part_4.setRotationPoint(3F, -3.5F, 0F);
        Part_4.setTextureSize(64, 32);
        Part_4.mirror = true;
        setRotation(Part_4, 0.2268928F, -0.9250245F, 1.20427718F);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5);
        Part_0.render(f5); // Box_0
        Part_1.render(f5); // Box_1
        Part_2.render(f5); // Box_2
        Part_3.render(f5); // Box_3
        Part_4.render(f5); // Box_4
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
