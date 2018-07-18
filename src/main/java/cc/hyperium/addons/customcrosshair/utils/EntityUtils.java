package cc.hyperium.addons.customcrosshair.utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.*;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class EntityUtils {
    public static boolean isEntityHostile(final Entity e) {
        return !(e instanceof EntityBat) && !(e instanceof EntityChicken) && !(e instanceof EntityCow) && !(e instanceof EntityHorse) && !(e instanceof EntityMooshroom) && !(e instanceof EntityOcelot) && !(e instanceof EntityPig) && !(e instanceof EntityRabbit) && !(e instanceof EntitySheep) && !(e instanceof EntitySquid) && !(e instanceof EntityVillager) && !(e instanceof EntityWolf);
    }

    public static boolean isSword(final ItemStack itemStack) {
        final Item item = itemStack.getItem();
        return item == Items.wooden_sword || item == Items.stone_sword || item == Items.iron_sword || item == Items.golden_sword || item == Items.diamond_sword;
    }
}
