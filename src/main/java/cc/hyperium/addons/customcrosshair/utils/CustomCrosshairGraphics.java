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

package cc.hyperium.addons.customcrosshair.utils;

import cc.hyperium.addons.customcrosshair.CustomCrosshairAddon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class CustomCrosshairGraphics {

    private static FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;

    public static void drawHorizontalLine(final int y, final int x1, final int x2,
                                          final Color colour) {

        drawFilledRectangle(x1, y, x2, y + 1, colour);
    }

    public static void drawVerticalLine(final int x, final int y1, final int y2,
                                        final Color colour) {
        drawFilledRectangle(x, y1, x + 1, y2, colour);
    }

    public static void drawRectangle(final int x1, final int y1, final int x2, final int y2,
                                     final Color colour) {
        drawHorizontalLine(y1, x1, x2, colour);
        drawHorizontalLine(y2, x1, x2 + 1, colour);
        drawVerticalLine(x1, y1, y2, colour);
        drawVerticalLine(x2, y1, y2, colour);
    }

    public static void drawFilledRectangle(int x1, int y1, int x2, int y2, final Color colour) {
        if (x1 < x2) {
            final int tempX = x1;
            x1 = x2;
            x2 = tempX;
        }
        if (y1 < y2) {
            final int tempY = y1;
            y1 = y2;
            y2 = tempY;
        }
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
        GlStateManager
            .color(colour.getRed() / 255.0f, colour.getGreen() / 255.0f, colour.getBlue() / 255.0f,
                colour.getAlpha() / 255.0f);
        worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldrenderer.pos(x1, y2, 0.0).endVertex();
        worldrenderer.pos(x2, y2, 0.0).endVertex();
        worldrenderer.pos(x2, y1, 0.0).endVertex();
        worldrenderer.pos(x1, y1, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawBorderedRectangle(final int x1, final int y1, final int x2, final int y2,
                                             final Color innerColour, final Color outerColour) {
        drawFilledRectangle(x1, y1, x2, y2, innerColour);
        drawRectangle(x1, y1, x2, y2, outerColour);
    }

    public static void drawThemeBorderedRectangle(final int x1, final int y1, final int x2,
                                                  final int y2) {
        drawBorderedRectangle(x1, y1, x2, y2, CustomCrosshairAddon.PRIMARY_T, CustomCrosshairAddon.SECONDARY);
    }

    public static void drawString(final String text, final int x, final int y, final int colour) {
        CustomCrosshairGraphics.fontRenderer.drawString(text, x, y, colour);
    }

    public static void drawStringWithShadow(final String text, final int x, final int y,
                                            final int colour) {
        fontRenderer.drawStringWithShadow(text, (float) x, (float) y, colour);
    }

    public static void drawLine(final int x1, final int y1, final int x2, final int y2,
                                final Color colour) {
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
        GlStateManager
            .color(colour.getRed() / 255.0f, colour.getGreen() / 255.0f, colour.getBlue() / 255.0f,
                colour.getAlpha() / 255.0f);
        worldrenderer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION);
        worldrenderer.pos(x1, y1, 0.0).endVertex();
        worldrenderer.pos(x2, y2, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawCircle(final double x, final double y, final int radius,
                                  final Color colour) {
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
        GlStateManager
            .color(colour.getRed() / 255.0f, colour.getGreen() / 255.0f, colour.getBlue() / 255.0f,
                colour.getAlpha() / 255.0f);
        worldrenderer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION);
        for (int degrees = 0; degrees <= 360; ++degrees) {
            final float radians = (float) (degrees * 0.017453292519943295);
            worldrenderer.pos(x + Math.cos(radians) * radius, y + Math.sin(radians) * radius, 0.0)
                .endVertex();
        }
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
}
