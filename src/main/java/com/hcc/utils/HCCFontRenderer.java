/*
 * Hypixel Community Client, Client optimized for Hypixel Network
 * Copyright (C) 2018  HCC Dev Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.hcc.utils;



import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glPopMatrix;

import org.lwjgl.opengl.GL11;

import java.awt.Font;

import net.minecraft.client.Minecraft;
import net.minecraft.util.StringUtils;

import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

public class HCCFontRenderer {

    private Minecraft mc = Minecraft.getMinecraft();

    private final UnicodeFont unicodeFont;
    private final int[] colorCodes = new int[32];

    private int fontType, size;
    private String fontName;

    private float kerning;

    public HCCFontRenderer(String fontName, int fontType, int size) {
        this(fontName, fontType, size, 0);
    }

    public HCCFontRenderer(String fontName, int fontType, int size, float kerning) {
        this.fontName = fontName;
        this.fontType = fontType;
        this.size = size;

        this.unicodeFont = new UnicodeFont(new Font(fontName, fontType, size));
        this.kerning = kerning;

        this.unicodeFont.addAsciiGlyphs();
        this.unicodeFont.getEffects().add(new ColorEffect(java.awt.Color.WHITE));

        try {
            this.unicodeFont.loadGlyphs();
        } catch(Exception e) {
            e.printStackTrace();
        }

        for(int i = 0; i < 32; i++) {
            int shadow = (i >> 3 & 1) * 85;
            int red = (i >> 2 & 1) * 170 + shadow;
            int green = (i >> 1 & 1) * 170 + shadow;
            int blue = (i >> 0 & 1) * 170 + shadow;

            if(i == 6) {
                red += 85;
            }

            if(i >= 16) {
                red /= 4;
                green /= 4;
                blue /= 4;
            }

            this.colorCodes[i] = (red & 255) << 16 | (green & 255) << 8 | blue & 255;
        }

    }

    public int drawString(String text, float x, float y, int color) {
        x *= 2.0F;
        y *= 2.0F;
        float originalX = x;

        GL11.glPushMatrix();
        GL11.glScaled(0.5F, 0.5F, 0.5F);

        boolean blend = GL11.glIsEnabled(GL11.GL_BLEND);
        boolean lighting = GL11.glIsEnabled(GL11.GL_LIGHTING);
        boolean texture = GL11.glIsEnabled(GL11.GL_TEXTURE_2D);
        if(!blend)
            glEnable(GL11.GL_BLEND);
        if(lighting)
            glDisable(GL11.GL_LIGHTING);
        if(texture)
            glDisable(GL11.GL_TEXTURE_2D);

        int currentColor = color;
        char[] characters = text.toCharArray();

        int index = 0;
        for(char c : characters) {
            if(c == '\r') {
                x = originalX;
            }
            if(c == '\n') {
                y += getHeight(Character.toString(c)) * 2.0F;
            }
            if(c != '\247' && (index == 0 || index == characters.length - 1 || characters[index - 1] != '\247')) {
                unicodeFont.drawString(x, y, Character.toString(c), new org.newdawn.slick.Color(currentColor));
                x += (getWidth(Character.toString(c)) * 2.0F);
            } else if(c == ' ') {
                x += unicodeFont.getSpaceWidth();
            } else if(c == '\247' && index != characters.length - 1) {
                int codeIndex = "0123456789abcdefg".indexOf(text.charAt(index + 1));
                if(codeIndex < 0) continue;

                int col = this.colorCodes[codeIndex];
                currentColor = col;
            }

            index++;
        }

        GL11.glScaled(2.0F, 2.0F, 2.0F);
        if(texture)
            GL11.glEnable(GL11.GL_TEXTURE_2D);
        if(lighting)
            GL11.glEnable(GL11.GL_LIGHTING);
        if(!blend)
            GL11.glDisable(GL11.GL_BLEND);
        glPopMatrix();
        return (int) x;
    }

    public int drawStringWithShadow(String text, float x, float y, int color) {
        drawString(StringUtils.stripControlCodes(text), x + 0.5F, y + 0.5F, 0x000000);
        return drawString(text, x, y, color);
    }

    public void drawCenteredString(String text, float x, float y, int color) {
        drawString(text, x - (int)getWidth(text) / 2, y, color);
    }

    public void drawCenteredStringWithShadow(String text, float x, float y, int color) {
        drawCenteredString(StringUtils.stripControlCodes(text), x+0.5F, y+0.5F, color);
        drawCenteredString(text, x, y, color);
    }

    public float getWidth(String s) {
        float width = 0.0F;

        String str = StringUtils.stripControlCodes(s);
        for(char c : str.toCharArray()) {
            width += unicodeFont.getWidth(Character.toString(c)) + this.kerning;
        }

        return width / 2.0F;
    }

    public float getCharWidth(char c){
        return unicodeFont.getWidth(String.valueOf(c));
    }

    public float getHeight(String s) {
        return unicodeFont.getHeight(s) / 2.0F;
    }

    public UnicodeFont getFont() {
        return this.unicodeFont;
    }

    public String trimStringToWidth(String par1Str, int par2)
    {
        StringBuilder var4 = new StringBuilder();
        float var5 = 0.0F;
        int var6 = 0;
        int var7 = 1;
        boolean var8 = false;
        boolean var9 = false;

        for (int var10 = var6; var10 >= 0 && var10 < par1Str.length() && var5 < (float)par2; var10 += var7)
        {
            char var11 = par1Str.charAt(var10);
            float var12 = this.getCharWidth(var11);

            if (var8)
            {
                var8 = false;

                if (var11 != 108 && var11 != 76)
                {
                    if (var11 == 114 || var11 == 82)
                    {
                        var9 = false;
                    }
                }
                else
                {
                    var9 = true;
                }
            }
            else if (var12 < 0.0F)
            {
                var8 = true;
            }
            else
            {
                var5 += var12;

                if (var9)
                {
                    ++var5;
                }
            }

            if (var5 > (float)par2)
            {
                break;
            }

            else
            {
                var4.append(var11);
            }
        }

        return var4.toString();
    }
}