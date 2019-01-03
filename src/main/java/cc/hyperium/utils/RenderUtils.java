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

package cc.hyperium.utils;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static org.lwjgl.opengl.GL11.GL_LINE_STRIP;

public class RenderUtils {

    public static void drawFilledCircle(int xx, int yy, float radius, int col) {
        float f = (col >> 24 & 0xFF) / 255.0F;
        float f2 = (col >> 16 & 0xFF) / 255.0F;
        float f3 = (col >> 8 & 0xFF) / 255.0F;
        float f4 = (col & 0xFF) / 255.0F;
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
        GL11.glBegin(6);

        for (int i = 0; i < 50; i++) {
            float x = radius * MathHelper.sin((float) (i * 0.12566370614359174D));
            float y = radius * MathHelper.cos((float) (i * 0.12566370614359174D));
            GlStateManager.color(f2, f3, f4, f);
            GL11.glVertex2f(xx + x, yy + y);
        }

        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();
    }

    public static void drawCircle(int xx, int yy, int radius, int col) {
        float f = (col >> 24 & 0xFF) / 255.0F;
        float f2 = (col >> 16 & 0xFF) / 255.0F;
        float f3 = (col >> 8 & 0xFF) / 255.0F;
        float f4 = (col & 0xFF) / 255.0F;
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glShadeModel(7425);
        GL11.glBegin(2);
        for (int i = 0; i < 70; i++) {
            float x = radius * MathHelper.cos((float) (i * 0.08975979010256552D));
            float y = radius * MathHelper.sin((float) (i * 0.08975979010256552D));
            GlStateManager.color(f2, f3, f4, f);
            GL11.glVertex2f(xx + x, yy + y);
        }
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glPopMatrix();
    }

    public static void drawRect(float g, float h, float i, float j, Color c) {
        Gui.drawRect((int) g, (int) h, (int) i, (int) j, c.getRGB());
    }

    public static void drawRect(float g, float h, float i, float j, int col1) {
        Gui.drawRect((int) g, (int) h, (int) i, (int) j, col1);
    }

    public static void drawBorderedRect(float x, float y, float x2, float y2, float l1, int col1, int col2) {
        drawRect(x, y, x2, y2, col2);
        float f = (col1 >> 24 & 0xFF) / 255.0F;
        float f2 = (col1 >> 16 & 0xFF) / 255.0F;
        float f3 = (col1 >> 8 & 0xFF) / 255.0F;
        float f4 = (col1 & 0xFF) / 255.0F;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glPushMatrix();
        GlStateManager.color(f2, f3, f4, f);
        GL11.glLineWidth(l1);
        GL11.glBegin(1);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x, y2);
        GL11.glVertex2d(x2, y2);
        GL11.glVertex2d(x2, y);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x2, y);
        GL11.glVertex2d(x, y2);
        GL11.glVertex2d(x2, y2);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
    }

    public static void drawBorderedRect(float x, float y, float x2, float y2, float l1, Color c, Color c2) {
        drawRect(x, y, x2, y2, c2);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glPushMatrix();
        glColor(c);
        GL11.glLineWidth(l1);
        GL11.glBegin(1);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x, y2);
        GL11.glVertex2d(x2, y2);
        GL11.glVertex2d(x2, y);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x2, y);
        GL11.glVertex2d(x, y2);
        GL11.glVertex2d(x2, y2);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
    }

    public static void glColor(int hex) {
        float alpha = (hex >> 24 & 0xFF) / 255.0F;
        float red = (hex >> 16 & 0xFF) / 255.0F;
        float green = (hex >> 8 & 0xFF) / 255.0F;
        float blue = (hex & 0xFF) / 255.0F;
        GlStateManager.color(red, green, blue, alpha);
    }

    public static void glColor(Color color) {
        GlStateManager.color(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F,
            color.getAlpha() / 255.0F);
    }

    public static void drawLine(float x, float y, float x1, float y1, float width, int colour) {
        float red = (float) (colour >> 16 & 0xFF) / 255F;
        float green = (float) (colour >> 8 & 0xFF) / 255F;
        float blue = (float) (colour & 0xFF) / 255F;
        float alpha = (float) (colour >> 24 & 0xFF) / 255F;

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);

        GL11.glPushMatrix();
        GlStateManager.color(red, green, blue, alpha);
        GL11.glLineWidth(width);
        GL11.glBegin(GL11.GL_LINE_STRIP);
        GL11.glVertex2f(x, y);
        GL11.glVertex2f(x1, y1);
        GL11.glEnd();
        GL11.glPopMatrix();

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
    }

    public static void drawSmoothRect(int left, int top, int right, int bottom, int color) {
        drawSmoothRect(left, top, right, bottom, 4, color);

    }

    public static void drawSmoothRect(int left, int top, int right, int bottom, int circleSize, int color) {
        left += circleSize;
        right -= circleSize;
        Gui.drawRect(left, top, right, bottom, color);
        int i = circleSize - 1;
        Gui.drawRect(left - circleSize, top + i, left, bottom - i, color);
        Gui.drawRect(right, top + i, right + circleSize, bottom - i, color);

        RenderUtils.drawFilledCircle(left, top + circleSize, circleSize, color);
        RenderUtils.drawFilledCircle(left, bottom - circleSize, circleSize, color);

        RenderUtils.drawFilledCircle(right, top + circleSize, circleSize, color);
        RenderUtils.drawFilledCircle(right, bottom - circleSize, circleSize, color);

    }

    public static void drawArc(float cx, float cy, float r, float startAngle, float angle, int segments, int color) {
        float red = (float) (color >> 16 & 0xFF) / 255F;
        float green = (float) (color >> 8 & 0xFF) / 255F;
        float blue = (float) (color & 0xFF) / 255F;
        float alpha = (float) (color >> 24 & 0xFF) / 255F;

        float theta = angle / (float) (segments - 1);

        double tf = Math.tan(theta);
        float rf = MathHelper.cos(theta);

        float x = r * MathHelper.cos(startAngle);
        float y = r * MathHelper.sin(startAngle);

        GlStateManager.pushMatrix();
        GlStateManager.color(red, green, blue, alpha);
        GL11.glBegin(GL_LINE_STRIP);
        for (int ii = 0; ii < segments; ii++) {
            GL11.glVertex2f(x + cx, y + cy);

            float tx = -y;
            float ty = x;

            x += tx * tf;
            y += ty * tf;

            x *= rf;
            y *= rf;
        }
        GL11.glEnd();
        GlStateManager.popMatrix();
    }
}
