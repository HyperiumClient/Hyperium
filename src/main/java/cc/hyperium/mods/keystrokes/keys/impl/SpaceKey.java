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

package cc.hyperium.mods.keystrokes.keys.impl;

import cc.hyperium.mods.keystrokes.KeystrokesMod;
import cc.hyperium.mods.keystrokes.keys.AbstractKey;
import cc.hyperium.utils.ChatColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.*;

public class SpaceKey extends AbstractKey {

    private final KeyBinding key;

    private boolean wasPressed = true;

    private long lastPress;

    private String name;

    public SpaceKey(KeystrokesMod mod, KeyBinding key, int xOffset, int yOffset, String name) {
        super(mod, xOffset, yOffset);
        this.key = key;
        this.name = name;
    }

    private boolean isButtonDown(int buttonCode) {
        if (buttonCode < 0) {
            // Mouse bind.
            return Mouse.isButtonDown(buttonCode + 100);
        } else if (buttonCode > 0) {
            // Key bind.
            return Keyboard.isKeyDown(buttonCode);
        }

        return false;
    }

    @Override
    public void renderKey(int x, int y) {
        int yOffset = this.yOffset;

        if (!this.mod.getSettings().isShowingMouseButtons()) {
            yOffset -= 24;
        }

        if (!this.mod.getSettings().isShowingSneak()) {
            yOffset -= 18;
        }

        if (!mod.getSettings().isShowingWASD()) {
            yOffset -= 48;
        }

        Keyboard.poll();
        boolean pressed = isButtonDown(this.key.getKeyCode());
        String name = this.name.equalsIgnoreCase("space") ? (mod.getSettings().isChroma() ? "------" : (ChatColor.STRIKETHROUGH.toString() + "------")) : "Sneak";

        if (pressed != this.wasPressed) {
            this.wasPressed = pressed;
            this.lastPress = System.currentTimeMillis();
        }

        int textColor = getColor();
        int pressedColor = getPressedColor();

        double textBrightness;
        int color;

        if (pressed) {
            color = Math.min(255, (int) ((this.mod.getSettings().getFadeTime() * 5) * (System.currentTimeMillis() - this.lastPress)));
            textBrightness = Math.max(0.0D, 1.0D - (double) (System.currentTimeMillis() - this.lastPress) / (this.mod.getSettings().getFadeTime() * 2));
        } else {
            color = Math.max(0, 255 - (int) ((this.mod.getSettings().getFadeTime() * 5) * (System.currentTimeMillis() - this.lastPress)));
            textBrightness = Math.min(1.0D, (double) (System.currentTimeMillis() - this.lastPress) / (this.mod.getSettings().getFadeTime() * 2));
        }

        if (mod.getSettings().isKeyBackgroundEnabled()) {
            if (mod.getSettings().getKeyBackgroundRed() == 0 && mod.getSettings().getKeyBackgroundGreen() == 0 && mod.getSettings().getKeyBackgroundBlue() == 0) {
                Gui.drawRect(x + this.xOffset, y + yOffset, x + this.xOffset + 70, y + yOffset + 16,
                    new Color(mod.getSettings().getKeyBackgroundRed(), mod.getSettings().getKeyBackgroundGreen(), mod.getSettings().getKeyBackgroundBlue(),
                        mod.getSettings().getKeyBackgroundOpacity()).getRGB() + (color << 16) + (color << 8) + color);
            } else {
                Gui.drawRect(x + this.xOffset, y + yOffset, x + this.xOffset + 70, y + yOffset + 16,
                    new Color(mod.getSettings().getKeyBackgroundRed(), mod.getSettings().getKeyBackgroundGreen(), mod.getSettings().getKeyBackgroundBlue(),
                        mod.getSettings().getKeyBackgroundOpacity()).getRGB());
            }
        }

        int red = textColor >> 16 & 255;
        int green = textColor >> 8 & 255;
        int blue = textColor & 255;

        int colorN = new Color(0, 0, 0).getRGB() + ((int) ((double) red * textBrightness) << 16) + ((int) ((double) green * textBrightness) << 8) + (int) ((double) blue * textBrightness);

        if (this.mod.getSettings().isChroma()) {
            if (this.name.equalsIgnoreCase("space")) {
                int xIn = x + (this.xOffset + 76) / 4;
                int y2 = y + yOffset + 9;
                GlStateManager.pushMatrix();
                GlStateManager.translate((float) xIn, (float) y2, 0.0f);
                GlStateManager.rotate(-90.0f, 0.0f, 0.0f, 1.0f);
                this.drawGradientRect(0, 0, 2, 35, Color.HSBtoRGB((float) ((System.currentTimeMillis() - xIn * 10 - y2 * 10) % 2000L) / 2000.0f,
                    0.8f, 0.8f), Color.HSBtoRGB((float) ((System.currentTimeMillis() - (xIn + 35) * 10 - y2 * 10) % 2000L) / 2000.0f, 0.8f, 0.8f));
                GlStateManager.popMatrix();
            } else {
                drawChromaString(name, x + ((this.xOffset + 70) / 2) - Minecraft.getMinecraft().fontRendererObj.getStringWidth(name) / 2, y + yOffset + 5, 1.0F);
            }
        } else {
            this.drawCenteredString(name, x + (this.xOffset + 70) / 2, y + yOffset + 5, pressed ? pressedColor : colorN);
        }
    }
}
