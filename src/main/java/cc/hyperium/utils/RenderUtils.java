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

package cc.hyperium.utils;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static org.lwjgl.opengl.GL11.GL_LINE_STRIP;

public class RenderUtils {

    private static void bindColor(int color) {
        float alpha = (color >> 24 & 255) / 255.0f;
        float red = (color >> 16 & 255) / 255.0f;
        float green = (color >> 8 & 255) / 255.0f;
        float blue = (color & 255) / 255.0f;
        GlStateManager.color(red, green, blue, alpha);
    }

    private static void preDraw() {
        GlStateManager.pushAttrib();
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableTexture2D();
    }

    private static void postDraw() {
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
        GlStateManager.bindTexture(0);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public static void drawFilledCircle(int x, int y, float radius, int color) {
        preDraw();

        GL11.glBegin(GL11.GL_TRIANGLE_FAN);

        for (int i = 0; i < 50; i++) {
            float px = x + radius * MathHelper.sin((float) (i * (6.28318530718 / 50)));
            float py = y + radius * MathHelper.cos((float) (i * (6.28318530718 / 50)));

            bindColor(color);

            GL11.glVertex2d(px, py);
        }

        GL11.glEnd();
        postDraw();
    }

    public static void drawRect(float g, float h, float i, float j, Color c) {
        Gui.drawRect((int) g, (int) h, (int) i, (int) j, c.getRGB());
    }

    public static void drawRect(float g, float h, float i, float j, int col1) {
        Gui.drawRect((int) g, (int) h, (int) i, (int) j, col1);
    }

    public static void drawLine(float x, float y, float x1, float y1, float width, int colour) {
        bindColor(colour);
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);

        GlStateManager.pushMatrix();
        GL11.glLineWidth(width);
        GL11.glBegin(GL11.GL_LINE_STRIP);
        GL11.glVertex2f(x, y);
        GL11.glVertex2f(x1, y1);
        GL11.glEnd();
        GlStateManager.popMatrix();

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
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

        drawFilledCircle(left, top + circleSize, circleSize, color);
        drawFilledCircle(left, bottom - circleSize, circleSize, color);

        drawFilledCircle(right, top + circleSize, circleSize, color);
        drawFilledCircle(right, bottom - circleSize, circleSize, color);
    }
}
