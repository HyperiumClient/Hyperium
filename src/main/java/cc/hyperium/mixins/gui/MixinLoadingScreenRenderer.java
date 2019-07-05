package cc.hyperium.mixins.gui;

import cc.hyperium.mixinsimp.client.GlStateModifier;
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
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import java.awt.*;

@Mixin(LoadingScreenRenderer.class)
public abstract class MixinLoadingScreenRenderer implements IProgressUpdate {

    @Shadow
    private String message;
    @Shadow
    private Minecraft mc;
    @Shadow
    private String currentlyDisplayedText;
    @Shadow
    private long systemTime;
    @Shadow
    private ScaledResolution scaledResolution;
    @Shadow
    private Framebuffer framebuffer;

    private static boolean first = true;

    /**
     * @author intellij please just leave me alone
     */
    @Overwrite
    public void setLoadingProgress(int progress) {
        long i = Minecraft.getSystemTime();

        if (i - this.systemTime >= 100L) {
            this.systemTime = i;
            ScaledResolution scaledresolution = new ScaledResolution(this.mc);
            int j = scaledresolution.getScaleFactor();
            int k = scaledresolution.getScaledWidth();
            int l = scaledresolution.getScaledHeight();

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

            if(progress == -1 && first) {
                progress = 2;
                first = false;
            } else {
                progress = 10;
            }

            if (progress >= 0) {
                int i1 = 100;
                int j1 = 2;
                int k1 = k / 2 - i1 / 2;
                int l1 = scaledResolution.getScaledHeight() - 15;
                GlStateManager.disableTexture2D();;
                worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
                worldrenderer.pos((double) i1, (double) l1, 0.0D).color(128, 128, 128, 255).endVertex();
                worldrenderer.pos((double) i1, (double) (l1 + j1), 0.0D).color(128, 128, 128, 255).endVertex();
                worldrenderer.pos((double) (i1 + i1), (double) (l1 + j1), 0.0D).color(128, 128, 128, 255).endVertex();
                worldrenderer.pos((double) (i1 + i1), (double) l1, 0.0D).color(128, 128, 128, 255).endVertex();
                worldrenderer.pos((double) i1, (double) l1, 0.0D).color(128, 255, 128, 255).endVertex();
                worldrenderer.pos((double) i1, (double) (l1 + j1), 0.0D).color(128, 255, 128, 255).endVertex();
                worldrenderer.pos((double) (i1 + progress), (double) (l1 + j1), 0.0D).color(128, 255, 128, 255).endVertex();
                worldrenderer.pos((double) (i1 + progress), (double) l1, 0.0D).color(128, 255, 128, 255).endVertex();
                tessellator.draw();
                GlStateManager.enableTexture2D();
            }

            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            this.mc.fontRendererObj.drawString(this.currentlyDisplayedText, 5, scaledResolution.getScaledHeight() - 30, 16777215);
            this.mc.fontRendererObj.drawString(this.message, 5, scaledResolution.getScaledHeight() - 15, 16777215);
            this.framebuffer.unbindFramebuffer();

            if (OpenGlHelper.isFramebufferEnabled()) {
                this.framebuffer.framebufferRender(k * j, l * j);
            }

            this.mc.updateDisplay();

            try {
                Thread.yield();
            } catch (Exception ignored) {
            }
        }
    }
}
