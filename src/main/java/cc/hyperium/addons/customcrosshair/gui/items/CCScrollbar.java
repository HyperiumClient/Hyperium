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

import cc.hyperium.addons.customcrosshair.utils.CustomCrosshairGraphics;
import net.minecraft.client.gui.GuiScreen;

public class CCScrollbar extends CCGuiItem {

    private int contentHeight;
    private boolean mouseDown;
    private int boxPosition;
    private int boxHeight;
    private int offset;
    private int value;
    private int minValue;
    private int maxValue;

    public CCScrollbar(final GuiScreen screen, final int id, final int x, final int y, final int width, final int height, final int cHeight) {
        super(screen, id, "", x, y, width, height);
        this.contentHeight = cHeight;
        this.boxPosition = 0;
        this.boxHeight = 30;
        this.minValue = 0;
        this.maxValue = Math.abs(this.contentHeight - this.getHeight());
    }

    @Override
    public void drawItem(final int mouseX, final int mouseY) {
        if (this.contentHeight <= this.getHeight()) {
            return;
        }
        int y = this.getPosY() + this.boxPosition;
        if (this.mouseDown) {
            y = mouseY - this.offset;
            if (y < this.getPosY()) {
                y = this.getPosY();
            }
            if (y > this.getPosY() + this.getHeight() - this.boxHeight) {
                y = this.getPosY() + this.getHeight() - this.boxHeight;
            }
            this.setCurrentPosition(y - this.getPosY());
        }
        CustomCrosshairGraphics
            .drawThemeBorderedRectangle(this.getPosX(), this.getPosY(), this.getPosX() + this.getWidth(), this.getPosY() + this.getHeight());
        CustomCrosshairGraphics
            .drawThemeBorderedRectangle(this.getPosX(), this.getPosY() + this.boxPosition, this.getPosX() + this.getWidth(), this.getPosY() + this.boxPosition + this.boxHeight);
    }

    @Override
    public void mouseClicked(final int mouseX, final int mouseY) {
        if (this.contentHeight <= this.getHeight()) {
            return;
        }
        if (this.isMouseOverBox(mouseX, mouseY)) {
            this.mouseDown = true;
            this.offset = mouseY - (this.getPosY() + this.boxPosition);
        }
    }

    @Override
    public void mouseReleased(final int mouseX, final int mouseY) {
        this.mouseDown = false;
    }

    private boolean isMouseOverBox(final int mouseX, final int mouseY) {
        return mouseX >= this.getPosX() && mouseX <= this.getPosX() + this.getWidth() && mouseY >= this.getPosY() + this.boxPosition && mouseY <= this.getPosY() + this.boxPosition + this.boxHeight;
    }

    private void setCurrentPosition(final int x) {
        if (this.contentHeight <= this.getHeight()) {
            return;
        }
        this.boxPosition = x;
        this.value = this.minValue + this.boxPosition / (this.getHeight() - this.boxHeight) * (this.maxValue - this.minValue);
    }

    public void setValue(final int newValue) {
        if (this.contentHeight <= this.getHeight()) {
            return;
        }
        this.value = newValue;
        if (this.value < this.minValue) {
            this.value = this.minValue;
        }
        if (this.value > this.maxValue) {
            this.value = this.maxValue;
        }
        this.boxPosition = (this.getHeight() - this.boxHeight) * (this.value - this.minValue) / (this.maxValue - this.minValue);
    }

    public int getValue() {
        return this.value;
    }

    public int getMinValue() {
        return this.minValue;
    }

    public int getMaxValue() {
        return this.maxValue;
    }

    public boolean isMouseDown() {
        return this.mouseDown;
    }
}

