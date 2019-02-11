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
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.Color;

public class MouseButton extends IKey {

    private static final String[] BUTTONS = new String[]{"LMB", "RMB"};
    private final int button;
    private boolean wasPressed = true;
    private long lastPress = 0L;

    public MouseButton(KeystrokesMod mod, int button, int xOffset, int yOffset) {
        super(mod, xOffset, yOffset);
        this.button = button;
    }

    public int getButton() {
        return button;
    }

    @Override
    public void renderKey(int x, int y) {
        Mouse.poll();
        boolean pressed = Mouse.isButtonDown(this.button);
        String name = BUTTONS[this.button];
        if (pressed != this.wasPressed) {
            this.wasPressed = pressed;
            this.lastPress = System.currentTimeMillis();
        }

        int textColor = getColor();
        int pressedColor = getPressedColor();

        int color;
        double textBrightness;

        if (pressed) {
            color = Math.min(255, (int) ((this.mod.getSettings().getFadeTime() * 5) * (System.currentTimeMillis() - this.lastPress)));
            textBrightness = Math.max(0.0D, 1.0D - (double) (System.currentTimeMillis() - this.lastPress) / (this.mod.getSettings().getFadeTime() * 2));
        } else {
            color = Math.max(0, 255 - (int) ((this.mod.getSettings().getFadeTime() * 5) * (System.currentTimeMillis() - this.lastPress)));
            textBrightness = Math.min(1.0D, (double) (System.currentTimeMillis() - this.lastPress) / (this.mod.getSettings().getFadeTime() * 2));
        }

        Gui.drawRect(x + this.xOffset, y + this.yOffset, x + this.xOffset + 34, y + this.yOffset + 22, new Color(0, 0, 0, 120).getRGB() + (color << 16) + (color << 8) + color);

        int red = textColor >> 16 & 255;
        int green = textColor >> 8 & 255;
        int blue = textColor & 255;

        int colorN = new Color(0, 0, 0).getRGB() + ((int) ((double) red * textBrightness) << 16) + ((int) ((double) green * textBrightness) << 8) + (int) ((double) blue * textBrightness);

        if (this.mod.getSettings().isShowingCPSOnButtons()) {
            if (this.mod.getSettings().isChroma()) {
                drawChromaString(name, x + this.xOffset + 8, y + this.yOffset + 4);
                GL11.glPushMatrix();
                GL11.glScalef(0.5f, 0.5f, 0.0f);
                drawChromaString((name.equals(MouseButton.BUTTONS[0]) ? this.mod.getRenderer().getCPSKeys()[0].getLeftCPS() : this.mod.getRenderer().getCPSKeys()[0].getRightCPS()) + " CPS", Math.round(x / 0.5f + this.xOffset / 0.5f + 20.0f), Math.round(y / 0.5f + this.yOffset / 0.5f + 28.0f));
                GL11.glPopMatrix();
            } else {
                mc.fontRendererObj.drawString(name, x + this.xOffset + 8, y + this.yOffset + 4, pressed ? pressedColor : colorN);
                GL11.glPushMatrix();
                GL11.glScalef(0.5f, 0.5f, 0.0f);
                mc.fontRendererObj.drawString((name.equals(MouseButton.BUTTONS[0]) ? this.mod.getRenderer().getCPSKeys()[0].getLeftCPS() : this.mod.getRenderer().getCPSKeys()[0].getRightCPS()) + " CPS", Math.round(x / 0.5f + this.xOffset / 0.5f + 20.0f), Math.round(y / 0.5f + this.yOffset / 0.5f + 28.0f), pressed ? pressedColor : colorN);
                GL11.glPopMatrix();
            }
        } else {
            if (this.mod.getSettings().isChroma()) {
                drawChromaString(name, x + this.xOffset + 8, y + this.yOffset + 8);
            } else {
                this.mc.fontRendererObj.drawString(name, x + this.xOffset + 8, y + this.yOffset + 8, pressed ? pressedColor : colorN);
            }
        }
    }
}
