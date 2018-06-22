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

package cc.hyperium.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

import java.awt.Color;

public class HyperiumSlider extends GuiButton {
    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private double value = 0;

    public HyperiumSlider(int id, int x, int y, int width, int height) {
        super(id, x, y, width, height, "");
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        drawRect(x, y + 5, x + width, (y + height) - 5, new Color(0, 0, 0, 60).getRGB());
        drawRect((int) (x + (1 / width) * value), y, (int) (3 + x + (1 / width) * value), y + height, new Color(0, 0, 0, 80).getRGB());
    }

    @Override
    protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
        if (visible) {
            value = (float) (mouseX - (this.x + 4)) / (float) (this.width - 8);
            if (value < 0.0D)
                value = 0.0D;
            else if (value > 1.0D)
                value = 1.0D;
        }
    }
}
