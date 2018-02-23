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

package cc.hyperium.mods.chromahud.displayitems.hyperium.chromahud;

import cc.hyperium.mods.chromahud.ElementRenderer;
import cc.hyperium.mods.chromahud.api.Dimension;
import cc.hyperium.mods.chromahud.api.DisplayItem;
import cc.hyperium.utils.JsonHolder;
import net.minecraft.client.Minecraft;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mitchellkatz on 6/26/17.
 */
public class TimeHud extends DisplayItem {
    private String format;

    public TimeHud(JsonHolder data, int ordinal) {
        super(data, ordinal);
        this.format = data.optString("format");
        if (this.format.isEmpty()) {
            this.format = "HH:mm:ss";
        }

    }


    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        data.put("format", format);
        this.format = format;
    }

    @Override
    public Dimension draw(int starX, double startY, boolean isConfig) {
        try {
            String string = new SimpleDateFormat(format).format(new Date(System.currentTimeMillis()));
            ElementRenderer.draw(starX, startY, string);
            return new Dimension(isConfig ? Minecraft.getMinecraft().fontRendererObj.getStringWidth(string) : 0, 10);
        } catch (Exception e) {
            ElementRenderer.draw(starX, startY, "Invalid");
        }
        return new Dimension(0, 0);
    }

}
