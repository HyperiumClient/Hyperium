package cc.hyperium.mixins.item;

import cc.hyperium.event.ArrowShootEvent;
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

    @Shadow
    public abstract int getMaxItemUseDuration(ItemStack p_getMaxItemUseDuration_1_);

    /**
     * @author Amp
     * @reason Events
     */
    @Overwrite
    public void onPlayerStoppedUsing(ItemStack p_onPlayerStoppedUsing_1_, World p_onPlayerStoppedUsing_2_, EntityPlayer p_onPlayerStoppedUsing_3_, int p_onPlayerStoppedUsing_4_) {
        boolean lvt_5_1_ = p_onPlayerStoppedUsing_3_.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, p_onPlayerStoppedUsing_1_) > 0;
        if (lvt_5_1_ || p_onPlayerStoppedUsing_3_.inventory.hasItem(Items.arrow)) {
            int lvt_6_1_ = this.getMaxItemUseDuration(p_onPlayerStoppedUsing_1_) - p_onPlayerStoppedUsing_4_;
            float lvt_7_1_ = (float) lvt_6_1_ / 20.0F;
            lvt_7_1_ = (lvt_7_1_ * lvt_7_1_ + lvt_7_1_ * 2.0F) / 3.0F;
            if ((double) lvt_7_1_ < 0.1D) {
                return;
            }

            if (lvt_7_1_ > 1.0F) {
                lvt_7_1_ = 1.0F;
            }

            EntityArrow lvt_8_1_ = new EntityArrow(p_onPlayerStoppedUsing_2_, p_onPlayerStoppedUsing_3_, lvt_7_1_ * 2.0F);
            if (lvt_7_1_ == 1.0F) {
                lvt_8_1_.setIsCritical(true);
            }

            int lvt_9_1_ = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, p_onPlayerStoppedUsing_1_);
            if (lvt_9_1_ > 0) {
                lvt_8_1_.setDamage(lvt_8_1_.getDamage() + (double) lvt_9_1_ * 0.5D + 0.5D);
            }

            int lvt_10_1_ = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, p_onPlayerStoppedUsing_1_);
            if (lvt_10_1_ > 0) {
                lvt_8_1_.setKnockbackStrength(lvt_10_1_);
            }

            if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, p_onPlayerStoppedUsing_1_) > 0) {
                lvt_8_1_.setFire(100);
            }

            p_onPlayerStoppedUsing_1_.damageItem(1, p_onPlayerStoppedUsing_3_);
            p_onPlayerStoppedUsing_2_.playSoundAtEntity(p_onPlayerStoppedUsing_3_, "random.bow", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + lvt_7_1_ * 0.5F);
            if (lvt_5_1_) {
                lvt_8_1_.canBePickedUp = 2;
            } else {
                p_onPlayerStoppedUsing_3_.inventory.consumeInventoryItem(Items.arrow);
            }

            p_onPlayerStoppedUsing_3_.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem((ItemBow) (Object) this)]);
            if (!p_onPlayerStoppedUsing_2_.isRemote) {
                p_onPlayerStoppedUsing_2_.spawnEntityInWorld(lvt_8_1_);
            }
            EventBus.INSTANCE.post(new ArrowShootEvent(lvt_8_1_, lvt_6_1_, p_onPlayerStoppedUsing_1_));
        }

    }
}
