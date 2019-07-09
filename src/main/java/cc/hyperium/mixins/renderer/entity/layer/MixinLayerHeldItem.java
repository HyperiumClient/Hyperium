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

package cc.hyperium.mixins.renderer.entity.layer;

import cc.hyperium.mods.oldanimations.OldBlocking;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LayerHeldItem.class)
public class MixinLayerHeldItem {

    private OldBlocking oldBlocking = new OldBlocking();

    @Shadow
    @Final
    private RendererLivingEntity<?> livingEntityRenderer;

    /**
     * @author SiroQ
     * @reason Adds compatibility for the 1.7 blocking animation
     */
    @Overwrite
    public void doRenderLayer(EntityLivingBase entity, float f, float f2, float f3, float partialTicks, float f4, float f5, float scale) {
        oldBlocking.doRenderLayer(entity, this.livingEntityRenderer);
    }
}
