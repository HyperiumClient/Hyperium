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

package com.hcc.gui.settings;

import com.hcc.utils.HCCFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

import java.awt.*;
import java.util.function.Consumer;

public class SettingItem extends GuiButton {
    private static final HCCFontRenderer fontRenderer =  new HCCFontRenderer("Arial", Font.PLAIN, 12);
    private int hoverColor = new Color(0, 0, 0, 30).getRGB();
    private int color = new Color(0, 0, 0, 0).getRGB();
    private int textColor = new Color(255, 255, 255, 255).getRGB();
    private int textHoverColor = new Color(255, 255, 255, 255).getRGB();
    private String displayString;
    private Consumer<Integer> callback;

    public SettingItem(int id, int x, int y, int width, String displayString, Consumer<Integer> callback) {
        super(id, x, y, width, 15, displayString);
        this.displayString = displayString;
        this.callback = callback;
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        boolean pressed = super.mousePressed(mc, mouseX, mouseY);
        if(pressed)
            callback.accept(id);
        return pressed;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (this.visible) {
            this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            this.mouseDragged(mc, mouseX, mouseY);

            // TODO RECT COLORS
            if (this.hovered) {
                drawRect(this.xPosition, this.yPosition,
                        this.xPosition + this.width, this.yPosition + this.height,
                        hoverColor);
            } else {
                drawRect(this.xPosition, this.yPosition,
                        this.xPosition + this.width, this.yPosition + this.height,
                        color);
            }
            int j = textColor;

            if (!this.enabled) {
                j = 10526880;
            } else if (this.hovered) {
                j = textHoverColor;
            }
            fontRenderer.drawString(this.displayString, this.xPosition + 4, this.yPosition + (this.height - 8) / 2, j);
            fontRenderer.drawString(">", this.xPosition + width - 2, this.yPosition + (this.height - 8) / 2, j);
        }

    }
}
