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

import cc.hyperium.Hyperium;
import cc.hyperium.addons.customcrosshair.CustomCrosshairAddon;
import cc.hyperium.addons.customcrosshair.gui.GuiCustomCrosshairEditColour;
import cc.hyperium.addons.customcrosshair.utils.CustomCrosshairGraphics;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;

public class CCEditColourButton extends CCGuiItem {

    private Color editColour;
    private CCButton editButton;

    private CustomCrosshairAddon addon;

    public CCEditColourButton(CustomCrosshairAddon addon, final GuiScreen screen, final Color colour) {
        this(addon, screen, -1, "noname", 0, 0, 0, 0, colour);
        editColour = colour;
    }

    public CCEditColourButton(CustomCrosshairAddon addon, final GuiScreen screen, final int id, final String text, final int x, final int y, final int width, final int height, final Color colour) {
        super(screen, id, text, x, y, width, height);
        editColour = colour;

        this.addon = addon;
    }

    @Override
    public void drawItem(final int mouseX, final int mouseY) {
        CustomCrosshairGraphics
            .drawBorderedRectangle(getPosX(), getPosY(), getPosX() + getHeight(), getPosY() + getHeight(), editColour, new Color(255, 255, 255, 255));
        editButton.drawItem(mouseX, mouseY);
        CustomCrosshairGraphics
            .drawString(getDisplayText(), getPosX() + getHeight() + editButton.getWidth() + 4, getPosY() + getHeight() / 2 - 3, 16777215);
    }

    @Override
    public void setPosition(final int x, final int y) {
        super.setPosition(x, y);
        editButton = new CCButton(getCurrentScreen(), 0, "Edit", getPosX() + getHeight() + 2, getPosY() + getHeight() / 2 - 6, 25, 13);
    }

    @Override
    public void mouseClicked(final int mouseX, final int mouseY) {
        if (mouseX >= editButton.getPosX() && mouseX <= editButton.getPosX() + editButton.getWidth() && mouseY >= editButton.getPosY() && mouseY <= editButton.getPosY() + editButton.getHeight()) {
            Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new GuiCustomCrosshairEditColour(addon, editColour, "Edit " + getDisplayText() + "..."));
        }
    }
}

