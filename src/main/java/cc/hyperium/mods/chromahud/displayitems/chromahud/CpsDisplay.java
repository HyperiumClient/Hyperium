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

package cc.hyperium.mods.chromahud.displayitems.chromahud;

import cc.hyperium.mods.chromahud.ElementRenderer;
import cc.hyperium.mods.chromahud.api.Dimension;
import cc.hyperium.mods.chromahud.api.DisplayItem;
import cc.hyperium.utils.JsonHolder;
import net.minecraft.client.Minecraft;


/**
 * Created by mitchellkatz on 6/25/17.
 */
public class CpsDisplay extends DisplayItem {

    public CpsDisplay(JsonHolder data, int ordinal) {
        super(data, ordinal);
    }


    @Override
    public Dimension draw(int starX, double startY, boolean isConfig) {
        ElementRenderer.draw(starX, startY, "CPS: " + ElementRenderer.getCPS());
        if (isConfig)
            return new Dimension(Minecraft.getMinecraft().fontRendererObj.getStringWidth("CPS: " + ElementRenderer.getCPS()), 10);
        return new Dimension(0, 10);

    }


}
