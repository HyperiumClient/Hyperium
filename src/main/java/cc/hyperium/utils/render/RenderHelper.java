package cc.hyperium.utils.render;

import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

public class RenderHelper {

    private static void bindColor(int color) {
        float alpha = (color >> 24 & 255) / 255.0F;
        float red = (color >> 16 & 255) / 255.0F;
        float green = (color >> 8 & 255) / 255.0F;
        float blue = (color & 255) / 255.0F;
        GL11.glColor4f(red, green, blue, alpha);
    }

    private static void preDraw() {
        GlStateManager.pushAttrib();
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
    }

    private static void postDraw() {
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
        GlStateManager.bindTexture(0);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public static void drawLine(int sx, int sy, int ex, int ey, int color) {
        preDraw();
        bindColor(color);
        GL11.glBegin(GL11.GL_LINE_STRIP);
        GL11.glVertex2d(sx, sy);
        GL11.glVertex2d(ex, ey);
        GL11.glEnd();
        postDraw();
    }
}
