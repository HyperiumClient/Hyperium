package com.hcc.mods.chromahud.displayitems.hcc.chromahud;


import com.hcc.mods.chromahud.ElementRenderer;
import com.hcc.mods.chromahud.api.Dimension;
import com.hcc.mods.chromahud.api.DisplayItem;
import com.hcc.utils.JsonHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mitchell Katz on 5/29/2017.
 */
public class ArmourHud extends DisplayItem {
    List<ItemStack> list = new ArrayList<>();
    private int ordinal;
    private boolean dur = false;
    private boolean hand = false;

    public ArmourHud(JsonHolder raw, int ordinal) {
        super(raw, ordinal);
        this.dur = raw.optBoolean("dur");
        this.hand = raw.optBoolean("hand");
    }

    @Override
    public void save() {
        data.put("dur", dur);
        data.put("hand", hand);

    }

    @Override
    public Dimension draw(int starX, double startY, boolean isConfig) {
        list.clear();
        if (isConfig) {
            list.add(new ItemStack(Item.getItemById(276), 1));
            list.add(new ItemStack(Item.getItemById(261), 1));
            list.add(new ItemStack(Item.getItemById(262), 64));
            list.add(new ItemStack(Item.getItemById(310), 1));
            list.add(new ItemStack(Item.getItemById(311), 1));
            list.add(new ItemStack(Item.getItemById(312), 1));
            list.add(new ItemStack(Item.getItemById(313), 1));
        } else {
            list = itemsToRender();
        }
        drawArmour(starX, startY);

        return new Dimension(16, getHeight());
    }

    public void drawArmour(int x, double y) {
        RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
        EntityPlayerSP thePlayer = Minecraft.getMinecraft().thePlayer;
        if (thePlayer == null || renderItem == null) {
            return;
        }
        ElementRenderer.render(list, x, y, dur);
    }

    public int getHeight() {
        return list.size() * 16;
    }

    private List<ItemStack> itemsToRender() {
        List<ItemStack> items = new ArrayList<>();
        ItemStack heldItem = Minecraft.getMinecraft().thePlayer.getHeldItem();
        if (hand && heldItem != null)
            items.add(heldItem);
        ItemStack[] inventory = Minecraft.getMinecraft().thePlayer.inventory.armorInventory;
        for (int i = 3; i >= 0; i--) {
            if (inventory[i] != null && inventory[i].getItem() != null) {
                items.add(inventory[i]);
            }
        }
        for (ItemStack is : Minecraft.getMinecraft().thePlayer.inventory.mainInventory) {
            if (is != null) {
                if (is.getUnlocalizedName().equalsIgnoreCase("item.bow")) {
                    items.add(is);
                    break;
                }

            }

        }


        return items;
    }


    public void toggleDurability() {
        this.dur = !dur;
    }

    public void toggleHand() {
        this.hand = !hand;
    }

}
