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

package cc.hyperium.mods.chromahud.displayitems.hyperium;

import cc.hyperium.mods.chromahud.ElementRenderer;
import cc.hyperium.mods.chromahud.api.DisplayItem;
import cc.hyperium.utils.JsonHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

public class MemoryDisplay extends DisplayItem {

    public MemoryDisplay(JsonHolder data, int ordinal) {
        super(data, ordinal);
    }

    @Override
    public void draw(int x, double y, boolean config) {
        int mbDiv = 1048576;
        long maxMemory = Runtime.getRuntime().maxMemory() / mbDiv;
        long totalMemory = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / mbDiv;

        String displayString = totalMemory + " / " + (maxMemory == Long.MAX_VALUE ? "No limit" : maxMemory) + "MB";
        ElementRenderer.draw(x, y, displayString);
        FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;

        height = fr.FONT_HEIGHT * ElementRenderer.getCurrentScale();
        width = fr.getStringWidth(displayString) * ElementRenderer.getCurrentScale();
    }
}
