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

package cc.hyperium.mixins.renderer;

import cc.hyperium.mixinsimp.renderer.HyperiumItemRenderer;
import cc.hyperium.mods.itemphysic.physics.ClientPhysic;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public class MixinItemRenderer {

    @Final
    @Shadow
    private Minecraft mc;

    @Shadow
    private ItemStack itemToRender;

    @Shadow
    private float equippedProgress;
    @Shadow
    private float prevEquippedProgress;
    private HyperiumItemRenderer hyperiumItemRenderer = new HyperiumItemRenderer((ItemRenderer) (Object) this);


    /**
     * @author Cubxity
     * @reason 1.7 animations
     */
    @Overwrite
    private void transformFirstPersonItem(float equipProgress, float swingProgress) {
        hyperiumItemRenderer.transformFirstPersonItem(equipProgress, swingProgress);
    }


    /**
     * @author CoalOres
     * @reason 1.7 animations
     */
    @Overwrite
    public void renderItemInFirstPerson(float partialTicks) {
        hyperiumItemRenderer.renderItemInFirstPerson(partialTicks, prevEquippedProgress, equippedProgress, itemToRender);
    }
}
