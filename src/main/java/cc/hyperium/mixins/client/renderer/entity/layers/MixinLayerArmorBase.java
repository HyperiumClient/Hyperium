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

package cc.hyperium.mixins.client.renderer.entity.layers;

import cc.hyperium.config.Settings;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LayerArmorBase.class)
public abstract class MixinLayerArmorBase<T extends ModelBase> implements LayerRenderer<EntityLivingBase> {

    @Shadow public abstract ItemStack getCurrentArmor(EntityLivingBase entitylivingbaseIn, int armorSlot);

    /**
     * @author asbyth
     * @reason Disable Enchantment Glint on worn Armor pieces
     */
    @Inject(method = "renderGlint", at = @At("HEAD"), cancellable = true)
    private void renderGlint(EntityLivingBase entitylivingbaseIn, T modelbaseIn, float limbSwing, float limbSwingAmount,
                             float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, CallbackInfo ci) {
        if (Settings.DISABLE_ENCHANT_GLINT) ci.cancel();
    }

    @Inject(method = "renderLayer", at = @At("HEAD"), cancellable = true)
    private void preRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks,
                                float netHeadYaw, float headPitch, float scale, int armorSlot, CallbackInfo ci) {
        ItemStack itemstack = this.getCurrentArmor(entitylivingbaseIn, armorSlot);

        if (itemstack != null && itemstack.getItem() instanceof ItemArmor) {
            Item item = itemstack.getItem();
            if (Settings.HIDE_LEATHER_ARMOR &&
                (item == Items.leather_boots || item == Items.leather_leggings || item == Items.leather_chestplate || item == Items.leather_helmet)) {
                ci.cancel();
            }
        }
    }
}
