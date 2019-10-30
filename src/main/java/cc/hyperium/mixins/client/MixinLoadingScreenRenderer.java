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

package cc.hyperium.mixins.client;

import cc.hyperium.config.Settings;
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
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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
     * @author ConorTheDev
     * @reason Custom screen when loading to a new world
     */
    @Inject(method = "setLoadingProgress", at = @At("HEAD"), cancellable = true)
    private void setLoadingProgress(CallbackInfo ci) {
        if (Settings.HYPERIUM_LOADING_SCREEN) {
            long nanoTime = Minecraft.getSystemTime();

            if (nanoTime - systemTime >= 100L) {
                systemTime = nanoTime;
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
                GlStateManager.ortho(0.0D, scaledresolution.getScaledWidth_double(), scaledresolution.getScaledHeight_double(), 0.0D, 100.0D, 300.0D);
                GlStateManager.matrixMode(GL11.GL_MODELVIEW);
                GlStateManager.loadIdentity();
                GlStateManager.translate(0.0F, 0.0F, -200.0F);

                if (!OpenGlHelper.isFramebufferEnabled()) GlStateManager.clear(16640);

                Tessellator tessellator = Tessellator.getInstance();
                WorldRenderer worldrenderer = tessellator.getWorldRenderer();
                mc.getTextureManager().bindTexture(new ResourceLocation("textures/world-loading.png"));

                Gui.drawModalRectWithCustomSizedTexture(0, 0, 0.0f, 0.0f, scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight(), scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight());

                int progress;
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
                    worldrenderer.pos(maxProgress, barHeight + barTop, 0.0D).color(128, 128, 128, 255).endVertex();
                    worldrenderer.pos(maxProgress + maxProgress, barHeight + barTop, 0.0D).color(128, 128, 128, 255).endVertex();
                    worldrenderer.pos(maxProgress + maxProgress, barHeight, 0.0D).color(128, 128, 128, 255).endVertex();
                    worldrenderer.pos(maxProgress, barHeight, 0.0D).color(128, 255, 128, 255).endVertex();
                    worldrenderer.pos(maxProgress, barHeight + barTop, 0.0D).color(128, 255, 128, 255).endVertex();
                    worldrenderer.pos(maxProgress + progress, barHeight + barTop, 0.0D).color(128, 255, 128, 255).endVertex();
                    worldrenderer.pos(maxProgress + progress, barHeight, 0.0D).color(128, 255, 128, 255).endVertex();
                    tessellator.draw();
                    GlStateManager.enableAlpha();
                    GlStateManager.enableBlend();
                    Gui.drawRect(0, scaledResolution.getScaledHeight() - 35, scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight(),
                        new Color(0, 0, 0, 50).getRGB());
                    GlStateManager.disableAlpha();
                    GlStateManager.disableBlend();
                    GlStateManager.enableTexture2D();
                }

                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
                mc.fontRendererObj.drawString(currentlyDisplayedText, 5, scaledResolution.getScaledHeight() - 30, -1);
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

            ci.cancel();
        }
    }
}
