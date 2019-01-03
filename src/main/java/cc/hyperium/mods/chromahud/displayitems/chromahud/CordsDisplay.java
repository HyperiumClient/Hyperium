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

package cc.hyperium.mods.chromahud.displayitems.chromahud;

import cc.hyperium.config.Settings;
import cc.hyperium.mods.chromahud.ElementRenderer;
import cc.hyperium.mods.chromahud.api.DisplayItem;
import cc.hyperium.utils.JsonHolder;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mitchell Katz on 5/25/2017.
 */
public class CordsDisplay extends DisplayItem {

    public int state = 0;
    public int precision = 1;
    private JsonObject raw;


    public CordsDisplay(JsonHolder options, int orderinal) {
        super(options, orderinal);
        state = options.optInt("state");
        this.precision = options.optInt("precision");
    }

    @Override
    public void save() {
        data.put("state", state);
        data.put("precision", precision);
    }

    @Override
    public String toString() {
        return "CordsDisplay{" +
            "state=" + state +
            '}';
    }

    @Override
    public void draw(int x, double y, boolean isConfig) {
        List<String> tmp = new ArrayList<>();
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        if (player != null) {
            StringBuilder start = new StringBuilder("0");
            if (precision > 0)
                start.append(".");
            for (int i = 0; i < precision; i++) {
                start.append("0");
            }
            DecimalFormat df = new DecimalFormat(start.toString());

            if (state == 0) {
                if (!Settings.CHROMAHUD_SQUAREBRACE_PREFIX_OPTION) {
                    tmp.add("X: " + df.format(player.posX) +
                        " Y: " + df.format(player.posY) +
                        " Z: " + df.format(player.posZ));
                } else {
                    tmp.add("[X] " + df.format(player.posX) +
                        " [Y] " + df.format(player.posY) +
                        " [Z] " + df.format(player.posZ));
                }
            } else if (state == 1) {
                if (!Settings.CHROMAHUD_SQUAREBRACE_PREFIX_OPTION) {
                    tmp.add("[X] " + df.format(player.posX));
                    tmp.add("[Y] " + df.format(player.posY));
                    tmp.add("[Z] " + df.format(player.posZ));
                }
            } else tmp.add("Illegal state of cords unit (" + state + ")");
        } else {
            if (!Settings.CHROMAHUD_SQUAREBRACE_PREFIX_OPTION) tmp.add("X: null, Y: null, Z: null");
            else tmp.add("[X] null, [Y] null, [Z] null");
        }
        ElementRenderer.draw(x, y, tmp);
        this.width = isConfig ? ElementRenderer.maxWidth(tmp) : 0;
        this.height = tmp.size() * 10;

    }
}
