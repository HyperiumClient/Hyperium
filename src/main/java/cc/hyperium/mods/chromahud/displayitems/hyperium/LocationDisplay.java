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

import cc.hyperium.Hyperium;
import cc.hyperium.mods.chromahud.ElementRenderer;
import cc.hyperium.mods.chromahud.api.DisplayItem;
import cc.hyperium.utils.JsonHolder;
import net.minecraft.client.Minecraft;

/**
 * @author Sk1er
 */
public class LocationDisplay extends DisplayItem {

    public LocationDisplay(JsonHolder raw, int ordinal) {
        super(raw, ordinal);
        height = 10;
    }

    @Override
    public void draw(int starX, double startY, boolean config) {
        String string = "Location: " + Hyperium.INSTANCE.getHandlers().getLocationHandler().getLocation();
        ElementRenderer.draw(starX, startY, string);
        width = config ? Minecraft.getMinecraft().fontRendererObj.getStringWidth(string) : 0;
    }
}
