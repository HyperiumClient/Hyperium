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
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

import java.awt.Color;

public class Key extends IKey {

    private final KeyBinding key;
    private boolean wasPressed = true;
    private long lastPress = 0L;

    public Key(KeystrokesMod mod, KeyBinding key, int xOffset, int yOffset) {
        super(mod, xOffset, yOffset);
        this.key = key;
    }

    private int isSelfDown() {
        try {
            return Keyboard.isKeyDown(this.key.getKeyCode()) ? 1 : 0;
        } catch (Exception e) {
            return -1;
        }
    }

    @Override
    public void renderKey(int x, int y) {
        Keyboard.poll();
        int pressed = isSelfDown();
        String name = Keyboard.getKeyName(this.key.getKeyCode());
        if ((pressed == 1) != this.wasPressed) {
            this.wasPressed = pressed == 1;
            this.lastPress = System.currentTimeMillis();
        }

        int textColor = getColor();
        int pressedColor = getPressedColor();

        double textBrightness;
        int color;

        if (pressed==1) {
            color = Math.min(255, (int) ((this.mod.getSettings().getFadeTime() * 5) * (System.currentTimeMillis() - this.lastPress)));
            textBrightness = Math.max(0.0D, 1.0D - (double) (System.currentTimeMillis() - this.lastPress) / (this.mod.getSettings().getFadeTime() * 5));
        } else if(pressed == 0){
            color = Math.max(0, 255 - (int) ((this.mod.getSettings().getFadeTime() * 5) * (System.currentTimeMillis() - this.lastPress)));
            textBrightness = Math.min(1.0D, (double) (System.currentTimeMillis() - this.lastPress) / (this.mod.getSettings().getFadeTime() * 5));
        } else {
            color = 255;
            textBrightness = Math.min(1.0D, (double) (System.currentTimeMillis() - this.lastPress) / (this.mod.getSettings().getFadeTime() * 5));

        }

        Gui.drawRect(x + this.xOffset, y + this.yOffset, x + this.xOffset + 22, y + this.yOffset + 22, new Color(0, 0, 0, 120).getRGB() + (color << 16) + (color << 8) + color);
        int keyWidth = 22;

        int red = textColor >> 16 & 255;
        int green = textColor >> 8 & 255;
        int blue = textColor & 255;

        int colorN = new Color(0, 0, 0).getRGB() + ((int) ((double) red * textBrightness) << 16) + ((int) ((double) green * textBrightness) << 8) + (int) ((double) blue * textBrightness);

        FontRenderer fontRenderer = this.mc.fontRendererObj;
        int stringWidth = fontRenderer.getStringWidth(name);
        float scaleFactor = 1.0F;

        // Check if text will overflow outside of the box.
        if (stringWidth > keyWidth) {
            scaleFactor = (float) keyWidth / stringWidth;
        }

        GlStateManager.pushMatrix();

        float xPos = (x + this.xOffset + 8);
        float yPos = (y + this.yOffset + 8);

        GlStateManager.scale(scaleFactor, scaleFactor, 1.0F);


        if (scaleFactor != 1.0F) {
            float scaleFactorRec = 1 / scaleFactor;

            // Text has been scaled down to fit the box so draw at start of box.
            xPos = ((x + this.xOffset) * scaleFactorRec) + 1;

            // Scale Y value.
            yPos *= scaleFactorRec;

        } else if (name.length() > 1) {
            // Centres text if it fits in the box but is longer than one character.
            xPos -= stringWidth / 4;
        }

        if (this.mod.getSettings().isChroma()) {
            drawChromaString(name, (int) xPos, (int) yPos);
        } else {
            this.mc.fontRendererObj.drawString(name, (int) xPos, (int) yPos, pressed ==1 ? pressedColor : (pressed == 0 ?colorN : Color.RED.getRGB()));
        }
        GlStateManager.popMatrix();
    }
}
