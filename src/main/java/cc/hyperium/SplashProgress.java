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

package cc.hyperium;

import cc.hyperium.mixinsimp.client.GlStateModifier;
import cc.hyperium.utils.HyperiumFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ResourceLocation;

import java.awt.Color;
import java.awt.Font;

public class SplashProgress {

    public static final int DEFAULT_MAX = 13;
    public static int MAX = DEFAULT_MAX;
    public static int PROGRESS = 0;
    public static String CURRENT = "";
    public static boolean CANCEL_IF_MAX = true;
    private static ResourceLocation splash;
    private static TextureManager ctm;
    /**
     * FontRenderer for drawing splash screen
     */
    private static HyperiumFontRenderer sfr;

    public static void update() {
        if (Minecraft.getMinecraft() == null || Minecraft.getMinecraft().getLanguageManager() == null) {
            return;
        }
        drawSplash(Minecraft.getMinecraft().getTextureManager());
    }

    public static void setProgress(int givenProgress, String givenSplash) {
        PROGRESS = givenProgress;
        CURRENT = givenSplash;
        update();
    }

    public static void drawSplash(TextureManager tm) {
        if (ctm == null) {
            ctm = tm;
        }

        ScaledResolution scaledresolution = new ScaledResolution(Minecraft.getMinecraft());
        int i = scaledresolution.getScaleFactor();
        Framebuffer framebuffer = new Framebuffer(scaledresolution.getScaledWidth() * i, scaledresolution.getScaledHeight() * i, true);
        framebuffer.bindFramebuffer(false);
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        GlStateManager.ortho(0.0D, (double) scaledresolution.getScaledWidth(), (double) scaledresolution.getScaledHeight(), 0.0D, 1000.0D, 3000.0D);
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();
        GlStateManager.translate(0.0F, 0.0F, -2000.0F);
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        GlStateManager.disableDepth();
        GlStateManager.enableTexture2D();

        if (splash == null) {
            splash = new ResourceLocation("textures/hyperium-splash.png");
        }

        tm.bindTexture(splash);

        GlStateManager.resetColor();
        GlStateModifier.INSTANCE.reset();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Gui.drawScaledCustomSizeModalRect(0, 0, 0, 0, 1920, 1080, scaledresolution.getScaledWidth(), scaledresolution.getScaledHeight(), 1920, 1080);
        drawProgress();
        framebuffer.unbindFramebuffer();
        framebuffer.framebufferRender(scaledresolution.getScaledWidth() * i, scaledresolution.getScaledHeight() * i);
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1F);
        Minecraft.getMinecraft().updateDisplay();
    }

    private static void drawProgress() {
        if (Minecraft.getMinecraft().gameSettings == null || Minecraft.getMinecraft().getTextureManager() == null) {
            return;
        }

        if (sfr == null) {
            sfr = new HyperiumFontRenderer("Arial", Font.PLAIN, 20);
        }

        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

        double nProgress = (double) PROGRESS;
        double calc = (nProgress / MAX) * sr.getScaledWidth();

        Gui.drawRect(0, sr.getScaledHeight() - 35, sr.getScaledWidth(), sr.getScaledHeight(), new Color(0, 0, 0, 50).getRGB());

        GlStateManager.resetColor();
        GlStateModifier.INSTANCE.reset();
        sfr.drawString(CURRENT, 20, sr.getScaledHeight() - 25, 0xffffffff);
        String s = PROGRESS + "/" + MAX;
        sfr.drawString(s, sr.getScaledWidth() - 20 - sfr.getStringWidth(s), sr.getScaledHeight() - 25, 0xe1e1e1ff);
        // Progress
        GlStateManager.resetColor();
        GlStateModifier.INSTANCE.reset();
        Gui.drawRect(0, sr.getScaledHeight() - 2, (int) calc, sr.getScaledHeight(), new Color(149, 201, 144).getRGB());
        // Bar base
        Gui.drawRect(0, sr.getScaledHeight() - 2, sr.getScaledWidth(), sr.getScaledHeight(), new Color(0, 0, 0, 10).getRGB());
    }
}
