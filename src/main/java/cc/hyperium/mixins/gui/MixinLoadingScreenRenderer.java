package cc.hyperium.mixins.gui;

import net.minecraft.client.LoadingScreenRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.awt.*;

@Mixin(LoadingScreenRenderer.class)
public abstract class MixinLoadingScreenRenderer implements IProgressUpdate {

    @Shadow private String message;
    @Shadow private Minecraft mc;
    @Shadow private String currentlyDisplayedText;
    @Shadow private long systemTime;
    @Shadow private ScaledResolution scaledResolution;
    @Shadow private Framebuffer framebuffer;

    /**
     * @author intellij please just leave me alone
     */
    @Overwrite
    public void setLoadingProgress(int progress) {
        long nanoTime = Minecraft.getSystemTime();

        if (nanoTime - this.systemTime >= 100L) {
            this.systemTime = nanoTime;
            ScaledResolution scaledresolution = new ScaledResolution(this.mc);
            int scaleFactor = scaledresolution.getScaleFactor();
            int scaledWidth = scaledresolution.getScaledWidth();
            int scaledHeight = scaledresolution.getScaledHeight();

            if (OpenGlHelper.isFramebufferEnabled()) {
                this.framebuffer.framebufferClear();
            } else {
                GlStateManager.clear(256);
            }

            this.framebuffer.bindFramebuffer(false);
            GlStateManager.matrixMode(5889);
            GlStateManager.loadIdentity();
            GlStateManager.ortho(0.0D, scaledresolution.getScaledWidth_double(), scaledresolution.getScaledHeight_double(), 0.0D, 100.0D, 300.0D);
            GlStateManager.matrixMode(5888);
            GlStateManager.loadIdentity();
            GlStateManager.translate(0.0F, 0.0F, -200.0F);

            if (!OpenGlHelper.isFramebufferEnabled()) {
                GlStateManager.clear(16640);
            }

            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            mc.getTextureManager().bindTexture(new ResourceLocation("textures/world-loading.png"));

            Gui.drawModalRectWithCustomSizedTexture(0, 0, 0.0f, 0.0f, scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight(), scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight());

            if (this.currentlyDisplayedText.equals("Loading world")) {
                if (this.message.isEmpty()) {
                    progress = 33;
                } else if (this.message.equals("Converting world")) {
                    progress = 66;
                } else if (this.message.equals("Building terrain")) {
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
                worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
                worldrenderer.pos((double) maxProgress, (double) barHeight, 0.0D).color(128, 128, 128, 255).endVertex();
                worldrenderer.pos((double) maxProgress, (double) (barHeight + barTop), 0.0D).color(128, 128, 128, 255).endVertex();
                worldrenderer.pos((double) (maxProgress + maxProgress), (double) (barHeight + barTop), 0.0D).color(128, 128, 128, 255).endVertex();
                worldrenderer.pos((double) (maxProgress + maxProgress), (double) barHeight, 0.0D).color(128, 128, 128, 255).endVertex();
                worldrenderer.pos((double) maxProgress, (double) barHeight, 0.0D).color(128, 255, 128, 255).endVertex();
                worldrenderer.pos((double) maxProgress, (double) (barHeight + barTop), 0.0D).color(128, 255, 128, 255).endVertex();
                worldrenderer.pos((double) (maxProgress + progress), (double) (barHeight + barTop), 0.0D).color(128, 255, 128, 255).endVertex();
                worldrenderer.pos((double) (maxProgress + progress), (double) barHeight, 0.0D).color(128, 255, 128, 255).endVertex();
                tessellator.draw();
                GlStateManager.enableAlpha();
                GlStateManager.enableBlend();
                Gui.drawRect(0, scaledResolution.getScaledHeight() - 35, scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight(), new Color(0, 0, 0, 50).getRGB());
                GlStateManager.disableAlpha();
                GlStateManager.disableBlend();
                GlStateManager.enableTexture2D();
            }

            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
            int white = 16777215;
            this.mc.fontRendererObj.drawString(this.currentlyDisplayedText, 5, scaledResolution.getScaledHeight() - 30, white);
            this.mc.fontRendererObj.drawString(this.message, 5, scaledResolution.getScaledHeight() - 15, white);
            this.framebuffer.unbindFramebuffer();

            if (OpenGlHelper.isFramebufferEnabled()) {
                this.framebuffer.framebufferRender(scaledWidth * scaleFactor, scaledHeight * scaleFactor);
            }

            this.mc.updateDisplay();

            try {
                Thread.yield();
            } catch (Exception ignored) {
            }
        }
    }
}
