package cc.hyperium.gui.loading;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class HyperiumLoadingScreen {

  public static void renderHyperiumLoadingScreen(long systemTime, Framebuffer framebuffer,
      Minecraft mc, ScaledResolution scaledResolution, String currentlyDisplayedText,
      String message, int progress) {
    long nanoTime = Minecraft.getSystemTime();

    if (nanoTime - systemTime >= 100L) {
      ScaledResolution scaledresolution = new ScaledResolution(mc);
      int scaleFactor = scaledresolution.getScaleFactor();
      int scaledWidth = scaledresolution.getScaledWidth();
      int scaledHeight = scaledresolution.getScaledHeight();

      if (OpenGlHelper.isFramebufferEnabled()) {
        framebuffer.framebufferClear();
      } else {
        GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT);
      }

      framebuffer.bindFramebuffer(false);
      GlStateManager.matrixMode(GL11.GL_PROJECTION);
      GlStateManager.loadIdentity();
      GlStateManager.ortho(0.0D, scaledresolution.getScaledWidth_double(),
          scaledresolution.getScaledHeight_double(), 0.0D, 100.0D, 300.0D);
      GlStateManager.matrixMode(GL11.GL_MODELVIEW);
      GlStateManager.loadIdentity();
      GlStateManager.translate(0.0F, 0.0F, -200.0F);

      if (!OpenGlHelper.isFramebufferEnabled()) {
        GlStateManager.clear(16640);
      }

      Tessellator tessellator = Tessellator.getInstance();
      WorldRenderer worldrenderer = tessellator.getWorldRenderer();
      mc.getTextureManager()
          .bindTexture(new ResourceLocation("hyperium", "textures/world-loading.png"));

      Gui.drawModalRectWithCustomSizedTexture(0, 0, 0.0f, 0.0f, scaledResolution.getScaledWidth(),
          scaledResolution.getScaledHeight(), scaledResolution.getScaledWidth(),
          scaledResolution.getScaledHeight());

      if ("Loading world".equals(currentlyDisplayedText)) {
        if (message.isEmpty()) {
          progress = 33;
        } else if (message.equals("Converting world")) {
          progress = 66;
        } else if (message.equals("Building terrain")) {
          progress = 90;
        } else {
          progress = 100;
        }
      } else {
        progress = -1;
      }

      if (progress >= 0) {
        int maxProgress = 100;
        int barTop = 2;
        int barHeight = scaledResolution.getScaledHeight() - 15;
        GlStateManager.disableTexture2D();
        worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos(maxProgress, barHeight, 0.0D).color(128, 128, 128, 255).endVertex();
        worldrenderer.pos(maxProgress, barHeight + barTop, 0.0D).color(128, 128, 128, 255)
            .endVertex();
        worldrenderer.pos(maxProgress + maxProgress, barHeight + barTop, 0.0D)
            .color(128, 128, 128, 255).endVertex();
        worldrenderer.pos(maxProgress + maxProgress, barHeight, 0.0D).color(128, 128, 128, 255)
            .endVertex();
        worldrenderer.pos(maxProgress, barHeight, 0.0D).color(128, 255, 128, 255).endVertex();
        worldrenderer.pos(maxProgress, barHeight + barTop, 0.0D).color(128, 255, 128, 255)
            .endVertex();
        worldrenderer.pos(maxProgress + progress, barHeight + barTop, 0.0D)
            .color(128, 255, 128, 255).endVertex();
        worldrenderer.pos(maxProgress + progress, barHeight, 0.0D).color(128, 255, 128, 255)
            .endVertex();
        tessellator.draw();
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        Gui.drawRect(0, scaledResolution.getScaledHeight() - 35, scaledResolution.getScaledWidth(),
            scaledResolution.getScaledHeight(),
            new Color(0, 0, 0, 50).getRGB());
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
      }

      GlStateManager.enableBlend();
      GlStateManager
          .tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE,
              GL11.GL_ZERO);
      mc.fontRendererObj
          .drawString(currentlyDisplayedText, 5, scaledResolution.getScaledHeight() - 30, -1);
      mc.fontRendererObj.drawString(message, 5, scaledResolution.getScaledHeight() - 15, -1);
      framebuffer.unbindFramebuffer();

      if (OpenGlHelper.isFramebufferEnabled()) {
        framebuffer.framebufferRender(scaledWidth * scaleFactor, scaledHeight * scaleFactor);
      }

      mc.updateDisplay();

      try {
        Thread.yield();
      } catch (Exception ignored) {
      }
    }
  }
}
