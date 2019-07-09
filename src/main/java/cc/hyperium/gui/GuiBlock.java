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
        this.expandRight = true;
        this.printRight = false;
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
    }

    @Override
    public String toString() {
        return "GuiBlock{left=" + this.left + ", right=" + this.right + ", top=" + this.top + ", bottom=" + this.bottom + '}';
    }

    public int getWidth() {
        return this.right - this.left;
    }

    public int getHeight() {
        return this.bottom - this.top;
    }

    public void ensureWidth(int width, boolean scaleRight) {
        if (this.getWidth() < width) {
            if (scaleRight) {
                this.right = this.left + width;
            } else {
                this.left = this.right - width;
            }
        }
    }

    public void ensureHeight(int height, boolean scaleBottom) {
        if (this.getHeight() < height) {
            if (scaleBottom) {
                this.bottom = this.top + height;
            } else {
                this.top = this.bottom - height;
            }
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
        return x >= this.left && x <= this.right && y >= this.top && y <= this.bottom;
    }

    public int getBottom() {
        return bottom;
    }

    public void setBottom(int bottom) {
        this.bottom = bottom;
    }

    public boolean drawString(List<String> strings, FontRenderer fontRenderer, boolean shadow, boolean center, int xOffset, int yOffset, boolean scaleToFitX, boolean scaleToFixY, int color, boolean sideLeft) {
        boolean suc = true;
        for (String string : strings) {
            suc = suc && drawString(string, fontRenderer, shadow, center, xOffset, yOffset, scaleToFitX, scaleToFixY, color, sideLeft);
        }
        return suc;
    }

    public void translate(int x, int y) {
        this.left += x;
        this.right += x;
        this.top += y;
        this.bottom += y;
    }

    public void scalePosition(float amount) {
        this.left *= amount;
        this.right *= amount;
        this.top *= amount;
        this.bottom *= amount;
    }

    public boolean drawString(String string, FontRenderer fontRenderer, boolean shadow, boolean center, int xOffset, int yOffset, boolean scaleToFitX, boolean scaleToFixY, int color, boolean sideLeft) {
        int stringWidth = fontRenderer.getStringWidth(string);
        int x;

        if (sideLeft) {
            x = this.left + xOffset;
        } else {
            x = this.right - stringWidth - xOffset;
        }

        int y = this.top + yOffset;

        if (center) {
            x -= stringWidth / 2;
        }

        if (sideLeft) {
            if (x + stringWidth > this.right) {
                if (!scaleToFitX) {
                    return false;
                }

                if (this.expandRight) {
                    this.right = x + stringWidth + xOffset;
                } else {
                    this.left = this.right - stringWidth - xOffset;
                    x = this.left;
                }
            }
        } else if (this.right - stringWidth < this.left) {
            if (!scaleToFitX) {
                return false;
            }

            if (this.expandRight) {
                this.right = x + stringWidth + xOffset;
                x = this.right;
            } else {
                this.left = this.right - stringWidth - xOffset;
            }
        }

        if (y + 10 > this.bottom) {
            if (!scaleToFixY) {
                return false;
            }
            this.bottom = y + 10;
        }

        if (y < this.top) {
            if (!scaleToFixY) {
                return false;
            }
            this.top = y;
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
