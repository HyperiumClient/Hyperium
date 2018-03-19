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

package cc.hyperium.gui.settings;

import cc.hyperium.utils.HyperiumFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

import java.awt.*;
import java.util.function.Consumer;

public class SettingItem extends GuiButton {
    protected static final HyperiumFontRenderer fontRenderer =  new HyperiumFontRenderer("Arial", Font.PLAIN, 12);
    protected int hoverColor = new Color(0, 0, 0, 30).getRGB();
    protected Color color = new Color(0, 0, 0, 0);
    protected int textColor = new Color(255, 255, 255, 255).getRGB();
    protected int textHoverColor = new Color(255, 255, 255, 255).getRGB();
    public String displayString;
    public Consumer<SettingItem> callback;

    public SettingItem(int id, int x, int y, int width, String displayString, Consumer<SettingItem> callback) {
        super(id, x, y, width, 15, displayString);
        this.displayString = displayString;
        this.callback = callback;
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        return super.mousePressed(mc, mouseX, mouseY);
    }

    public void drawItem(Minecraft mc, int mouseX, int mouseY, int x, int y) {
        this.xPosition = x;
        this.yPosition = y;
        if (this.visible) {
            this.hovered = mouseX >= x && mouseY >= y && mouseX < this.xPosition + this.width && mouseY < y + this.height;
            this.mouseDragged(mc, mouseX, mouseY);

            if (this.hovered) {
                drawRect(x, y,
                        x + this.width, y + this.height,
                        hoverColor);
            } else {
                drawRect(x, y,
                        x + this.width, y + this.height,
                        color.getRGB());
            }
            int j = textColor;

            if (!this.enabled) {
                j = 10526880;
            } else if (this.hovered) {
                j = textHoverColor;
            }
            fontRenderer.drawString(this.displayString, x + 4, y + (this.height - 8) / 2, j);
            fontRenderer.drawString(">", x + width - 6, y + (this.height - 8) / 2, j);
        }

    }
}
