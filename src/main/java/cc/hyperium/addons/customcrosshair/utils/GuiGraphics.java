package cc.hyperium.addons.customcrosshair.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class GuiGraphics {
    private static FontRenderer fontRenderer;

    public static void drawHorizontalLine(final int y, final int x1, final int x2, final RGBA colour) {
        drawFilledRectangle(x1, y, x2, y + 1, colour);
    }

    public static void drawVerticalLine(final int x, final int y1, final int y2, final RGBA colour) {
        drawFilledRectangle(x, y1, x + 1, y2, colour);
    }

    public static void drawRectangle(final int x1, final int y1, final int x2, final int y2, final RGBA colour) {
        drawHorizontalLine(y1, x1, x2, colour);
        drawHorizontalLine(y2, x1, x2 + 1, colour);
        drawVerticalLine(x1, y1, y2, colour);
        drawVerticalLine(x2, y1, y2, colour);
    }

    public static void drawFilledRectangle(int x1, int y1, int x2, int y2, final RGBA colour) {
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
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(colour.getRed() / 255.0f, colour.getGreen() / 255.0f, colour.getBlue() / 255.0f, colour.getOpacity() / 255.0f);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos((double)x1, (double)y2, 0.0).endVertex();
        worldrenderer.pos((double)x2, (double)y2, 0.0).endVertex();
        worldrenderer.pos((double)x2, (double)y1, 0.0).endVertex();
        worldrenderer.pos((double)x1, (double)y1, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawTexturedRectangle(final int x, final int y, final int textureX, final int textureY, final int width, final int height) {
        final float f = 0.00390625f;
        final float f2 = 0.00390625f;
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos((double)(x + 0), (double)(y + height), 0.0).tex((double)((textureX + 0) * f), (double)((textureY + height) * f2)).endVertex();
        worldrenderer.pos((double)(x + width), (double)(y + height), 0.0).tex((double)((textureX + width) * f), (double)((textureY + height) * f2)).endVertex();
        worldrenderer.pos((double)(x + width), (double)(y + 0), 0.0).tex((double)((textureX + width) * f), (double)((textureY + 0) * f2)).endVertex();
        worldrenderer.pos((double)(x + 0), (double)(y + 0), 0.0).tex((double)((textureX + 0) * f), (double)((textureY + 0) * f2)).endVertex();
        tessellator.draw();
    }

    public static void drawBorderedRectangle(final int x1, final int y1, final int x2, final int y2, final RGBA innerColour, final RGBA outerColour) {
        drawFilledRectangle(x1, y1, x2, y2, innerColour);
        drawRectangle(x1, y1, x2, y2, outerColour);
    }

    public static void drawThemeBorderedRectangle(final int x1, final int y1, final int x2, final int y2) {
        drawBorderedRectangle(x1, y1, x2, y2, GuiTheme.PRIMARY_T, GuiTheme.SECONDARY);
    }

    public static void drawString(final String text, final int x, final int y, final int colour) {
        GuiGraphics.fontRenderer.drawString(text, x, y, colour);
    }

    public static void drawStringWithShadow(final String text, final int x, final int y, final int colour) {
        GuiGraphics.fontRenderer.drawStringWithShadow(text, (float)x, (float)y, colour);
    }

    public static int getStringWidth(final String text) {
        return GuiGraphics.fontRenderer.getStringWidth(text);
    }

    public static int[] getScreenSize() {
        final int[] size = new int[2];
        final ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());
        size[0] = resolution.getScaledWidth();
        size[1] = resolution.getScaledHeight();
        return size;
    }

    public static double[] getScreenSizeDouble() {
        final double[] size = new double[2];
        final ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());
        size[0] = resolution.getScaledWidth_double();
        size[1] = resolution.getScaledHeight_double();
        return size;
    }

    public static void drawLine(final int x1, final int y1, final int x2, final int y2, final RGBA colour) {
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(colour.getRed() / 255.0f, colour.getGreen() / 255.0f, colour.getBlue() / 255.0f, colour.getOpacity() / 255.0f);
        worldrenderer.begin(3, DefaultVertexFormats.POSITION);
        worldrenderer.pos((double)x1, (double)y1, 0.0).endVertex();
        worldrenderer.pos((double)x2, (double)y2, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawCircle(final double x, final double y, final int radius, final RGBA colour) {
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(colour.getRed() / 255.0f, colour.getGreen() / 255.0f, colour.getBlue() / 255.0f, colour.getOpacity() / 255.0f);
        worldrenderer.begin(3, DefaultVertexFormats.POSITION);
        for (int degrees = 0; degrees <= 360; ++degrees) {
            final float radians = (float)(degrees * 0.017453292519943295);
            worldrenderer.pos(x + Math.cos(radians) * radius, y + Math.sin(radians) * radius, 0.0).endVertex();
        }
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    static {
        GuiGraphics.fontRenderer = Minecraft.getMinecraft().fontRendererObj;
    }
}
