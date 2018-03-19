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

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.opengl.GL11;

import java.awt.*;


public class SettingSlider extends GuiButton {

    // Slider value
    public float sliderValue;

    public boolean dragging = false;

    /**
     * Sets up a slider.
     */
    public SettingSlider(int buttonID, int x, int y, String label, float initialValue) {
        super(buttonID, x, y, 120, 20, label);
        sliderValue = initialValue;
    }

    /**
     * Fired when the mouse button is dragged. Equivalent of
     * MouseListener.mouseDragged(MouseEvent e).
     */
    @Override
    protected void mouseDragged(Minecraft mc, int x, int y) {

        if (dragging) {
            sliderValue = limitToRange((float) (x - (xPosition + 4)) / (float) (width - 8));
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        drawRect(xPosition + (int) (sliderValue * (float) (width - 8)), yPosition, xPosition + (int) (sliderValue * (float) (width - 8)) +4, yPosition + 20, 0xFFFFFF);
        drawRect(xPosition + (int) (sliderValue * (float) (width - 8)), yPosition, xPosition + (int) (sliderValue * (float) (width - 8)) +4, yPosition + 20, new Color(25, 0, 0).getRGB());
    }

    /**
     * Returns true if the mouse has been pressed on this control. Equivalent of
     * MouseListener.mousePressed(MouseEvent e).
     */
    @Override
    public boolean mousePressed(Minecraft mc, int x, int y) {
        if (super.mousePressed(mc, x, y)) {
            sliderValue = limitToRange((float) (x - (xPosition + 4)) / (float) (width - 8));

            dragging = true;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Fired when the mouse button is released. Equivalent of
     * MouseListener.mouseReleased(MouseEvent e).
     */
    @Override
    public void mouseReleased(int par1, int par2) {
        dragging = false;
    }

    /**
     * Returns 0 if the button is disabled, 1 if the mouse is NOT hovering over
     * this button and 2 if it IS hovering over this button.
     */
    @Override
    protected int getHoverState(boolean par1) {
        return 0;
    }

    private float limitToRange (float value) {
        if (value < 0) {
            return 0;
        } else if (value > 1) {
            return 1;
        } else {
            return value;
        }
    }
}