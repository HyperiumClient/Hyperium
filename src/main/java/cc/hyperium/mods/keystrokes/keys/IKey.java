package cc.hyperium.mods.keystrokes.keys;

import cc.hyperium.mods.keystrokes.KeystrokesMod;
import cc.hyperium.mods.keystrokes.config.KeystrokesSettings;
import cc.hyperium.mods.keystrokes.utils.AntiReflection;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import java.awt.*;

/**
 * Used as the base class for all keys with a few essential methods and fields
 *
 * @author boomboompower
 */
public abstract class IKey {

    @AntiReflection.HiddenField
    protected final int xOffset;

    @AntiReflection.HiddenField
    protected final int yOffset;

    @AntiReflection.HiddenField
    protected KeystrokesSettings settings = KeystrokesMod.getSettings();

    @AntiReflection.HiddenField
    protected Minecraft mc = Minecraft.getMinecraft();

    public IKey(int xOffset, int yOffset) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;

        AntiReflection.filterClassMembers(getClass());
    }

    @AntiReflection.HiddenMethod
    protected void drawChromaString(String text, int x, int y) {
        FontRenderer renderer = Minecraft.getMinecraft().fontRendererObj;
        for (char c : text.toCharArray()) {
            int i = Color.HSBtoRGB((float) ((System.currentTimeMillis() - (x * 10) - (y * 10)) % 2000) / 2000.0F, 0.8F, 0.8F);
            String tmp = String.valueOf(c);
            renderer.drawString(tmp, x, y, i);
            x += renderer.getStringWidth(tmp);
        }
    }

    /**
     * Renders the key at the specified x and y location
     */
    @AntiReflection.HiddenMethod
    protected abstract void renderKey(int x, int y);

    /**
     * Gets the x offset of the key
     *
     * @return x offset of the key
     */
    @AntiReflection.HiddenMethod
    protected final int getXOffset() {
        return this.xOffset;
    }

    /**
     * Gets the y offset of the key
     *
     * @return y offset
     */
    @AntiReflection.HiddenMethod
    protected final int getYOffset() {
        return this.yOffset;
    }

    /**
     * Gets the color of the text whilst the key is not being pressed
     * <p>
     * if chroma this will return the current generated chroma color
     *
     * @return the color from settings or chroma if its enabled
     */
    @AntiReflection.HiddenMethod
    protected final int getColor() {
        return this.settings.isChroma() ? Color.HSBtoRGB((float) ((System.currentTimeMillis() - (getXOffset() * 10) - (getYOffset() * 10)) % 2000) / 2000.0F, 0.8F, 0.8F) : new Color(this.settings.getRed(), this.settings.getGreen(), this.settings.getBlue()).getRGB();
    }

    /**
     * Gets the color of the text whilst the key is being pressed
     * <p>
     * this will not be used if chroma is enabled
     *
     * @return the color from settings or chroma if its enabled
     */
    @AntiReflection.HiddenMethod
    public final int getPressedColor() {
        return this.settings.isChroma() ? new Color(0, 0, 0).getRGB() : new Color(this.settings.getPressedRed(), this.settings.getPressedGreen(), this.settings.getPressedBlue()).getRGB();
    }

    /**
     * Draws a centered string without a background shadow at the specified location
     *      with the given color
     *
     * @param text text to draw
     * @param x the x position for the text
     * @param y the y position for the text
     * @param color the texts color
     */
    @AntiReflection.HiddenMethod
    protected final void drawCenteredString(String text, int x, int y, int color) {
        this.mc.fontRendererObj.drawString(text, (float) (x - this.mc.fontRendererObj.getStringWidth(text) / 2), (float) y, color, false);
    }
}
