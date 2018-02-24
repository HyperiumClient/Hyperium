package cc.hyperium.mods.keystrokes.keys.impl;

import cc.hyperium.mods.keystrokes.utils.AntiReflection;
import cc.hyperium.mods.keystrokes.utils.boom.ChatColor;
import cc.hyperium.mods.keystrokes.KeystrokesMod;
import cc.hyperium.mods.keystrokes.keys.IKey;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.settings.KeyBinding;

import org.lwjgl.input.Keyboard;

import java.awt.*;

public class SpaceKey extends IKey {

    @AntiReflection.HiddenField
    private final Minecraft mc = Minecraft.getMinecraft();

    @AntiReflection.HiddenField
    private final KeyBinding key;

    @AntiReflection.HiddenField
    private boolean wasPressed = true;

    @AntiReflection.HiddenField
    private long lastPress = 0L;

    public SpaceKey(KeyBinding key, int xOffset, int yOffset) {
        super(xOffset, yOffset);
        this.key = key;

        AntiReflection.filterClassMembers(getClass());
    }

    @AntiReflection.HiddenMethod
    @Override
    public void renderKey(int x, int y) {
        int yOffset = this.yOffset;

        if (!KeystrokesMod.getSettings().isShowingMouseButtons()) {
            yOffset -= 24;
        }

        Keyboard.poll();
        boolean pressed = Keyboard.isKeyDown(this.key.getKeyCode());
        String name = (!this.settings.isChroma() ? ChatColor.STRIKETHROUGH.toString() + "-----" : "------");

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
            textBrightness = Math.max(0.0D, 1.0D - (double) (System.currentTimeMillis() - this.lastPress) / (this.settings.getFadeTime() * 2));
        } else {
            color = Math.max(0, 255 - (int) ((this.settings.getFadeTime() * 5) * (System.currentTimeMillis() - this.lastPress)));
            textBrightness = Math.min(1.0D, (double) (System.currentTimeMillis() - this.lastPress) / (this.settings.getFadeTime() * 2));
        }

        Gui.drawRect(x + this.xOffset, y + yOffset, x + this.xOffset + 70, y + yOffset + 16, new Color(0, 0, 0, 120).getRGB() + (color << 16) + (color << 8) + color);

        int red = textColor >> 16 & 255;
        int green = textColor >> 8 & 255;
        int blue = textColor & 255;

        int pressedRed = pressedColor >> 16 & 255;
        int pressedGreen = pressedColor >> 8 & 255;
        int pressedBlue = pressedColor & 255;

        int colorN = new Color(0, 0, 0).getRGB() + ((int) ((double) red * textBrightness) << 16) + ((int) ((double) green * textBrightness) << 8) + (int) ((double) blue * textBrightness);

        if (super.settings.isChroma()) {
            drawSpacebar(name, x + ((this.xOffset + 76) / 2), y + yOffset + 5);
        } else {
            drawCenteredString(name, x + ((this.xOffset + 70) / 2), y + yOffset + 5, pressed ? pressedColor : colorN);
        }
    }

    @AntiReflection.HiddenMethod
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