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

package cc.hyperium.mods.chromahud.displayitems.hyperium;

import cc.hyperium.Hyperium;
import cc.hyperium.mods.chromahud.ElementRenderer;
import cc.hyperium.mods.chromahud.api.DisplayItem;
import cc.hyperium.utils.JsonHolder;
import cc.hyperium.config.Settings;

public class HypixelDisplay extends DisplayItem {
    public HypixelDisplay(JsonHolder data, int ordinal) {
        super(data, ordinal);
        this.height = 10;
    }

    @Override
    public void draw(int x, double y, boolean config) {
        String string = "";
        if(!Settings.CHROMAHUD_SQUAREBRACE_PREFIX_OPTION) {
            string = "Hypixel: " + Hyperium.INSTANCE.getHandlers().getHypixelDetector().isHypixel();
        } else {
            string = "[Hypixel] " + Hyperium.INSTANCE.getHandlers().getHypixelDetector().isHypixel();
        }
        ElementRenderer.draw(x, y, string);
        //TODO remove specific reference
        this.width = config ? ElementRenderer.getFontRenderer().getStringWidth(string) : 0;
    }
}
