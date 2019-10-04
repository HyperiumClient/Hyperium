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

package cc.hyperium.handlers.handlers.stats.display;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.Color;

public class DisplayLine extends StatsDisplayItem {
    private String value;
    private int color;
    private int scale;

    public DisplayLine(String value) {
        this(value, Color.WHITE.getRGB());
    }

    public DisplayLine(String value, int color) {
        this(value, color, 1);
    }

    public DisplayLine(String value, int color, int scale) {
        this.value = value;
        this.color = color;
        width = Minecraft.getMinecraft().fontRendererObj.getStringWidth(value) * scale;
        height = 10 * scale;
        this.scale = scale;
    }


    @Override
    public void draw(int x, int y) {
        GlStateManager.scale(scale, scale, scale);
        Minecraft.getMinecraft().fontRendererObj.drawString(value, x / scale, y / scale, color);
        GlStateManager.scale(1F / scale, 1F / scale, 1F / scale);
    }
}
