/*
 * Hyperium Client, Free client with huds and popular mod
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

package cc.hyperium.mods.chromahud.displayitems.hyperium;

import cc.hyperium.Hyperium;
import cc.hyperium.mods.chromahud.ElementRenderer;
import cc.hyperium.mods.chromahud.api.Dimension;
import cc.hyperium.mods.chromahud.api.DisplayItem;
import cc.hyperium.utils.JsonHolder;
import net.minecraft.client.Minecraft;

import java.text.NumberFormat;
import java.util.Locale;

public class RatingDisplay extends DisplayItem {
    private static final NumberFormat format = NumberFormat.getNumberInstance(Locale.US);
    public RatingDisplay(JsonHolder data, int ordinal) {
        super(data, ordinal);
        this.height = 10;
    }

    @Override
    public void draw(int x, double y, boolean config) {
        String string = "Rating: " + format.format(Hyperium.INSTANCE.getHandlers().getValueHandler().getRankedRating());
        if (data.optBoolean("delta")) {
            string += " (" + Hyperium.INSTANCE.getHandlers().getValueHandler().getDeltaRankedRating() + ")";
        }

        ElementRenderer.draw(x, y, string);
        this.width = config ? Minecraft.getMinecraft().fontRendererObj.getStringWidth(string) : 0;
    }
}
