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

package cc.hyperium.addons.customcrosshair.gui.items;

import cc.hyperium.addons.customcrosshair.utils.CustomCrosshairGraphics;

import java.awt.Color;

import net.minecraft.client.gui.GuiScreen;

import java.util.ArrayList;
import java.util.List;

public class CCListDropBox extends CCGuiItem {

    private boolean isOpen;
    private List<String> itemList;
    private String selectedItem;
    private int selectedItemIndex;

    public CCListDropBox(final GuiScreen screen) {
        super(screen);
        this.isOpen = false;
        this.itemList = new ArrayList<String>();
        this.setWidth(100);
        this.setHeight(10);
        this.itemList.add("Item One");
        this.itemList.add("Item Two");
        this.itemList.add("Item Three");
        this.itemList.add("Item Four");
        this.itemList.add("Item Five");
    }

    @Override
    public void drawItem(final int mouseX, final int mouseY) {
        CustomCrosshairGraphics
            .drawBorderedRectangle(this.getPosX(), this.getPosY(), this.getPosX() + this.getWidth(), this.getPosY() + this.getHeight(), new Color(23, 107, 192, 128), new Color(240, 240, 240, 255));
        if (this.selectedItem != null && this.selectedItem != "") {
            CustomCrosshairGraphics
                .drawString(this.selectedItem, this.getPosX() + 2, this.getPosY(), 16777215);
        } else {
            CustomCrosshairGraphics
                .drawString("No item selected.", this.getPosX() + 2, this.getPosY() + 2, 16777215);
        }
        CustomCrosshairGraphics
            .drawBorderedRectangle(this.getPosX() + this.getWidth() - 10, this.getPosY(), this.getPosX() + this.getWidth(), this.getPosY() + this.getHeight(), new Color(23, 107, 192, 128), new Color(240, 240, 240, 255));
        final int listHeight = 1 + this.itemList.size() * 10;
        CustomCrosshairGraphics
            .drawBorderedRectangle(this.getPosX(), this.getPosY() + this.getHeight(), this.getPosX() + this.getWidth(), this.getPosY() + this.getHeight() + listHeight, new Color(23, 107, 192, 128), new Color(240, 240, 240, 255));
        for (int i = 0; i < this.itemList.size(); ++i) {
            CustomCrosshairGraphics
                .drawString(this.itemList.get(i), this.getPosX() + 2, this.getPosY() + this.getHeight() + i * 10 + 2, 16777215);
        }
    }
}

