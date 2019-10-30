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

package cc.hyperium.gui;

import net.minecraft.client.gui.FontRenderer;

import java.util.List;

/**
 * @author Sk1er
 */

public class GuiBlock {
    /*
            0       1


            2       3
         */
    private int left;
    private int right;
    private int top;
    private int bottom;
    private boolean expandRight;
    private boolean printRight;

    public GuiBlock(int left, int right, int top, int bottom) {
        expandRight = true;
        printRight = false;
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
    }

    @Override
    public String toString() {
        return "GuiBlock{left=" + left + ", right=" + right + ", top=" + top + ", bottom=" + bottom + '}';
    }

    public int getWidth() {
        return right - left;
    }

    public int getHeight() {
        return bottom - top;
    }

    public void ensureWidth(int width, boolean scaleRight) {
        if (getWidth() < width) {
            if (scaleRight) right = left + width;
            else left = right - width;
        }
    }

    public void ensureHeight(int height, boolean scaleBottom) {
        if (getHeight() < height) {
            if (scaleBottom) bottom = top + height;
            else top = bottom - height;
        }
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public boolean isMouseOver(int x, int y) {
        return x >= left && x <= right && y >= top && y <= bottom;
    }

    public int getBottom() {
        return bottom;
    }

    public void setBottom(int bottom) {
        this.bottom = bottom;
    }

    public boolean drawString(List<String> strings, FontRenderer fontRenderer, boolean shadow, boolean center, int xOffset, int yOffset,
                              boolean scaleToFitX, boolean scaleToFixY, int color, boolean sideLeft) {
        boolean suc = true;

        for (String string : strings) {
            suc = suc && drawString(string, fontRenderer, shadow, center, xOffset, yOffset, scaleToFitX, scaleToFixY, color, sideLeft);
        }

        return suc;
    }

    public void translate(int x, int y) {
        left += x;
        right += x;
        top += y;
        bottom += y;
    }

    public void scalePosition(float amount) {
        left *= amount;
        right *= amount;
        top *= amount;
        bottom *= amount;
    }

    public boolean drawString(String string, FontRenderer fontRenderer, boolean shadow, boolean center, int xOffset, int yOffset,
                              boolean scaleToFitX, boolean scaleToFixY, int color, boolean sideLeft) {
        int stringWidth = fontRenderer.getStringWidth(string);
        int x = sideLeft ? left + xOffset : right - stringWidth - xOffset;
        int y = top + yOffset;

        if (center) x -= stringWidth / 2;
        if (sideLeft) {
            if (x + stringWidth > right) {
                if (!scaleToFitX) return false;

                if (expandRight) {
                    right = x + stringWidth + xOffset;
                } else {
                    left = right - stringWidth - xOffset;
                    x = left;
                }
            }
        } else if (right - stringWidth < left) {
            if (!scaleToFitX) return false;
            if (expandRight) {
                right = x + stringWidth + xOffset;
                x = right;
            } else {
                left = right - stringWidth - xOffset;
            }
        }

        if (y + 10 > bottom) {
            if (!scaleToFixY) return false;
            bottom = y + 10;
        }

        if (y < top) {
            if (!scaleToFixY) return false;
            top = y;
        }

        fontRenderer.drawString(string, (float) x, (float) y, color, shadow);
        return true;
    }

    public boolean isExpandRight() {
        return expandRight;
    }

    public void setExpandRight(boolean expandRight) {
        this.expandRight = expandRight;
    }

    public boolean isPrintRight() {
        return printRight;
    }

    public void setPrintRight(boolean printRight) {
        this.printRight = printRight;
    }

    public GuiBlock multiply(double scale) {
        return new GuiBlock((int) (left * scale), (int) (right * scale), (int) (top * scale), (int) (bottom * scale));
    }
}
