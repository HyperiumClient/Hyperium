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

import java.awt.Color;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class Key extends IKey {

    private final KeyBinding key;
    private boolean wasPressed;
    private long lastPress;

    public Key(KeystrokesMod mod, KeyBinding key, int xOffset, int yOffset) {
        super(mod, xOffset, yOffset);
        wasPressed = true;
        lastPress = 0L;
        this.key = key;
    }

    private boolean isKeyOrMouseDown(int keyCode) {
        if (keyCode < 0) {
            return Mouse.isButtonDown(keyCode + 100);
        }

        return Keyboard.isKeyDown(keyCode);
    }

    @Override
    public void renderKey(int x, int y) {
        Keyboard.poll();
        boolean pressed = isKeyOrMouseDown(key.getKeyCode());
        String name = getKeyOrMouseName(key.getKeyCode());

        if (pressed != wasPressed) {
            wasPressed = pressed;
            lastPress = System.currentTimeMillis();
        }

        int textColor = getColor();
        int pressedColor = getPressedColor();
        int color;
        double textBrightness;

        if (pressed) {
            color = Math.min(255, (int) (mod.getSettings().getFadeTime() * 5.0 * (System.currentTimeMillis() - lastPress)));
            textBrightness = Math.max(0.0, 1.0 - (System.currentTimeMillis() - lastPress) / (mod.getSettings().getFadeTime() * 5.0));
        } else {
            color = Math.max(0, 255 - (int) (mod.getSettings().getFadeTime() * 5.0 * (System.currentTimeMillis() - lastPress)));
            textBrightness = Math.min(1.0, (double) (System.currentTimeMillis() - lastPress) / (mod.getSettings().getFadeTime() * 5.0));
        }

        Gui.drawRect(x + xOffset, y + yOffset, x + xOffset + 22, y + yOffset + 22, new Color(0, 0, 0, 120).getRGB() + (color << 16) + (color << 8) + color);

        int keyWidth = 22;
        int red = textColor >> 16 & 0xFF;
        int green = textColor >> 8 & 0xFF;
        int blue = textColor & 0xFF;
        int colorN = new Color(0, 0, 0).getRGB() + ((int) (red * textBrightness) << 16) + ((int) (green * textBrightness) << 8) + (int) (blue * textBrightness);
        FontRenderer fontRenderer = mc.fontRendererObj;
        int stringWidth = fontRenderer.getStringWidth(name);
        float scaleFactor = 1.0f;

        if (stringWidth > keyWidth) {
            scaleFactor = keyWidth / stringWidth;
        }

        GlStateManager.pushMatrix();
        float xPos = (float) (x + xOffset + 8);
        float yPos = (float) (y + yOffset + 8);
        GlStateManager.scale(scaleFactor, scaleFactor, 1.0f);

        if (scaleFactor != 1.0f) {
            float scaleFactorRec = 1.0f / scaleFactor;
            xPos = (x + xOffset) * scaleFactorRec + 1.0f;
            yPos *= scaleFactorRec;
        } else if (name.length() > 1) {
            xPos -= stringWidth / 4;
        }

        if (mod.getSettings().isChroma()) {
            drawChromaString(name, (int) xPos, (int) yPos);
        } else {
            mc.fontRendererObj.drawString(name, (int) xPos, (int) yPos, pressed ? pressedColor : colorN);
        }

        GlStateManager.popMatrix();
    }
}
