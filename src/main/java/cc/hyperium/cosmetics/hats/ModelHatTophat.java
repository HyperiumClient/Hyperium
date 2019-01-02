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

public class ModelHatTophat extends ModelBase {

    ModelRenderer Part_0; // Box_0
    ModelRenderer Part_1; // Box_1

    public ModelHatTophat() {
        textureWidth = 64;
        textureHeight = 64;

        Part_0 = new ModelRenderer(this, 1, 1); // Box_7
        Part_0.addBox(0F, 0F, 0F, 10, 1, 10);
        Part_0.setRotationPoint(-5F, -1F, -5F);
        Part_0.setTextureSize(64, 64);
        Part_0.mirror = true;
        setRotation(Part_0, 0F, 0F, 0F);
        Part_1 = new ModelRenderer(this, 1, 17); // Box_8
        Part_1.addBox(0F, 0F, 0F, 8, 8, 8);
        Part_1.setRotationPoint(-4F, -9F, -4F);
        Part_1.setTextureSize(64, 64);
        Part_1.mirror = true;
        setRotation(Part_1, 0F, 0F, 0F);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5);
        Part_0.render(f5); // Box_0
        Part_1.render(f5); // Box_1
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
