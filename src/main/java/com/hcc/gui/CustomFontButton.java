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

package com.hcc.gui;

import com.hcc.utils.HCCFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;

public class CustomFontButton extends GuiButton {
    private int hoverColor = new Color(0, 0, 0, 60).getRGB();
    private int color = new Color(0, 0, 0, 50).getRGB();
    private int textColor = new Color(255, 255, 255, 255).getRGB();
    private int textHoverColor = new Color(255, 255, 255, 255).getRGB();
    private HCCFontRenderer fontRenderer = new HCCFontRenderer("Arial", Font.PLAIN, 12);
    private boolean enabled = true;

    public CustomFontButton(int buttonId, int x, int y, String buttonText) {
        super(buttonId, x, y, buttonText);
    }

    public CustomFontButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
    }

    @SuppressWarnings("Duplicates")
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (this.visible) {
            mc.getTextureManager().bindTexture(buttonTextures);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            this.mouseDragged(mc, mouseX, mouseY);

            if (this.hovered) {
                Gui.drawRect(this.xPosition, this.yPosition,
                        this.xPosition + this.width, this.yPosition + this.height,
                        hoverColor);
            } else {
                Gui.drawRect(this.xPosition, this.yPosition,
                        this.xPosition + this.width, this.yPosition + this.height,
                        color);
            }
            int j = textColor;

            if (!this.enabled) {
                j = 10526880;
            } else if (this.hovered) {
                j = textHoverColor;
            }
            float charlength = fontRenderer.getWidth(this.displayString);
            fontRenderer.drawString(this.displayString, (this.xPosition + this.width / 2) - (charlength / 2), this.yPosition + (this.height - 8) / 2, j);
        }
    }

}
