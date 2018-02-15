package com.hcc.gui;

import net.minecraft.client.gui.FontRenderer;

import java.util.List;

/**
 * Created by mitchellkatz on 2/15/18. Designed for production use on Sk1er.club
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

    public GuiBlock(int left, int right, int top, int bottom) {
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
    }

    public int getWidth() {
        return right - left;
    }

    public int getHeight() {
        return bottom - top;
    }

    public void ensureWidth(int width, boolean scaleRight) {
        if (getWidth() < width)
            if (scaleRight)
                right = left + width;
            else left = right - width;
    }

    public void ensureHeight(int height, boolean scaleBottom) {
        if (getHeight() < height)
            if (scaleBottom)
                bottom = top + height;
            else top = bottom - height;
    }

    public int getLeft() {
        return left;
    }

    public int getRight() {
        return right;
    }

    public int getTop() {
        return top;
    }

    public int getBottom() {
        return bottom;
    }

    public boolean drawString(List<String> strings, FontRenderer fontRenderer, boolean shadow, boolean center, int xOffset, int yOffset, boolean scaleToFitX, boolean scaleToFixY, int color) {
        boolean suc = true;
        for (String string : strings) {
            suc = suc && drawString(string, fontRenderer, shadow, center, xOffset, yOffset, scaleToFitX, scaleToFixY, color);
        }
        return suc;
    }

    public boolean drawString(String string, FontRenderer fontRenderer, boolean shadow, boolean center, int xOffset, int yOffset, boolean scaleToFitX, boolean scaleToFixY, int color) {
        int x = left + xOffset;
        int y = top + yOffset;
        int stringWidth = fontRenderer.getStringWidth(string);
        if (center) {
            x -= stringWidth / 2;
        }
        if (x + stringWidth > right) {
            if (scaleToFitX)
                right = x + stringWidth;
            else return false;
        }
        if (y + 10 > bottom) {
            if (scaleToFixY)
                bottom = y + 10;
            else return false;
        }
        if (y < top) {
            if (scaleToFixY)
                top = y;
            else return false;
        }
        fontRenderer.drawString(string, x, y, color, shadow);
        return true;
    }

}
