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

package cc.hyperium.mods.chromahud.displayitems.chromahud;


import cc.hyperium.mods.chromahud.ElementRenderer;
import cc.hyperium.mods.chromahud.api.DisplayItem;
import cc.hyperium.utils.JsonHolder;
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
    private List<ItemStack> list = new ArrayList<>();
    private int ordinal;
    private boolean dur = false;
    private boolean hand = false;
    private boolean armourOnTop = false;

    public ArmourHud(JsonHolder raw, int ordinal) {
        super(raw, ordinal);
        this.dur = raw.optBoolean("dur");
        this.armourOnTop = raw.optBoolean("armourOnTop");
        this.hand = raw.optBoolean("hand");
        this.width = 16;
    }

    @Override
    public void save() {
        data.put("dur", dur);
        data.put("hand", hand);
        data.put("armourOnTop", armourOnTop);
    }

    @Override
    public void draw(int starX, double startY, boolean isConfig) {
        list.clear();
        if (isConfig) {
            if (isArmourOnTop()) {
                list.add(new ItemStack(Item.getItemById(310), 1));
                list.add(new ItemStack(Item.getItemById(311), 1));
                list.add(new ItemStack(Item.getItemById(312), 1));
                list.add(new ItemStack(Item.getItemById(313), 1));
                list.add(new ItemStack(Item.getItemById(276), 1));
                list.add(new ItemStack(Item.getItemById(261), 1));
                list.add(new ItemStack(Item.getItemById(262), 64));

            } else {
                list.add(new ItemStack(Item.getItemById(276), 1));
                list.add(new ItemStack(Item.getItemById(261), 1));
                list.add(new ItemStack(Item.getItemById(262), 64));
                list.add(new ItemStack(Item.getItemById(310), 1));
                list.add(new ItemStack(Item.getItemById(311), 1));
                list.add(new ItemStack(Item.getItemById(312), 1));
                list.add(new ItemStack(Item.getItemById(313), 1));
            }
        } else {
            list = itemsToRender();
        }
        drawArmour(starX, startY);

        this.height = getArmourHeight();
    }

    private void drawArmour(int x, double y) {
        RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
        EntityPlayerSP thePlayer = Minecraft.getMinecraft().thePlayer;
        if (thePlayer == null || renderItem == null) {
            return;
        }
        ElementRenderer.render(list, x, y, dur);
    }

    private int getArmourHeight() {
        return list.size() * 16;
    }

    private List<ItemStack> itemsToRender() {
        List<ItemStack> items = new ArrayList<>();
        ItemStack heldItem = Minecraft.getMinecraft().thePlayer.getHeldItem();
        if (hand && heldItem != null && !armourOnTop)
            items.add(heldItem);
        ItemStack[] inventory = Minecraft.getMinecraft().thePlayer.inventory.armorInventory;
        for (int i = 3; i >= 0; i--) {
            if (inventory[i] != null && inventory[i].getItem() != null) {
                items.add(inventory[i]);
            }
        }
        if (hand && heldItem != null && armourOnTop)
            items.add(heldItem);


        return items;
    }


    public void toggleDurability() {
        this.dur = !dur;
    }

    public void toggleHand() {
        this.hand = !hand;
    }

    public boolean isArmourOnTop() {
        return armourOnTop;
    }

    public void setArmourOnTop(boolean armourOnTop) {
        this.armourOnTop = armourOnTop;
    }
}
