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

package cc.hyperium.cosmetics.companions.hamster;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderHamster extends RenderLiving<EntityHamster> {
    private ResourceLocation hamsterTexture = new ResourceLocation("textures/cosmetics/companions/hamsterbrown.png");

    public RenderHamster(RenderManager rendermanagerIn) {
        super(rendermanagerIn, new HamsterModel(), 0.2f);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityHamster entity) {
        return hamsterTexture;
    }

    @Override
    protected void preRenderCallback(EntityHamster entitylivingbaseIn, float partialTickTime) {
        super.preRenderCallback(entitylivingbaseIn, partialTickTime);
        GlStateManager.scale(0.5, 0.5, 0.5);
    }
}
