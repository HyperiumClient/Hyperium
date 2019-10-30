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
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class MouseButton extends AbstractKey {

    private static final String[] BUTTONS = new String[]{"LMB", "RMB"};
    private final int button;
    private boolean wasPressed = true;
    private long lastPress;

    public MouseButton(KeystrokesMod mod, int button, int xOffset, int yOffset) {
        super(mod, xOffset, yOffset);
        this.button = button;
    }

    public int getButton() {
        return button;
    }

    @Override
    public void renderKey(int x, int y) {
        int yOffset = this.yOffset;

        Mouse.poll();
        boolean pressed = Mouse.isButtonDown(button);

        if (!mod.getSettings().isShowingWASD()) yOffset -= 48;

        String name = BUTTONS[button];
        if (pressed != wasPressed) {
            wasPressed = pressed;
            lastPress = System.currentTimeMillis();
        }

        int textColor = getColor();
        int pressedColor = getPressedColor();

        int color;
        double textBrightness;

        if (pressed) {
            color = Math.min(255, (int) ((mod.getSettings().getFadeTime() * 5) * (System.currentTimeMillis() - lastPress)));
            textBrightness = Math.max(0.0D, 1.0D - (double) (System.currentTimeMillis() - lastPress) / (mod.getSettings().getFadeTime() * 2));
        } else {
            color = Math.max(0, 255 - (int) ((mod.getSettings().getFadeTime() * 5) * (System.currentTimeMillis() - lastPress)));
            textBrightness = Math.min(1.0D, (double) (System.currentTimeMillis() - lastPress) / (mod.getSettings().getFadeTime() * 2));
        }

        if (mod.getSettings().isKeyBackgroundEnabled()) {
            if (mod.getSettings().getKeyBackgroundRed() == 0 && mod.getSettings().getKeyBackgroundGreen() == 0 && mod.getSettings().getKeyBackgroundBlue() == 0) {
                Gui.drawRect(x + xOffset, y + yOffset, x + xOffset + 34, y + yOffset + 22,
                    new Color(mod.getSettings().getKeyBackgroundRed(), mod.getSettings().getKeyBackgroundGreen(), mod.getSettings().getKeyBackgroundBlue(),
                        mod.getSettings().getKeyBackgroundOpacity()).getRGB() + (color << 16) + (color << 8) + color);
            } else {
                Gui.drawRect(x + xOffset, y + yOffset, x + xOffset + 34, y + yOffset + 22,
                    new Color(mod.getSettings().getKeyBackgroundRed(), mod.getSettings().getKeyBackgroundGreen(), mod.getSettings().getKeyBackgroundBlue(),
                        mod.getSettings().getKeyBackgroundOpacity()).getRGB());
            }
        }

        int red = textColor >> 16 & 255;
        int green = textColor >> 8 & 255;
        int blue = textColor & 255;

        int colorN = new Color(0, 0, 0).getRGB() + ((int) ((double) red * textBrightness) << 16) + ((int) ((double) green * textBrightness) << 8) + (int) ((double) blue * textBrightness);

        if (mod.getSettings().isShowingCPSOnButtons() && mod.getSettings().isShowingCPS()) {
            final int round = Math.round(y / 0.5f + yOffset / 0.5f + 28.0f);
            if (mod.getSettings().isChroma()) {
                drawChromaString(name, x + xOffset + 8, y + yOffset + 4, 1.0F);
                GL11.glPushMatrix();
                GL11.glScalef(0.5f, 0.5f, 0.0f);
                drawChromaString((name.equals(BUTTONS[0]) ? mod.getRenderer().getCPSKeys()[0].getLeftCPS() :
                    mod.getRenderer().getCPSKeys()[0].getRightCPS()) + " CPS", Math.round(x / 0.5f + xOffset / 0.5f + 10 / 0.5f), round, .5);
            } else {
                mc.fontRendererObj.drawString(name, x + xOffset + 8, y + yOffset + 4, pressed ? pressedColor : colorN);
                GL11.glPushMatrix();
                GL11.glScalef(0.5f, 0.5f, 0.0f);
                mc.fontRendererObj.drawString((name.equals(MouseButton.BUTTONS[0]) ? mod.getRenderer().getCPSKeys()[0].getLeftCPS() :
                    mod.getRenderer().getCPSKeys()[0].getRightCPS()) + " CPS", Math.round(x / 0.5f + xOffset / 0.5f + 20.0f), round, pressed ? pressedColor : colorN);
            }
            GL11.glPopMatrix();
        } else {
            if (mod.getSettings().isChroma()) {
                drawChromaString(name, x + xOffset + 8, y + yOffset + 8, 1.0F);
            } else {
                mc.fontRendererObj.drawString(name, x + xOffset + 8, y + yOffset + 8, pressed ? pressedColor : colorN);
            }
        }
    }
}
