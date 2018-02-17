/*
 *     Hypixel Community Client, Client optimized for Hypixel Network
 *     Copyright (C) 2018  HCC Dev Team
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

package com.hcc.mods.chromahud.displayitems.hcc;

import com.hcc.HCC;
import com.hcc.mods.chromahud.ElementRenderer;
import com.hcc.mods.chromahud.api.Dimension;
import com.hcc.mods.chromahud.api.DisplayItem;
import com.hcc.utils.JsonHolder;
import net.minecraft.client.Minecraft;


/**
 * Created by mitchellkatz on 5/30/17.
 */
public class LocationDisplay extends DisplayItem {

    public LocationDisplay(JsonHolder raw, int ordinal) {
        super(raw, ordinal);
    }


    @Override
    public Dimension draw(int starX, double startY, boolean config) {
        String string = "Location: " + HCC.INSTANCE.getHandlers().getLocationHandler().getLocation();
        ElementRenderer.draw(starX, startY, string);
        return new Dimension(config ? Minecraft.getMinecraft().fontRendererObj.getStringWidth(string) : 0, 10);
    }


}
