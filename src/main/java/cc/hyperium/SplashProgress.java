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

package cc.hyperium;

import cc.hyperium.utils.HyperiumFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class SplashProgress {

    // Max amount of progress updates
    private static final int DEFAULT_MAX = 11;

    // Current progress
    private static int PROGRESS;

    // Currently displayed progress text
    private static String CURRENT = "";

    // Background texture
    private static ResourceLocation splash;

    // Texture manager
    private static TextureManager ctm;

    // Font renderer
    private static HyperiumFontRenderer raleway;
    private static HyperiumFontRenderer roboto;

    /**
     * Update the splash text
     */
    public static void update() {
        if (Minecraft.getMinecraft() == null || Minecraft.getMinecraft().getLanguageManager() == null) return;
        drawSplash(Minecraft.getMinecraft().getTextureManager());
    }

    /**
     * Update the splash progress
     *
     * @param givenProgress Stage displayed on the splash
     * @param givenSplash   Text displayed on the splash
     */
    public static void setProgress(int givenProgress, String givenSplash) {
        PROGRESS = givenProgress;
        CURRENT = givenSplash;
        update();
    }

    /**
     * Render the splash screen background
     *
     * @param tm {@link TextureManager}
     */
    public static void drawSplash(TextureManager tm) {
        // Initialize the texture manager if null
        if (ctm == null) ctm = tm;

        // Get the users screen width and height to apply
        ScaledResolution scaledresolution = new ScaledResolution(Minecraft.getMinecraft());

        // Create the scale factor
        int scaleFactor = scaledresolution.getScaleFactor();

        // Bind the width and height to the framebuffer
        Framebuffer framebuffer = new Framebuffer(scaledresolution.getScaledWidth() * scaleFactor,
            scaledresolution.getScaledHeight() * scaleFactor, true);
        framebuffer.bindFramebuffer(false);

        // Create the projected image to be rendered
        GlStateManager.matrixMode(GL11.GL_PROJECTION);
        GlStateManager.loadIdentity();
        GlStateManager.ortho(0.0D, scaledresolution.getScaledWidth(), scaledresolution.getScaledHeight(), 0.0D, 1000.0D, 3000.0D);
        GlStateManager.matrixMode(GL11.GL_MODELVIEW);
        GlStateManager.loadIdentity();
        GlStateManager.translate(0.0F, 0.0F, -2000.0F);
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        GlStateManager.disableDepth();
        GlStateManager.enableTexture2D();

        // Initialize the splash texture
        if (splash == null) splash = new ResourceLocation("textures/hyperium-splash.png");

        // Bind the texture
        tm.bindTexture(splash);

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        // Draw the image
        Gui.drawScaledCustomSizeModalRect(0, 0, 0, 0, 1920, 1080,
            scaledresolution.getScaledWidth(), scaledresolution.getScaledHeight(), 1920, 1080);

        // Draw the progress bar
        drawProgress();

        // Unbind the width and height as it's no longer needed
        framebuffer.unbindFramebuffer();

        // Render the previously used frame buffer
        framebuffer.framebufferRender(scaledresolution.getScaledWidth() * scaleFactor, scaledresolution.getScaledHeight() * scaleFactor);

        // Update the texture to enable alpha drawing
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);

        // Update the users screen
        Minecraft.getMinecraft().updateDisplay();
    }

    /**
     * Render the progress bar and text
     */
    private static void drawProgress() {
        if (Minecraft.getMinecraft().gameSettings == null || Minecraft.getMinecraft().getTextureManager() == null)
            return;

        // Declare the font to be used
        if (raleway == null) raleway = new HyperiumFontRenderer("Raleway", 20);
        if (roboto == null) roboto = new HyperiumFontRenderer("Roboto", 20);

        // Get the users screen width and height to apply
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

        // Calculate the progress bar
        double nProgress = PROGRESS;
        double calc = (nProgress / DEFAULT_MAX) * sr.getScaledWidth();

        // Draw the transparent bar before the green bar
        Gui.drawRect(0, sr.getScaledHeight() - 35, sr.getScaledWidth(), sr.getScaledHeight(), new Color(0, 0, 0, 50).getRGB());

        // Draw the current splash text
        raleway.drawString(CURRENT, 20, sr.getScaledHeight() - 25, 0xffffffff);

        // Draw the current amount of progress / max amount of progress
        String s = PROGRESS + "/" + DEFAULT_MAX;
        roboto.drawString(s, sr.getScaledWidth() - 20 - roboto.getWidth(s), sr.getScaledHeight() - 25, 0xe1e1e1ff);

        // Render the blue progress bar
        Gui.drawRect(0, sr.getScaledHeight() - 2, (int) calc, sr.getScaledHeight(), new Color(3, 169, 244).getRGB());

        // Render the bar base
        Gui.drawRect(0, sr.getScaledHeight() - 2, sr.getScaledWidth(), sr.getScaledHeight(), new Color(0, 0, 0, 10).getRGB());
    }
}
