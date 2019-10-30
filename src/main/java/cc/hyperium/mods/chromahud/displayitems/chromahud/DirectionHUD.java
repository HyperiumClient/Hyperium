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

package cc.hyperium.mods.chromahud.displayitems.chromahud;

import cc.hyperium.mods.chromahud.ElementRenderer;
import cc.hyperium.mods.chromahud.api.DisplayItem;
import cc.hyperium.utils.JsonHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mitchell Katz on 6/1/2017.
 */
public class DirectionHUD extends DisplayItem {
    private static final String[] dir = {"South", "South West", "West", "North West", "North", "North East", "East", "South East"};
    private static final String[] dirShort = {"S", "SW", "W", "NW", "N", "NE", "E", "SE"};

    private boolean shortDirection;

    public DirectionHUD(JsonHolder raw, int ordinal) {
        super(raw, ordinal);
        height = 10;
        shortDirection = raw.optBoolean("shortDirection");
    }

    public void draw(int x, double y, boolean isConfig) {
        List<String> list = new ArrayList<>();
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        if (player != null) {
            int d = (int) player.rotationYaw;
            if (d < 0) d += 360;
            d += 22;
            int direction = (d % 360);
            direction = direction / (45);

            try {
                while (direction < 0) {
                    direction += 8;
                }

                list.add(!shortDirection ? dir[direction] : dirShort[direction]);
            } catch (Exception ignored) {}
        }

        ElementRenderer.draw(x, y, list);
        width = isConfig ? ElementRenderer.maxWidth(list) : 0;
    }

    public void toggleShortDirection() {
        shortDirection = !shortDirection;
    }

    @Override
    public void save() {
        data.put("shortDirection", shortDirection);
    }

    @Override
    public String toString() {
        return "DirectionHUD{" +
            "shortDirection=" + shortDirection +
            '}';
    }
}
