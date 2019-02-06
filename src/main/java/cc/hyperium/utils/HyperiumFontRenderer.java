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

package cc.hyperium.utils;

import cc.hyperium.installer.InstallerMain;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.StringUtils;
import org.lwjgl.opengl.GL11;
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
    private final float kerning;
    private final Map<String, Float> cachedStringWidth = new HashMap<>();
    private float antiAliasingFactor;
    private UnicodeFont unicodeFont;

    public HyperiumFontRenderer(String fontName, int fontType, int size) {
        this(fontName, fontType, size, 0);
    }

    public HyperiumFontRenderer(String fontName, float fontSize, float kerning, float antiAliasingFactor) {
        this.antiAliasingFactor = antiAliasingFactor;
        try {
            this.unicodeFont = new UnicodeFont(getFontByName(fontName).deriveFont(fontSize * this.antiAliasingFactor));
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
        this.kerning = kerning;

        this.unicodeFont.addAsciiGlyphs();
        this.unicodeFont.getEffects().add(new ColorEffect(java.awt.Color.WHITE));

        try {
            this.unicodeFont.loadGlyphs();
        } catch (Exception e) {
            e.printStackTrace();
        }

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

            this.colorCodes[i] = (red & 255) << 16 | (green & 255) << 8 | blue & 255;
        }
    }

    public HyperiumFontRenderer(Font font, float kerning, float antiAliasingFactor) {
        this.antiAliasingFactor = antiAliasingFactor;
        this.unicodeFont = new UnicodeFont(new Font(font.getName(), font.getStyle(), (int) (font.getSize() * antiAliasingFactor)));
        this.kerning = kerning;

        this.unicodeFont.addAsciiGlyphs();
        this.unicodeFont.getEffects().add(new ColorEffect(java.awt.Color.WHITE));

        try {
            this.unicodeFont.loadGlyphs();
        } catch (Exception e) {
            e.printStackTrace();
        }

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

            this.colorCodes[i] = (red & 255) << 16 | (green & 255) << 8 | blue & 255;
        }
    }

    public HyperiumFontRenderer(String fontName, int fontType, int size, float kerning) {
        this(new Font(fontName, fontType, size), kerning, 3.0F);
    }

    private Font getFontByName(String name) throws IOException, FontFormatException {
        if (name.equalsIgnoreCase("roboto condensed") || name.equalsIgnoreCase("roboto")) {
            return getFontFromInput("/assets/hyperium/fonts/RobotoCondensed-Regular.ttf");
        } else if (name.equalsIgnoreCase("montserrat")) {
            return getFontFromInput("/assets/hyperium/fonts/Montserrat-Regular.ttf");
        } else if (name.equalsIgnoreCase("segoeui") || name.equalsIgnoreCase("segoeui light")) {
            return getFontFromInput("/assets/hyperium/fonts/SegoeUI-Light.ttf");
        } else {
            // Need to return the default font.
            return getFontFromInput("/assets/hyperium/fonts/SegoeUI-Light.ttf");
        }
    }

    private Font getFontFromInput(String path) throws IOException, FontFormatException {
        return Font.createFont(Font.TRUETYPE_FONT, InstallerMain.class.getResourceAsStream(path));
    }

    public void drawStringScaled(String text, int givenX, int givenY, int color, double givenScale) {

        GL11.glPushMatrix();
        GL11.glTranslated(givenX, givenY, 0);
        GL11.glScaled(givenScale, givenScale, givenScale);
        drawString(text, 0, 0, color);
        GL11.glPopMatrix();

    }

    public int drawString(String text, float x, float y, int color) {
        if (text == null)
            return 0;

        x *= 2.0F;
        y *= 2.0F;

        float originalX = x;

        GL11.glPushMatrix();
        GlStateManager.scale(1 / antiAliasingFactor, 1 / antiAliasingFactor, 1 / antiAliasingFactor);
        GL11.glScaled(0.5F, 0.5F, 0.5F);
        x *= antiAliasingFactor;
        y *= antiAliasingFactor;
        float red = (float) (color >> 16 & 255) / 255.0F;
        float green = (float) (color >> 8 & 255) / 255.0F;
        float blue = (float) (color & 255) / 255.0F;
        float alpha = (float) (color >> 24 & 255) / 255.0F;
        GlStateManager.color(red, green, blue, alpha);

        boolean blend = GL11.glIsEnabled(GL11.GL_BLEND);
        boolean lighting = GL11.glIsEnabled(GL11.GL_LIGHTING);
        boolean texture = GL11.glIsEnabled(GL11.GL_TEXTURE_2D);
        if (!blend)
            GL11.glEnable(GL11.GL_BLEND);
        if (lighting)
            GL11.glDisable(GL11.GL_LIGHTING);
        if (texture)
            GL11.glDisable(GL11.GL_TEXTURE_2D);

        int currentColor = color;
        char[] characters = text.toCharArray();

        int index = 0;
        for (char c : characters) {
            if (c == '\r') {
                x = originalX;
            }
            if (c == '\n') {
                y += getHeight(Character.toString(c)) * 2.0F;
            }
            if (c != '\247' && (index == 0 || index == characters.length - 1 || characters[index - 1] != '\247')) {
                //Line causing error
                unicodeFont.drawString(x, y, Character.toString(c), new org.newdawn.slick.Color(currentColor));
                x += (getWidth(Character.toString(c)) * 2.0F * antiAliasingFactor);
            } else if (c == ' ') {
                x += unicodeFont.getSpaceWidth();
            } else if (c == '\247' && index != characters.length - 1) {
                int codeIndex = "0123456789abcdefg".indexOf(text.charAt(index + 1));
                if (codeIndex < 0) continue;

                currentColor = this.colorCodes[codeIndex];
            }

            index++;
        }

        GL11.glScaled(2.0F, 2.0F, 2.0F);
        if (texture)
            GL11.glEnable(GL11.GL_TEXTURE_2D);
        if (lighting)
            GL11.glEnable(GL11.GL_LIGHTING);
        if (!blend)
            GL11.glDisable(GL11.GL_BLEND);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();
        return (int) x / 2;
    }

    public int drawStringWithShadow(String text, float x, float y, int color) {
        drawString(StringUtils.stripControlCodes(text), x + 0.5F, y + 0.5F, 0x000000);
        return drawString(text, x, y, color);
    }

    public void drawCenteredString(String text, float x, float y, int color) {
        drawString(text, x - (int) getWidth(text) / 2, y, color);
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

    public float getWidth(String s) {
        if (cachedStringWidth.size() > 1000)
            cachedStringWidth.clear();
        return cachedStringWidth.computeIfAbsent(s, e -> {
            float width = 0.0F;
            String str = StringUtils.stripControlCodes(s);
            for (char c : str.toCharArray()) {
                width += unicodeFont.getWidth(Character.toString(c)) + this.kerning;
            }

            return width / 2.0F / antiAliasingFactor;
        });

    }

    public int getStringWidth(String text) {
        if (text == null) {
            return 0;
        } else {
            int i = 0;
            boolean flag = false;

            for (int j = 0; j < text.length(); ++j) {
                char c0 = text.charAt(j);
                float k = this.getWidth(String.valueOf(c0));

                if (k < 0 && j < text.length() - 1) {
                    ++j;
                    c0 = text.charAt(j);

                    if (c0 != 'l' && c0 != 'L') {
                        if (c0 == 'r' || c0 == 'R') {
                            flag = false;
                        }
                    } else {
                        flag = true;
                    }

                    k = 0;
                }

                i += k;

                if (flag && k > 0) {
                    ++i;
                }
            }

            return i;
        }
    }

    public float getCharWidth(char c) {
        return unicodeFont.getWidth(String.valueOf(c));
    }

    public float getHeight(String s) {
        return unicodeFont.getHeight(s) / 2.0F;
    }

    public UnicodeFont getFont() {
        return this.unicodeFont;
    }

    public String trimStringToWidth(String par1Str, int par2) {
        StringBuilder var4 = new StringBuilder();
        float var5 = 0.0F;
        int var6 = 0;
        int var7 = 1;
        boolean var8 = false;
        boolean var9 = false;

        for (int var10 = var6; var10 >= 0 && var10 < par1Str.length() && var5 < (float) par2; var10 += var7) {
            char var11 = par1Str.charAt(var10);
            float var12 = this.getCharWidth(var11);

            if (var8) {
                var8 = false;

                if (var11 != 108 && var11 != 76) {
                    if (var11 == 114 || var11 == 82) {
                        var9 = false;
                    }
                } else {
                    var9 = true;
                }
            } else if (var12 < 0.0F) {
                var8 = true;
            } else {
                var5 += var12;

                if (var9) {
                    ++var5;
                }
            }

            if (var5 > (float) par2) {
                break;
            } else {
                var4.append(var11);
            }
        }

        return var4.toString();
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
