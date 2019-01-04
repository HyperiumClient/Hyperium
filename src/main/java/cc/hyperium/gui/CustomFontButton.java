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

import cc.hyperium.utils.HyperiumFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.Color;
import java.awt.Font;

public class CustomFontButton extends GuiButton {
    private final int hoverColor = new Color(0, 0, 0, 60).getRGB();
    private final int color = new Color(0, 0, 0, 50).getRGB();
    private final int textColor = new Color(255, 255, 255, 255).getRGB();
    private final int textHoverColor = new Color(255, 255, 255, 255).getRGB();
    private final HyperiumFontRenderer fontRenderer = new HyperiumFontRenderer("Arial", Font.PLAIN, 12);
    public boolean renderBackground = true;

    public CustomFontButton(int buttonId, int x, int y, String buttonText) {
        super(buttonId, x, y, buttonText);
    }

    public CustomFontButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
    }

    @SuppressWarnings("Duplicates")
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (!this.visible) {
            return;
        }

        mc.getTextureManager().bindTexture(buttonTextures);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

        this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
        this.mouseDragged(mc, mouseX, mouseY);

        if (renderBackground) {
            Gui.drawRect(
                this.xPosition,
                this.yPosition,
                this.xPosition + this.width,
                this.yPosition + this.height,
                this.hovered ? hoverColor : color
            );
        }

        float charlength = fontRenderer.getWidth(this.displayString);

        boolean enabled = true;
        fontRenderer.drawString(
            this.displayString,
            (this.xPosition + this.width / 2) - (charlength / 2),
            this.yPosition + (this.height - 8) / 2,
            enabled ? 10526880 : this.hovered ? textHoverColor : textColor
        );
    }

    @Override
    public void playPressSound(SoundHandler soundHandlerIn) {
        // Do nothing
    }
}
