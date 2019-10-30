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

import cc.hyperium.gui.GuiBlock;
import cc.hyperium.mods.keystrokes.KeystrokesMod;
import cc.hyperium.mods.keystrokes.keys.AbstractKey;
import cc.hyperium.utils.ChatColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.*;

public class CustomKey extends AbstractKey {

    private int key;
    private boolean wasPressed;
    private long lastPress;
    private int type;
    private GuiBlock hitbox;

    public CustomKey(KeystrokesMod mod, int key, int type) {
        super(mod, 0, 0);
        wasPressed = true;
        lastPress = 0L;
        hitbox = new GuiBlock(0, 0, 0, 0);
        this.key = key;
        this.type = type;
    }

    @Override
    public void renderKey(int x, int y) {
        Keyboard.poll();
        boolean pressed = isButtonDown(key);
        String name = (type == 0) ? (mod.getSettings().isChroma() ? "------" : (ChatColor.STRIKETHROUGH.toString() + "------")) : getKeyOrMouseName(key);

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
            textBrightness = Math.max(0.0, 1.0 - (System.currentTimeMillis() - lastPress) / (mod.getSettings().getFadeTime() * 2.0));
        } else {
            color = Math.max(0, 255 - (int) (mod.getSettings().getFadeTime() * 5.0 * (System.currentTimeMillis() - lastPress)));
            textBrightness = Math.min(1.0, (System.currentTimeMillis() - lastPress) / (mod.getSettings().getFadeTime() * 2.0));
        }

        int left = x + xOffset;
        int top = y + yOffset;
        int right;
        int bottom;

        if (type == 0 || type == 1) {
            right = x + xOffset + 70;
            bottom = y + yOffset + 16;
        } else {
            right = x + xOffset + 22;
            bottom = y + yOffset + 22;
        }

        if (mod.getSettings().isKeyBackgroundEnabled()) {
            if (mod.getSettings().getKeyBackgroundRed() == 0 && mod.getSettings().getKeyBackgroundGreen() == 0 && mod.getSettings().getKeyBackgroundBlue() == 0) {
                Gui.drawRect(left, top, right, bottom,
                    new Color(mod.getSettings().getKeyBackgroundRed(), mod.getSettings().getKeyBackgroundGreen(), mod.getSettings().getKeyBackgroundBlue(),
                        mod.getSettings().getKeyBackgroundOpacity()).getRGB() + (color << 16) + (color << 8) + color);
            } else {
                Gui.drawRect(left, top, right, bottom,
                    new Color(mod.getSettings().getKeyBackgroundRed(), mod.getSettings().getKeyBackgroundGreen(), mod.getSettings().getKeyBackgroundBlue(),
                        mod.getSettings().getKeyBackgroundOpacity()).getRGB());
            }
        }

        hitbox.setLeft(left);
        hitbox.setTop(top);
        hitbox.setRight(right);
        hitbox.setBottom(bottom);

        int red = textColor >> 16 & 0xFF;
        int green = textColor >> 8 & 0xFF;
        int blue = textColor & 0xFF;
        int colorN = new Color(0, 0, 0).getRGB() + ((int) (red * textBrightness) << 16) + ((int) (green * textBrightness) << 8) + (int) (blue * textBrightness);
        final float yPos = y + yOffset + 8;
        final FontRenderer fontRendererObj = Minecraft.getMinecraft().fontRendererObj;
        if (mod.getSettings().isChroma()) {
            if (type == 0) {
                int xIn = x + (xOffset + 76) / 4;
                int y2 = y + yOffset + 9;
                GlStateManager.pushMatrix();
                GlStateManager.translate((float) xIn, (float) y2, 0.0f);
                GlStateManager.rotate(-90.0f, 0.0f, 0.0f, 1.0f);
                drawGradientRect(0, 0, 2, 35, Color.HSBtoRGB((System.currentTimeMillis() - xIn * 10 - y2 * 10) %
                    2000L / 2000.0f, 0.8f, 0.8f), Color.HSBtoRGB((System.currentTimeMillis() - (xIn + 35) * 10 - y2 * 10) %
                    2000L / 2000.0f, 0.8f, 0.8f));
                GlStateManager.popMatrix();
            } else if (type == 1) {
                drawChromaString(name, x + ((xOffset + 70) / 2) - fontRendererObj.getStringWidth(name) / 2, y + yOffset + 5, 1.0F);
            } else {
                drawChromaString(name, (left + right) / 2 - fontRendererObj.getStringWidth(name) / 2, (int) yPos, 1.0);
            }
        } else if (type == 0 || type == 1) {
            drawCenteredString(name, x + (xOffset + 70) / 2, y + yOffset + 5, pressed ? pressedColor : colorN);
        } else {
            mc.fontRendererObj.drawString(name, (left + right) / 2 - fontRendererObj.getStringWidth(name) / 2, (int) yPos, pressed ? pressedColor : colorN);
        }
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public GuiBlock getHitbox() {
        return hitbox;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    private boolean isButtonDown(int buttonCode) {
        return buttonCode < 0 ? Mouse.isButtonDown(buttonCode + 100) : buttonCode > 0 && Keyboard.isKeyDown(buttonCode);
    }
}
