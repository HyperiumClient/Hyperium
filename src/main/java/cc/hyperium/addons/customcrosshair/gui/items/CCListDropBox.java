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

package cc.hyperium.addons.customcrosshair.gui.items;

import cc.hyperium.addons.customcrosshair.utils.CustomCrosshairGraphics;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CCListDropBox extends CCGuiItem {

    private boolean isOpen;
    private List<String> itemList;
    private String selectedItem;
    private int selectedItemIndex;

    public CCListDropBox(final GuiScreen screen) {
        super(screen);
        isOpen = false;
        itemList = new ArrayList<>();
        setWidth(100);
        setHeight(10);
        itemList.add("Item One");
        itemList.add("Item Two");
        itemList.add("Item Three");
        itemList.add("Item Four");
        itemList.add("Item Five");
    }

    @Override
    public void drawItem(final int mouseX, final int mouseY) {
        CustomCrosshairGraphics
            .drawBorderedRectangle(getPosX(), getPosY(), getPosX() + getWidth(), getPosY() + getHeight(), new Color(23, 107, 192, 128), new Color(240, 240, 240, 255));
        if (selectedItem != null && !selectedItem.equals("")) {
            CustomCrosshairGraphics
                .drawString(selectedItem, getPosX() + 2, getPosY(), 16777215);
        } else {
            CustomCrosshairGraphics
                .drawString("No item selected.", getPosX() + 2, getPosY() + 2, 16777215);
        }
        CustomCrosshairGraphics
            .drawBorderedRectangle(getPosX() + getWidth() - 10, getPosY(), getPosX() + getWidth(), getPosY() + getHeight(), new Color(23, 107, 192, 128), new Color(240, 240, 240, 255));
        final int listHeight = 1 + itemList.size() * 10;
        CustomCrosshairGraphics
            .drawBorderedRectangle(getPosX(), getPosY() + getHeight(), getPosX() + getWidth(), getPosY() + getHeight() + listHeight, new Color(23, 107, 192, 128), new Color(240, 240, 240, 255));
        for (int i = 0; i < itemList.size(); ++i) {
            CustomCrosshairGraphics
                .drawString(itemList.get(i), getPosX() + 2, getPosY() + getHeight() + i * 10 + 2, 16777215);
        }
    }
}

