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
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class Key extends AbstractKey {

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
        return keyCode < 0 ? Mouse.isButtonDown(keyCode + 100) : Keyboard.isKeyDown(keyCode);
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

        if (mod.getSettings().isKeyBackgroundEnabled()) {
            if (mod.getSettings().getKeyBackgroundRed() == 0 && mod.getSettings().getKeyBackgroundGreen() == 0 && mod.getSettings().getKeyBackgroundBlue() == 0) {
                Gui.drawRect(x + xOffset, y + yOffset, x + xOffset + 22, y + yOffset + 22,
                    new Color(mod.getSettings().getKeyBackgroundRed(), mod.getSettings().getKeyBackgroundGreen(), mod.getSettings().getKeyBackgroundBlue(),
                        mod.getSettings().getKeyBackgroundOpacity()).getRGB() + (color << 16) + (color << 8) + color);
            } else {
                Gui.drawRect(x + xOffset, y + yOffset, x + xOffset + 22, y + yOffset + 22,
                    new Color(mod.getSettings().getKeyBackgroundRed(), mod.getSettings().getKeyBackgroundGreen(), mod.getSettings().getKeyBackgroundBlue(),
                        mod.getSettings().getKeyBackgroundOpacity()).getRGB());
            }
        }

        int keyWidth = 22;
        int red = textColor >> 16 & 0xFF;
        int green = textColor >> 8 & 0xFF;
        int blue = textColor & 0xFF;
        int colorN = new Color(0, 0, 0).getRGB() + ((int) (red * textBrightness) << 16) + ((int) (green * textBrightness) << 8) + (int) (blue * textBrightness);
        FontRenderer fontRenderer = mc.fontRendererObj;
        int stringWidth = fontRenderer.getStringWidth(name);
        float scaleFactor = 1.0f;

        if (stringWidth > keyWidth) scaleFactor = keyWidth / stringWidth;

        GlStateManager.pushMatrix();
        float xPos = (float) (x + xOffset + 8);
        float yPos = (float) (y + yOffset + 8);
        GlStateManager.scale(scaleFactor, scaleFactor, 1.0f);

        if (scaleFactor != 1.0f) {
            float scaleFactorRec = 1.0f / scaleFactor;
            xPos = (x + xOffset) * scaleFactorRec + 1.0f;
            yPos *= scaleFactorRec;
        } else if (name.length() > 1) {
            xPos -= stringWidth >> 2;
        }

        if (mod.getSettings().isUsingArrowKeys()) {
            double padding = 5;
            double bottom = y + yOffset + keyWidth - padding;
            double right = (x + xOffset) + keyWidth - padding;
            double left = x + xOffset + padding;
            double top = y + yOffset + padding;
            double centerX = (left + right) / 2D;
            double centerY = (top + bottom) / 2D;


            GlStateManager.translate(centerX, centerY, 0);
            Color baseColor = new Color(pressed ? pressedColor : colorN);
            Color topColor = baseColor;
            Color bottomLeftColor = baseColor;
            Color bottomRightColor = baseColor;

            if (mod.getSettings().isChroma()) { //Only applies to top for chroma, others are overridden
                topColor = getChromaColor(centerX, top, 1);
                bottomLeftColor = getChromaColor(left, bottom, 1);
                bottomRightColor = getChromaColor(right, bottom, 1);
            }

            int angle = 0;
            if (key == mc.gameSettings.keyBindLeft) {
                angle = -90;
                if (mod.getSettings().isChroma()) {
                    topColor = getChromaColor(centerX, centerY, 1);
                    bottomLeftColor = getChromaColor(left, bottom, 1);
                    bottomRightColor = getChromaColor(right, top, 1);
                }
            }

            if (key == mc.gameSettings.keyBindBack) {
                angle = -180;
                if (mod.getSettings().isChroma()) {
                    topColor = getChromaColor(centerX, bottom, 1);
                    bottomLeftColor = getChromaColor(right, top, 1);
                    bottomRightColor = getChromaColor(left, top, 1);
                }
            }

            if (key == mc.gameSettings.keyBindRight) {
                angle = 90;
                if (mod.getSettings().isChroma()) {
                    topColor = getChromaColor(right, centerY, 1);
                    bottomLeftColor = getChromaColor(left, top, 1);
                    bottomRightColor = getChromaColor(left, bottom, 1);
                }
            }

            GlStateManager.rotate(angle, 0, 0, 1);

            left -= centerX;
            right -= centerX;
            centerX = 0;
            top -= centerY;
            bottom -= centerY;

            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            GlStateManager.enableBlend();
            GlStateManager.disableTexture2D();
            GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
            GlStateManager.shadeModel(GL11.GL_SMOOTH);

            worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
            GlStateManager.color(1, 1, 1);
            worldrenderer.pos(centerX, top, 0.0D).color(topColor.getRed(), topColor.getGreen(), topColor.getBlue(), 255).endVertex();
            worldrenderer.pos(centerX, top, 0.0D).color(topColor.getRed(), topColor.getGreen(), topColor.getBlue(), 255).endVertex();
            worldrenderer.pos(left, bottom, 0.0D).color(bottomLeftColor.getRed(), bottomLeftColor.getGreen(), bottomLeftColor.getBlue(), 255).endVertex();
            worldrenderer.pos(right, bottom, 0.0D).color(bottomRightColor.getRed(), bottomRightColor.getGreen(), bottomLeftColor.getBlue(), 255).endVertex();
            tessellator.draw();

            GlStateManager.shadeModel(GL11.GL_FLAT);
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
        } else {
            if (mod.getSettings().isChroma()) {
                drawChromaString(name, (int) xPos, (int) yPos, 1.0);
            } else {
                mc.fontRendererObj.drawString(name, (int) xPos, (int) yPos, pressed ? pressedColor : colorN);
            }
        }

        GlStateManager.popMatrix();
    }
}
