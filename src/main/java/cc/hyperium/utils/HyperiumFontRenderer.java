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

package cc.hyperium.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.StringUtils;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HyperiumFontRenderer {

    public final int FONT_HEIGHT = 9;
    private final int[] colorCodes = new int[32];
    private final Map<String, Float> cachedStringWidth = new HashMap<>();
    private float antiAliasingFactor;
    private UnicodeFont unicodeFont;
    private int prevScaleFactor = new ScaledResolution(Minecraft.getMinecraft()).getScaleFactor();
    private String name;
    private float size;

    public HyperiumFontRenderer(String fontName, float fontSize) {
        name = fontName;
        size = fontSize;
        ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());

        try {
            prevScaleFactor = resolution.getScaleFactor();
            unicodeFont = new UnicodeFont(getFontByName(fontName).deriveFont(fontSize * prevScaleFactor / 2));
            unicodeFont.addAsciiGlyphs();
            unicodeFont.getEffects().add(new ColorEffect(java.awt.Color.WHITE));
            unicodeFont.loadGlyphs();
        } catch (FontFormatException | IOException | SlickException e) {
            e.printStackTrace();
        }

        this.antiAliasingFactor = resolution.getScaleFactor();

        for (int i = 0; i < 32; i++) {
            int shadow = (i >> 3 & 1) * 85;
            int red = (i >> 2 & 1) * 170 + shadow;
            int green = (i >> 1 & 1) * 170 + shadow;
            int blue = (i & 1) * 170 + shadow;

            if (i == 6) {
                red += 85;
            }

            if (i >= 16) {
                red /= 4;
                green /= 4;
                blue /= 4;
            }

            colorCodes[i] = (red & 255) << 16 | (green & 255) << 8 | blue & 255;
        }
    }

    public HyperiumFontRenderer(Font font) {
        this(font.getFontName(), font.getSize());
    }

    public HyperiumFontRenderer(String fontName, int fontType, int size) {
        this(new Font(fontName, fontType, size));
    }

    private Font getFontByName(String name) throws IOException, FontFormatException {
        if (name.equalsIgnoreCase("roboto condensed") || name.equalsIgnoreCase("roboto")) {
            return getFontFromInput("/assets/hyperium/fonts/RobotoCondensed-Regular.ttf");
        } else if (name.equalsIgnoreCase("montserrat")) {
            return getFontFromInput("/assets/hyperium/fonts/Montserrat-Regular.ttf");
        } else if (name.equalsIgnoreCase("segoeui") || name.equalsIgnoreCase("segoeui light")) {
            return getFontFromInput("/assets/hyperium/fonts/SegoeUI-Light.ttf");
        } else if (name.equalsIgnoreCase("raleway")) {
            return getFontFromInput("/assets/hyperium/fonts/Raleway-SemiBold.ttf");
        } else {
            // Need to return the default font.
            return getFontFromInput("/assets/hyperium/fonts/SegoeUI-Light.ttf");
        }
    }

    private Font getFontFromInput(String path) throws IOException, FontFormatException {
        return Font.createFont(Font.TRUETYPE_FONT, HyperiumFontRenderer.class.getResourceAsStream(path));
    }

    public void drawStringScaled(String text, int givenX, int givenY, int color, double givenScale) {
        GL11.glPushMatrix();
        GL11.glTranslated(givenX, givenY, 0);
        GL11.glScaled(givenScale, givenScale, givenScale);
        drawString(text, 0, 0, color);
        GL11.glPopMatrix();
    }

    public int drawString(String text, float x, float y, int color) {
        if (text == null) return 0;

        boolean textureEnabled = GL11.glIsEnabled(GL11.GL_TEXTURE_2D);
        ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());

        try {
            if (resolution.getScaleFactor() != prevScaleFactor) {
                prevScaleFactor = resolution.getScaleFactor();
                unicodeFont = new UnicodeFont(getFontByName(name).deriveFont(size * prevScaleFactor / 2));
                unicodeFont.addAsciiGlyphs();
                unicodeFont.getEffects().add(new ColorEffect(java.awt.Color.WHITE));
                unicodeFont.loadGlyphs();
            }
        } catch (FontFormatException | IOException | SlickException e) {
            e.printStackTrace();
        }

        this.antiAliasingFactor = resolution.getScaleFactor();

        float originalX = x;

        GL11.glPushMatrix();
        GlStateManager.scale(1 / antiAliasingFactor, 1 / antiAliasingFactor, 1 / antiAliasingFactor);
        x *= antiAliasingFactor;
        y *= antiAliasingFactor;
        float red = (float) (color >> 16 & 255) / 255.0F;
        float green = (float) (color >> 8 & 255) / 255.0F;
        float blue = (float) (color & 255) / 255.0F;
        float alpha = (float) (color >> 24 & 255) / 255.0F;
        GlStateManager.color(red, green, blue, alpha);

        int currentColor = color;
        char[] characters = text.toCharArray();

        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        int index = 0;
        for (char c : characters) {
            if (c == '\r') x = originalX;
            if (c == '\n') y += getHeight(Character.toString(c));

            if (c != '\247' && (index == 0 || index == characters.length - 1 || characters[index - 1] != '\247')) {
                unicodeFont.drawString(x, y, Character.toString(c), new org.newdawn.slick.Color(currentColor));
                x += (getWidth(Character.toString(c)) * antiAliasingFactor);
            } else if (c == ' ') {
                x += unicodeFont.getSpaceWidth();
            } else if (c == '\247' && index != characters.length - 1) {
                int codeIndex = "0123456789abcdefg".indexOf(text.charAt(index + 1));
                if (codeIndex < 0) continue;
                currentColor = colorCodes[codeIndex];
            }

            index++;
        }

        GlStateManager.color(1F, 1F, 1F, 1F);
        GlStateManager.bindTexture(0);
        if (textureEnabled) GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
        return (int) x;
    }

    public int drawStringWithShadow(String text, float x, float y, int color) {
        drawString(StringUtils.stripControlCodes(text), x + 0.5F, y + 0.5F, 0x000000);
        return drawString(text, x, y, color);
    }

    public void drawCenteredString(String text, float x, float y, int color) {
        drawString(text, x - ((int) getWidth(text) >> 1), y, color);
    }

    /**
     * Draw Centered Text Scaled
     *
     * @param text       - Given Text String
     * @param givenX     - Given X Position
     * @param givenY     - Given Y Position
     * @param color      - Given Color (HEX)
     * @param givenScale - Given Scale
     */
    public void drawCenteredTextScaled(String text, int givenX, int givenY, int color, double givenScale) {
        GL11.glPushMatrix();
        GL11.glTranslated(givenX, givenY, 0);
        GL11.glScaled(givenScale, givenScale, givenScale);
        drawCenteredString(text, 0, 0, color);
        GL11.glPopMatrix();
    }

    public void drawCenteredStringWithShadow(String text, float x, float y, int color) {
        drawCenteredString(StringUtils.stripControlCodes(text), x + 0.5F, y + 0.5F, color);
        drawCenteredString(text, x, y, color);
    }

    public float getWidth(String text) {
        if (cachedStringWidth.size() > 1000) cachedStringWidth.clear();
        return cachedStringWidth.computeIfAbsent(text, e -> unicodeFont.getWidth(ChatColor.stripColor(text)) / antiAliasingFactor);
    }

    public float getCharWidth(char c) {
        return unicodeFont.getWidth(String.valueOf(c));
    }

    public float getHeight(String s) {
        return unicodeFont.getHeight(s) / 2.0F;
    }

    public UnicodeFont getFont() {
        return unicodeFont;
    }

    public void drawSplitString(ArrayList<String> lines, int x, int y, int color) {
        drawString(
            String.join("\n\r", lines),
            x,
            y,
            color
        );
    }

    public List<String> splitString(String text, int wrapWidth) {
        List<String> lines = new ArrayList<>();

        String[] splitText = text.split(" ");
        StringBuilder currentString = new StringBuilder();

        for (String word : splitText) {
            String potential = currentString + " " + word;

            if (getWidth(potential) >= wrapWidth) {
                lines.add(currentString.toString());
                currentString = new StringBuilder();
            }

            currentString.append(word).append(" ");
        }

        lines.add(currentString.toString());
        return lines;
    }
}
