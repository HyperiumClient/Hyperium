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

package cc.hyperium.mixins.item;

import cc.hyperium.event.entity.ArrowShootEvent;
import cc.hyperium.event.EventBus;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemBow.class)
public abstract class MixinItemBow extends Item {

    @Shadow public abstract int getMaxItemUseDuration(ItemStack stack);

    /**
     * @author Amp
     * @reason Events
     */
    @Overwrite
    public void onPlayerStoppedUsing(ItemStack stack, World world, EntityPlayer entity, int timeLeft) {
        boolean infiniteArrows = entity.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, stack) > 0;
        if (infiniteArrows || entity.inventory.hasItem(Items.arrow)) {
            int maxTime = getMaxItemUseDuration(stack) - timeLeft;
            float pullBackTime = (float) maxTime / 20.0F;
            pullBackTime = (pullBackTime * pullBackTime + pullBackTime * 2.0F) / 3.0F;
            if ((double) pullBackTime < 0.1D) return;

            if (pullBackTime > 1.0F) pullBackTime = 1.0F;

            EntityArrow arrow = new EntityArrow(world, entity, pullBackTime * 2.0F);
            if (pullBackTime == 1.0F) arrow.setIsCritical(true);

            int powerEnchantmentLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack);
            if (powerEnchantmentLevel > 0) arrow.setDamage(arrow.getDamage() + (double) powerEnchantmentLevel * 0.5D + 0.5D);

            int punchEnchantmentLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, stack);
            if (punchEnchantmentLevel > 0) arrow.setKnockbackStrength(punchEnchantmentLevel);

            if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, stack) > 0) {
                arrow.setFire(100);
            }

            stack.damageItem(1, entity);
            world.playSoundAtEntity(entity, "random.bow", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + pullBackTime * 0.5F);
            if (infiniteArrows) arrow.canBePickedUp = 2;
            else entity.inventory.consumeInventoryItem(Items.arrow);

            entity.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem((ItemBow) (Object) this)]);
            if (!world.isRemote) world.spawnEntityInWorld(arrow);
            EventBus.INSTANCE.post(new ArrowShootEvent(arrow, maxTime, stack));
        }

    }
}
