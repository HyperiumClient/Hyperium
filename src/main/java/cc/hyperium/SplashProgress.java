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

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class SplashProgress {
    private static final int MAX = 12;
    public static int PROGRESS = 0;
    public static String CURRENT = "";
    private static ResourceLocation splash;
    private static TextureManager ctm;

    public static void update(){
        if(ctm == null)return;
//        drawProgress();
    }

    /**
     * FontRenderer for drawing splash screen
     */
    private static FontRenderer sfr;
    public static void drawSplash(TextureManager tm){
        if(ctm == null)
            ctm = tm;
        ScaledResolution scaledresolution = new ScaledResolution(Minecraft.getMinecraft());
        int i = scaledresolution.getScaleFactor();
        Framebuffer framebuffer = new Framebuffer(scaledresolution.getScaledWidth() * i, scaledresolution.getScaledHeight() * i, true);
        framebuffer.bindFramebuffer(false);
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        GlStateManager.ortho(0.0D, (double)scaledresolution.getScaledWidth(), (double)scaledresolution.getScaledHeight(), 0.0D, 1000.0D, 3000.0D);
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();
        GlStateManager.translate(0.0F, 0.0F, -2000.0F);
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        GlStateManager.disableDepth();
        GlStateManager.enableTexture2D();
        if(splash == null)
            splash = new ResourceLocation("textures/hyperium-splash.png");
        tm.bindTexture(splash);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Gui.drawScaledCustomSizeModalRect(0, 0, 0, 0, 1920, 1080, scaledresolution.getScaledWidth(), scaledresolution.getScaledHeight(), 1920, 1080);
//        drawProgress();
        framebuffer.unbindFramebuffer();
        framebuffer.framebufferRender(scaledresolution.getScaledWidth() * i, scaledresolution.getScaledHeight() * i);
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1F);
        Minecraft.getMinecraft().updateDisplay();
    }

    private static void drawProgress(){
        if(sfr == null)
            sfr = new FontRenderer(Minecraft.getMinecraft().gameSettings, new ResourceLocation("textures/font/ascii.png"), Minecraft.getMinecraft().getTextureManager(), false);
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        // Bar base
        Gui.drawRect(sr.getScaledWidth() / 2 - 75, sr.getScaledHeight() / 2 + 45, sr.getScaledWidth() / 2 + 75, sr.getScaledHeight() / 2 + 55, new Color(0, 0, 0, 50).getRGB());
        // Progress
        Gui.drawRect(sr.getScaledWidth() / 2 - 75, sr.getScaledHeight() / 2 + 45, (sr.getScaledWidth() / 2 - 75) + ((SplashProgress.PROGRESS / SplashProgress.MAX) * 150), sr.getScaledHeight() / 2 + 55, 0xFFFFFF);
    }

}
