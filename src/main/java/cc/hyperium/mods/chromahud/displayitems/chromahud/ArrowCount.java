/*
 *  Hypixel Community Client, Client optimized for Hypixel Network
 *     Copyright (C) 2018  Hyperium Dev Team
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.mods.chromahud.displayitems.chromahud;


import cc.hyperium.mods.chromahud.ElementRenderer;
import cc.hyperium.mods.chromahud.api.Dimension;
import cc.hyperium.mods.chromahud.api.DisplayItem;
import cc.hyperium.utils.JsonHolder;
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
