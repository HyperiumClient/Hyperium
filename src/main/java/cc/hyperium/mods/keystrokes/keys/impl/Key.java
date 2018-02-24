package cc.hyperium.mods.keystrokes.keys.impl;

import cc.hyperium.mods.keystrokes.utils.AntiReflection;
import cc.hyperium.mods.keystrokes.keys.IKey;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.settings.KeyBinding;

import org.lwjgl.input.Keyboard;

import java.awt.*;

public class Key extends IKey {

    @AntiReflection.HiddenField
    private final Minecraft mc = Minecraft.getMinecraft();

    @AntiReflection.HiddenField
    private final KeyBinding key;

    @AntiReflection.HiddenField
    private boolean wasPressed = true;

    @AntiReflection.HiddenField
    private long lastPress = 0L;

    public Key(KeyBinding key, int xOffset, int yOffset) {
        super(xOffset, yOffset);
        this.key = key;

        AntiReflection.filterClassMembers(getClass());
    }

    @AntiReflection.HiddenMethod
    @Override
    public void renderKey(int x, int y) {
        Keyboard.poll();
        boolean pressed = Keyboard.isKeyDown(this.key.getKeyCode());
        String name = Keyboard.getKeyName(this.key.getKeyCode());
        if (pressed != this.wasPressed) {
            this.wasPressed = pressed;
            this.lastPress = System.currentTimeMillis();
        }

        int textColor = getColor();
        int pressedColor = getPressedColor();

        double textBrightness;
        int color;

        if (pressed) {
            color = Math.min(255, (int) ((this.settings.getFadeTime() * 5) * (System.currentTimeMillis() - this.lastPress)));
            textBrightness = Math.max(0.0D, 1.0D - (double) (System.currentTimeMillis() - this.lastPress) / (this.settings.getFadeTime() * 5));
        } else {
            color = Math.max(0, 255 - (int) ((this.settings.getFadeTime() * 5) * (System.currentTimeMillis() - this.lastPress)));
            textBrightness = Math.min(1.0D, (double) (System.currentTimeMillis() - this.lastPress) / (this.settings.getFadeTime() * 5));
        }

        Gui.drawRect(x + this.xOffset, y + this.yOffset, x + this.xOffset + 22, y + this.yOffset + 22, new Color(0, 0, 0, 120).getRGB() + (color << 16) + (color << 8) + color);

        int red = textColor >> 16 & 255;
        int green = textColor >> 8 & 255;
        int blue = textColor & 255;

        int colorN = new Color(0, 0, 0).getRGB() + ((int) ((double) red * textBrightness) << 16) + ((int) ((double) green * textBrightness) << 8) + (int) ((double) blue * textBrightness);

        if (super.settings.isChroma()) {
            drawChromaString(name, x + this.xOffset + 8, y + this.yOffset + 8);
        } else {
            this.mc.fontRendererObj.drawString(name, x + this.xOffset + 8, y + this.yOffset + 8, pressed ? pressedColor : colorN);
        }
    }
}
