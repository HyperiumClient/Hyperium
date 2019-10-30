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
        contentHeight = cHeight;
        boxPosition = 0;
        boxHeight = 30;
        minValue = 0;
        maxValue = Math.abs(contentHeight - getHeight());
    }

    @Override
    public void drawItem(final int mouseX, final int mouseY) {
        if (contentHeight <= getHeight()) {
            return;
        }
        int y;
        if (mouseDown) {
            y = mouseY - offset;
            if (y < getPosY()) {
                y = getPosY();
            }
            if (y > getPosY() + getHeight() - boxHeight) {
                y = getPosY() + getHeight() - boxHeight;
            }
            setCurrentPosition(y - getPosY());
        }
        CustomCrosshairGraphics
            .drawThemeBorderedRectangle(getPosX(), getPosY(), getPosX() + getWidth(), getPosY() + getHeight());
        CustomCrosshairGraphics
            .drawThemeBorderedRectangle(getPosX(), getPosY() + boxPosition, getPosX() + getWidth(), getPosY() + boxPosition + boxHeight);
    }

    @Override
    public void mouseClicked(final int mouseX, final int mouseY) {
        if (contentHeight <= getHeight()) {
            return;
        }
        if (isMouseOverBox(mouseX, mouseY)) {
            mouseDown = true;
            offset = mouseY - (getPosY() + boxPosition);
        }
    }

    @Override
    public void mouseReleased(final int mouseX, final int mouseY) {
        mouseDown = false;
    }

    private boolean isMouseOverBox(final int mouseX, final int mouseY) {
        return mouseX >= getPosX() && mouseX <= getPosX() + getWidth() && mouseY >= getPosY() + boxPosition && mouseY <= getPosY() + boxPosition + boxHeight;
    }

    private void setCurrentPosition(final int x) {
        if (contentHeight <= getHeight()) {
            return;
        }
        boxPosition = x;
        value = minValue + boxPosition / (getHeight() - boxHeight) * (maxValue - minValue);
    }

    public void setValue(final int newValue) {
        if (contentHeight <= getHeight()) {
            return;
        }
        value = newValue;
        if (value < minValue) {
            value = minValue;
        }
        if (value > maxValue) {
            value = maxValue;
        }
        boxPosition = (getHeight() - boxHeight) * (value - minValue) / (maxValue - minValue);
    }

    public int getValue() {
        return value;
    }

    public int getMinValue() {
        return minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public boolean isMouseDown() {
        return mouseDown;
    }
}

