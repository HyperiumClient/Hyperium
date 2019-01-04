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

package cc.hyperium.mods.keystrokes.keys.impl;

import cc.hyperium.mods.keystrokes.KeystrokesMod;
import cc.hyperium.mods.keystrokes.keys.IKey;
import cc.hyperium.utils.ChatColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.Color;

public class SpaceKey extends IKey {

    private final KeyBinding key;

    private boolean wasPressed = true;

    private long lastPress = 0L;

    public SpaceKey(KeystrokesMod mod, KeyBinding key, int xOffset, int yOffset) {
        super(mod, xOffset, yOffset);
        this.key = key;
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

        Keyboard.poll();
        boolean pressed = isButtonDown(this.key.getKeyCode());
        String name = (!this.mod.getSettings().isChroma() ? ChatColor.STRIKETHROUGH.toString() + "-----" : "------");

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

        Gui.drawRect(x + this.xOffset, y + yOffset, x + this.xOffset + 70, y + yOffset + 16, new Color(0, 0, 0, 120).getRGB() + (color << 16) + (color << 8) + color);

        int red = textColor >> 16 & 255;
        int green = textColor >> 8 & 255;
        int blue = textColor & 255;

        int pressedRed = pressedColor >> 16 & 255;
        int pressedGreen = pressedColor >> 8 & 255;
        int pressedBlue = pressedColor & 255;

        int colorN = new Color(0, 0, 0).getRGB() + ((int) ((double) red * textBrightness) << 16) + ((int) ((double) green * textBrightness) << 8) + (int) ((double) blue * textBrightness);

        if (this.mod.getSettings().isChroma()) {
            drawSpacebar(name, x + ((this.xOffset + 76) / 2), y + yOffset + 5);
        } else {
            drawCenteredString(name, x + ((this.xOffset + 70) / 2), y + yOffset + 5, pressed ? pressedColor : colorN);
        }
    }

    private void drawSpacebar(String text, int xIn, int y) {
        FontRenderer renderer = Minecraft.getMinecraft().fontRendererObj;
        int x = xIn - (Minecraft.getMinecraft().fontRendererObj.getStringWidth(text) / 2);
        for (char c : text.toCharArray()) {
            int i = Color.HSBtoRGB((float) ((System.currentTimeMillis() - (x * 10) - (y * 10)) % 2000) / 2000.0F, 0.8F, 0.8F);
            renderer.drawString(String.valueOf(c), x, y, i);
            x += renderer.getCharWidth(c) - 1;
        }
    }
}
