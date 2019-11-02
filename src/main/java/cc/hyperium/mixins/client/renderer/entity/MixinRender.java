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

package cc.hyperium.mixins.client.renderer.entity;

import cc.hyperium.mixinsimp.client.renderer.entity.HyperiumRender;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

/**
 * @author Sk1er
 */
@Mixin(Render.class)
public abstract class MixinRender<T extends Entity> {

    @Shadow @Final protected RenderManager renderManager;

    private HyperiumRender<T> hyperiumRender = new HyperiumRender<>((Render<T>) (Object) this);

    /**
     * @author Sk1er
     * @reason Nametag Shading
     */
    @Overwrite
    protected void renderOffsetLivingLabel(T entityIn, double x, double y, double z, String str, float p_177069_9_, double p_177069_10_) {
        hyperiumRender.renderOffsetLivingLabel(entityIn, x, y, z, str, p_177069_9_, p_177069_10_);
    }

    /**
     * @author Sk1er
     * @reason We do it better
     */
    @Overwrite
    protected void renderName(T entity, double x, double y, double z) {
        hyperiumRender.renderName(entity, x, y, z);
    }

    /**
     * @author Sk1er
     * @reason Nametag SHading
     */
    @Overwrite
    protected void renderLivingLabel(T entityIn, String str, double x, double y, double z, int maxDistance) {
        hyperiumRender.renderLivingLabel(entityIn, str, x, y, z, maxDistance, renderManager);
    }
}

