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

package cc.hyperium.mods.keystrokes.keys;

import cc.hyperium.mods.keystrokes.KeystrokesMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.Color;

/**
 * Used as the base class for all keys with a few essential methods and fields
 *
 * @author boomboompower
 */
public abstract class IKey extends Gui {

    protected final Minecraft mc = Minecraft.getMinecraft();
    protected final KeystrokesMod mod;

    protected final int xOffset;
    protected final int yOffset;

    public IKey(KeystrokesMod mod, int xOffset, int yOffset) {
        this.mod = mod;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    protected void drawChromaString(String text, int x, int y) {
        FontRenderer renderer = Minecraft.getMinecraft().fontRendererObj;
        for (char c : text.toCharArray()) {
            int i = Color.HSBtoRGB((float) ((System.currentTimeMillis() - (x * 10) - (y * 10)) % 2000) / 2000.0F, 0.8F, 0.8F);
            String tmp = String.valueOf(c);
            renderer.drawString(tmp, x, y, i);
            x += renderer.getStringWidth(tmp);
        }
    }

    /**
     * Renders the key at the specified x and y location
     */
    protected abstract void renderKey(int x, int y);

    /**
     * Gets the x offset of the key
     *
     * @return x offset of the key
     */
    protected final int getXOffset() {
        return this.xOffset;
    }

    /**
     * Gets the y offset of the key
     *
     * @return y offset
     */
    protected final int getYOffset() {
        return this.yOffset;
    }

    /**
     * Gets the color of the text whilst the key is not being pressed
     * <p>
     * if chroma this will return the current generated chroma color
     *
     * @return the color from settings or chroma if its enabled
     */
    protected final int getColor() {
        return this.mod.getSettings().isChroma() ? Color.HSBtoRGB((float) ((System.currentTimeMillis() - (getXOffset() * 10) - (getYOffset() * 10)) % 2000) / 2000.0F, 0.8F, 0.8F) : new Color(this.mod.getSettings().getRed(), this.mod.getSettings().getGreen(), this.mod.getSettings().getBlue()).getRGB();
    }

    /**
     * Gets the color of the text whilst the key is being pressed
     * <p>
     * this will not be used if chroma is enabled
     *
     * @return the color from settings or chroma if its enabled
     */
    public final int getPressedColor() {
        return this.mod.getSettings().isChroma() ? new Color(0, 0, 0).getRGB() : new Color(this.mod.getSettings().getPressedRed(), this.mod.getSettings().getPressedGreen(), this.mod.getSettings().getPressedBlue()).getRGB();
    }

    /**
     * Draws a centered string without a background shadow at the specified location
     * with the given color
     *
     * @param text  text to draw
     * @param x     the x position for the text
     * @param y     the y position for the text
     * @param color the texts color
     */
    protected final void drawCenteredString(String text, int x, int y, int color) {
        this.mc.fontRendererObj.drawString(text, (float) (x - this.mc.fontRendererObj.getStringWidth(text) / 2), (float) y, color, false);
    }

    /**
     * Get the name of the key being set for CustomKey
     *
     * @param keyCode opengl name of the key being pressed
     * @return the name of the key
     */
    protected String getKeyOrMouseName(int keyCode) {
        if (keyCode < 0) {
            String openGLName = Mouse.getButtonName(keyCode + 100);
            if (openGLName != null) {
                if (openGLName.equalsIgnoreCase("button0")) {
                    return "LMB";
                }
                if (openGLName.equalsIgnoreCase("button1")) {
                    return "RMB";
                }
            }
            return openGLName;
        }
        return Keyboard.getKeyName(keyCode);
    }
}
