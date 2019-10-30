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

package cc.hyperium.addons.customcrosshair.gui.items;

import cc.hyperium.addons.customcrosshair.CustomCrosshairAddon;
import cc.hyperium.addons.customcrosshair.utils.CustomCrosshairGraphics;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;

public class CCButton extends CCGuiItem {

    public CCButton(final GuiScreen screen, final int id, final String text, final int x,
                    final int y, final int width, final int height) {
        super(screen, id, text, x, y, width, height);
    }

    @Override
    public void drawItem(final int mouseX, final int mouseY) {
        Color backgroundColour = CustomCrosshairAddon.PRIMARY_T;
        if (mouseX >= getPosX() && mouseX <= getPosX() + getWidth() && mouseY >= getPosY() && mouseY <= getPosY() + getHeight()) {
            backgroundColour = new Color(255, 180, 0, 255);
        }
        CustomCrosshairGraphics
            .drawBorderedRectangle(getPosX(), getPosY(), getPosX() + getWidth(), getPosY() + getHeight(), backgroundColour, CustomCrosshairAddon.SECONDARY);
        CustomCrosshairGraphics
            .drawString(getDisplayText(), getPosX() + getWidth() / 2 - Minecraft.getMinecraft().fontRendererObj.getStringWidth(getDisplayText()) / 2 + 1, getPosY() + getHeight() / 2 - 3, 16777215);
    }
}
