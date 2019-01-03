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

import cc.hyperium.addons.customcrosshair.CustomCrosshairAddon;
import cc.hyperium.addons.customcrosshair.utils.CustomCrosshairGraphics;

import java.awt.Color;

import net.minecraft.client.gui.GuiScreen;

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

    public static final String README = "I'd like to thank boomboompower for fixing this class for me. " +
                                        "Go sub to him: https://www.youtube.com/channel/UC63_abmxSPyoGneeDPsVpmQ " +
                                        "His twitter: https://twitter.com/xBOOMBOOMPOWERx";

    public CCSlider(final GuiScreen screen) {
        this(screen, -1, "no name", 0, 0, 100, 10, 0, 100);
    }

    public CCSlider(final GuiScreen screen, final int id, final String displayText, final int posX, final int posY, final int width, final int height, final int minValue, final int maxValue) {
        super(screen, id, displayText, posX, posY, width, height);
        this.setMinMaxValue(minValue, maxValue);
        this.boxWidth = 15;
        this.boxPosition = 1;
        this.offset = 0;
        this.mouseDown = false;
        this.value = 0;
        this.boxColour = CustomCrosshairAddon.PRIMARY_T;
    }

    @Override
    public void mouseClicked(final int mouseX, final int mouseY) {
        if (this.isMouseOverBox(mouseX, mouseY)) {
            this.mouseDown = true;
            this.offset = mouseX - (this.getPosX() + this.boxPosition);
        }
    }

    @Override
    public void mouseReleased(final int mouseX, final int mouseY) {
        this.mouseDown = false;
        this.setValue(this.value);
    }

    @Override
    public void drawItem(final int mouseX, final int mouseY) {
        CustomCrosshairGraphics
            .drawThemeBorderedRectangle(this.getPosX(), this.getPosY(), this.getPosX() + this.getWidth(), this.getPosY() + this.getHeight());
        Color borderColour = new Color(255, 255, 255, 255);
        if (this.isMouseOverBox(mouseX, mouseY) || this.mouseDown) {
            borderColour = new Color(255, 180, 0, 255);
        }
        double x = this.getPosX() + this.boxPosition;

        if (this.mouseDown) {
            x = mouseX - this.offset;
            if (x < this.getPosX() + 1) {
                x = this.getPosX() + 1;
            }
            if (x > this.getPosX() + this.getWidth() - this.boxWidth - 1) {
                x = this.getPosX() + this.getWidth() - this.boxWidth - 1;
            }
            this.setCurrentPosition(x - this.getPosX());
        }
        CustomCrosshairGraphics
            .drawBorderedRectangle((int) x, this.getPosY() + 1, (int) x + this.boxWidth, this.getPosY() + this.getHeight() - 1, this.getBoxColour(), borderColour);
        CustomCrosshairGraphics
            .drawString(this.getDisplayText() + ": " + this.getValue(), this.getPosX() + this.getWidth() + 3, this.getPosY() + this.getHeight() / 2 - 3, 16777215);
    }

    public CCSlider setCallback(Runnable callback) {
        this.callback = callback;

        return this;
    }

    private boolean isMouseOverBox(final int mouseX, final int mouseY) {
        return mouseX >= this.getPosX() + this.boxPosition && mouseX <= this.getPosX() + this.boxPosition + this.boxWidth && mouseY >= this.getPosY() + 1 && mouseY <= this.getPosY() + this.getHeight() - 1;
    }

    private void setCurrentPosition(final double x) {
        this.boxPosition = x;
        this.value = this.getMinValue() + (this.boxPosition - 1) / (this.getWidth() - this.boxWidth - 2) * (this.maxValue - this.minValue);

        if (this.callback != null) {
            this.callback.run();
        }
    }

    public void setValue(final double newValue) {
        this.value = newValue;
        this.boxPosition = (this.getWidth() - this.boxWidth - 2) * (this.getValue() - this.minValue) / (this.maxValue - this.minValue) + 1;

        if (this.callback != null) {
            this.callback.run();
        }
    }

    public void setMinMaxValue(final double min, final double max) {
        this.minValue = min;
        this.maxValue = max;
    }

    public int getMinValue() {
        return (int) this.minValue;
    }

    public int getMaxValue() {
        return (int) this.maxValue;
    }

    public int getValue() {
        return (int) this.value;
    }

    public Color getBoxColour() {
        return this.boxColour;
    }

    public void setBoxColour(final Color colour) {
        this.boxColour = colour;
    }
}
