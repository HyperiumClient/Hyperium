package com.hcc.mods.chromahud.displayitems.hcc.chromahud;


import com.hcc.mods.chromahud.ElementRenderer;
import com.hcc.mods.chromahud.api.Dimension;
import com.hcc.mods.chromahud.api.DisplayItem;
import com.hcc.utils.JsonHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mitchellkatz on 6/25/17.
 */
public class ArrowCount extends DisplayItem {
    private JsonHolder data;

    public ArrowCount(JsonHolder data, int ordinal) {
        super(data, ordinal);
        this.data = data;
    }


    @Override
    public Dimension draw(int starX, double startY, boolean isConfig) {
        List<ItemStack> list = new ArrayList<>();
        list.add(new ItemStack(Item.getItemById(262), 64));
        EntityPlayerSP thePlayer = Minecraft.getMinecraft().thePlayer;
        if (thePlayer != null) {
            int c = 0;
            for (ItemStack is : thePlayer.inventory.mainInventory) {
                if (is != null) {
                    if (is.getUnlocalizedName().equalsIgnoreCase("item.arrow"))
                        c += is.stackSize;
                }

            }
            ElementRenderer.render(list, starX, startY, false);
            ElementRenderer.draw(starX + 16, startY + 8, "x" + (isConfig ? 64:c));
            return new Dimension(16, 16);
        }
        return new Dimension(0, 0);
    }

}
