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

package cc.hyperium.mods.keystrokes.keys;

import cc.hyperium.mods.keystrokes.KeystrokesMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.*;

/**
 * Used as the base class for all keys with a few essential methods and fields
 *
 * @author boomboompower
 */
public abstract class AbstractKey extends Gui {

    protected final Minecraft mc = Minecraft.getMinecraft();
    protected final KeystrokesMod mod;

    protected final int xOffset;
    protected final int yOffset;

    public AbstractKey(KeystrokesMod mod, int xOffset, int yOffset) {
        this.mod = mod;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    protected Color getChromaColor(double x, double y, double offsetScale) {
        float v = 2000F;
        return new Color(Color.HSBtoRGB((float) ((System.currentTimeMillis() - (x * 10) * offsetScale - (y * 10) * offsetScale) % v) / v, 0.8F, 0.8F));
    }

    protected void drawChromaString(String text, int x, int y, double offsetScale) {
        FontRenderer renderer = Minecraft.getMinecraft().fontRendererObj;
        for (char c : text.toCharArray()) {
            int i = getChromaColor(x, y, offsetScale).getRGB();
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
     * Gets the color of the text whilst the key is not being pressed
     * <p>
     * if chroma this will return the current generated chroma color
     *
     * @return the color from settings or chroma if its enabled
     */
    protected final int getColor() {
        return mod.getSettings().isChroma() ? Color.HSBtoRGB((float) ((System.currentTimeMillis() - (xOffset * 10) - (yOffset * 10)) % 2000) / 2000.0F,
            0.8F, 0.8F) : new Color(mod.getSettings().getRed(), mod.getSettings().getGreen(), mod.getSettings().getBlue()).getRGB();
    }

    /**
     * Gets the color of the text whilst the key is being pressed
     * <p>
     * this will not be used if chroma is enabled
     *
     * @return the color from settings or chroma if its enabled
     */
    protected final int getPressedColor() {
        return mod.getSettings().isChroma() ? new Color(0, 0, 0).getRGB() : new Color(mod.getSettings().getPressedRed(), mod.getSettings().getPressedGreen(),
            mod.getSettings().getPressedBlue()).getRGB();
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
        mc.fontRendererObj.drawString(text, (float) (x - mc.fontRendererObj.getStringWidth(text) / 2), (float) y, color, false);
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
                if (openGLName.equalsIgnoreCase("button0")) return "LMB";
                if (openGLName.equalsIgnoreCase("button1")) return "RMB";
            }

            return openGLName;
        }

        if (mod.getSettings().isUsingLiteralKeys()) {
            switch (keyCode) {
                case Keyboard.KEY_GRAVE:
                    return "~";
                case Keyboard.KEY_MINUS:
                case Keyboard.KEY_SUBTRACT:
                    return "-";
                case Keyboard.KEY_APOSTROPHE:
                    return "'";
                case Keyboard.KEY_LBRACKET:
                    return "[";
                case Keyboard.KEY_RBRACKET:
                    return "]";
                case Keyboard.KEY_BACKSLASH:
                    return "\\";
                case Keyboard.KEY_DIVIDE:
                case Keyboard.KEY_SLASH:
                    return "/";
                case Keyboard.KEY_COMMA:
                    return ",";
                case Keyboard.KEY_PERIOD:
                    return ".";
                case Keyboard.KEY_SEMICOLON:
                    return ";";
                case Keyboard.KEY_EQUALS:
                    return "=";
                case Keyboard.KEY_UP:
                    return "▲";
                case Keyboard.KEY_DOWN:
                    return "▼";
                case Keyboard.KEY_LEFT:
                    return "◀";
                case Keyboard.KEY_RIGHT:
                    return "▶";
                case Keyboard.KEY_MULTIPLY:
                    return "*";
                case Keyboard.KEY_ADD:
                    return "+";
            }
        }

        return Keyboard.getKeyName(keyCode);
    }
}
