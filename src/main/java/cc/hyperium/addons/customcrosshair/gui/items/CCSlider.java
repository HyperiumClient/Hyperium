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
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;

public class CCSlider extends CCGuiItem {
    private double minValue;
    private double maxValue;
    private double value;
    private double boxPosition;
    private boolean mouseDown;
    private double offset;
    private int boxWidth;
    private Color boxColour;

    private Runnable callback;

        /*"I'd like to thank boomboompower for fixing this class for me. " +
        "Go sub to him: https://www.youtube.com/channel/UC63_abmxSPyoGneeDPsVpmQ " +
        "His twitter: https://twitter.com/xBOOMBOOMPOWERx";*/

    public CCSlider(final GuiScreen screen) {
        this(screen, -1, "no name", 0, 0, 100, 10, 0, 100);
    }

    public CCSlider(final GuiScreen screen, final int id, final String displayText, final int posX, final int posY, final int width, final int height, final int minValue, final int maxValue) {
        super(screen, id, displayText, posX, posY, width, height);
        setMinMaxValue(minValue, maxValue);
        boxWidth = 15;
        boxPosition = 1;
        offset = 0;
        mouseDown = false;
        value = 0;
        boxColour = CustomCrosshairAddon.PRIMARY_T;
    }

    @Override
    public void mouseClicked(final int mouseX, final int mouseY) {
        if (isMouseOverBox(mouseX, mouseY)) {
            mouseDown = true;
            offset = mouseX - (getPosX() + boxPosition);
        }
    }

    @Override
    public void mouseReleased(final int mouseX, final int mouseY) {
        mouseDown = false;
        setValue(value);
    }

    @Override
    public void drawItem(final int mouseX, final int mouseY) {
        CustomCrosshairGraphics
            .drawThemeBorderedRectangle(getPosX(), getPosY(), getPosX() + getWidth(), getPosY() + getHeight());
        Color borderColour = new Color(255, 255, 255, 255);
        if (isMouseOverBox(mouseX, mouseY) || mouseDown) {
            borderColour = new Color(255, 180, 0, 255);
        }
        double x = getPosX() + boxPosition;

        if (mouseDown) {
            x = mouseX - offset;
            if (x < getPosX() + 1) {
                x = getPosX() + 1;
            }
            if (x > getPosX() + getWidth() - boxWidth - 1) {
                x = getPosX() + getWidth() - boxWidth - 1;
            }
            setCurrentPosition(x - getPosX());
        }
        CustomCrosshairGraphics
            .drawBorderedRectangle((int) x, getPosY() + 1, (int) x + boxWidth, getPosY() + getHeight() - 1, boxColour, borderColour);
        CustomCrosshairGraphics
            .drawString(getDisplayText() + ": " + getValue(), getPosX() + getWidth() + 3, getPosY() + getHeight() / 2 - 3, 16777215);
    }

    public CCSlider setCallback(Runnable callback) {
        this.callback = callback;

        return this;
    }

    private boolean isMouseOverBox(final int mouseX, final int mouseY) {
        return mouseX >= getPosX() + boxPosition && mouseX <= getPosX() + boxPosition + boxWidth && mouseY >= getPosY() + 1 && mouseY <= getPosY() + getHeight() - 1;
    }

    private void setCurrentPosition(final double x) {
        boxPosition = x;
        value = getMinValue() + (boxPosition - 1) / (getWidth() - boxWidth - 2) * (maxValue - minValue);

        if (callback != null) {
            callback.run();
        }
    }

    public void setValue(final double newValue) {
        value = newValue;
        boxPosition = (getWidth() - boxWidth - 2) * (getValue() - minValue) / (maxValue - minValue) + 1;

        if (callback != null) {
            callback.run();
        }
    }

    public void setMinMaxValue(final double min, final double max) {
        minValue = min;
        maxValue = max;
    }

    public int getMinValue() {
        return (int) minValue;
    }

    public int getMaxValue() {
        return (int) maxValue;
    }

    public int getValue() {
        return (int) value;
    }

    public Color getBoxColour() {
        return boxColour;
    }

    public void setBoxColour(final Color colour) {
        boxColour = colour;
    }
}
