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

package cc.hyperium.addons.customcrosshair.gui.items;

import cc.hyperium.Hyperium;
import cc.hyperium.addons.customcrosshair.gui.GuiCustomCrosshairEditColour;
import cc.hyperium.addons.customcrosshair.CustomCrosshairAddon;
import cc.hyperium.addons.customcrosshair.utils.CustomCrosshairGraphics;

import java.awt.Color;

import net.minecraft.client.gui.GuiScreen;

public class CCEditColourButton extends CCGuiItem {

    private Color editColour;
    private CCButton editButton;

    private CustomCrosshairAddon addon;

    public CCEditColourButton(CustomCrosshairAddon addon, final GuiScreen screen, final Color colour) {
        this(addon, screen, -1, "noname", 0, 0, 0, 0, colour);
        this.editColour = colour;
    }

    public CCEditColourButton(CustomCrosshairAddon addon, final GuiScreen screen, final int id, final String text, final int x, final int y, final int width, final int height, final Color colour) {
        super(screen, id, text, x, y, width, height);
        this.editColour = colour;

        this.addon = addon;
    }

    @Override
    public void drawItem(final int mouseX, final int mouseY) {
        CustomCrosshairGraphics
            .drawBorderedRectangle(this.getPosX(), this.getPosY(), this.getPosX() + this.getHeight(), this.getPosY() + this.getHeight(), this.editColour, new Color(255, 255, 255, 255));
        this.editButton.drawItem(mouseX, mouseY);
        CustomCrosshairGraphics
            .drawString(this.getDisplayText(), this.getPosX() + this.getHeight() + this.editButton.getWidth() + 4, this.getPosY() + this.getHeight() / 2 - 3, 16777215);
    }

    @Override
    public void setPosition(final int x, final int y) {
        super.setPosition(x, y);
        this.editButton = new CCButton(this.getCurrentScreen(), 0, "Edit", this.getPosX() + this.getHeight() + 2, this.getPosY() + this.getHeight() / 2 - 6, 25, 13);
    }

    @Override
    public void mouseClicked(final int mouseX, final int mouseY) {
        if (mouseX >= this.editButton.getPosX() && mouseX <= this.editButton.getPosX() + this.editButton.getWidth() && mouseY >= this.editButton.getPosY() && mouseY <= this.editButton.getPosY() + this.editButton.getHeight()) {
            Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new GuiCustomCrosshairEditColour(this.addon, this.editColour, "Edit " + this.getDisplayText() + "..."));
        }
    }
}

