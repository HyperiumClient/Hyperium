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

package cc.hyperium.addons.sidebar.gui;

/*
 * Chroma made by Sk1er (ChromaHUD)
 */

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.scoreboard.*;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GuiSidebar extends Gui {
    public enum ChromaType {
        // Shet code i know
        ONE("Background 1", 0),
        TWO("Background 2", 1),
        THREE("Text 1", 2),
        FOUR("Text 2", 3);
        private String name;
        private int index;

        ChromaType(String name, int index) {
            this.name = name;
            this.index = index;
        }

        public String getName() {
            return this.name;
        }

        public static ChromaType next(ChromaType current) {
            return current == ChromaType.ONE ? ChromaType.TWO : (current == ChromaType.TWO ? ChromaType.THREE : (current == ChromaType.THREE ? ChromaType.FOUR : ChromaType.ONE));
        }
    }

    private FontRenderer fr;
    private int sidebarX;
    private int sidebarY;
    private int sidebarWidth;
    private int sidebarHeight;
    public boolean enabled;
    public boolean redNumbers;
    public boolean shadow;
    public float scale;
    public int offsetX;
    public int offsetY;
    public int color;
    public int alpha;
    public boolean chromaEnabled;
    public ChromaType chromaType;
    public int chromaSpeed;

    public GuiSidebar() {
        this.fr = Minecraft.getMinecraft().fontRendererObj;
    }

    public boolean contains(final int mouseX, final int mouseY) {
        final float mscale = this.scale - 1.0f;
        final float minX = this.sidebarX - this.sidebarWidth * mscale;
        final float maxX = minX + this.sidebarWidth * this.scale;
        final float maxY = this.sidebarY + this.sidebarHeight / 2 * mscale;
        final float minY = maxY - this.sidebarHeight * this.scale;
        return mouseX > minX && mouseX < maxX && mouseY > minY - this.fr.FONT_HEIGHT * this.scale && mouseY < maxY;
    }

    public void drawSidebar(final ScoreObjective sidebar, final ScaledResolution res) {
        if (!this.enabled) {
            return;
        }
        final FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
        final Scoreboard scoreboard = sidebar.getScoreboard();
        final List<Score> scores = new ArrayList<>();
        this.sidebarWidth = fr.getStringWidth(sidebar.getDisplayName());
        for (final Score score : scoreboard.getSortedScores(sidebar)) {
            final String name = score.getPlayerName();
            if (scores.size() < 15 && name != null && !name.startsWith("#")) {
                final Team team = scoreboard.getPlayersTeam(name);
                final String s2 = this.redNumbers ? (": " + EnumChatFormatting.RED + score.getScorePoints()) : "";
                final String str = ScorePlayerTeam.formatPlayerName(team, name) + s2;
                this.sidebarWidth = Math.max(this.sidebarWidth, fr.getStringWidth(str));
                scores.add(score);
            }
        }
        this.sidebarHeight = scores.size() * fr.FONT_HEIGHT;
        this.sidebarX = res.getScaledWidth() - this.sidebarWidth - 3 + this.offsetX;
        this.sidebarY = res.getScaledHeight() / 2 + this.sidebarHeight / 3 + this.offsetY;
        final int scalePointX = this.sidebarX + this.sidebarWidth;
        final int scalePointY = this.sidebarY - this.sidebarHeight / 2;
        final float mscale = this.scale - 1.0f;
        GL11.glTranslatef(-scalePointX * mscale, -scalePointY * mscale, 0.0f);
        GL11.glScalef(this.scale, this.scale, 1.0f);
        int index = 0;
        for (final Score score2 : scores) {
            ++index;
            final ScorePlayerTeam team2 = scoreboard.getPlayersTeam(score2.getPlayerName());
            final String s3 = ScorePlayerTeam.formatPlayerName(team2, score2.getPlayerName());
            String s4 = EnumChatFormatting.RED + "" + score2.getScorePoints();
            if (!this.redNumbers) {
                s4 = "";
            }
            final int scoreX = this.sidebarX + this.sidebarWidth + 1;
            final int scoreY = this.sidebarY - index * fr.FONT_HEIGHT;
            drawRect(this.sidebarX - 2, scoreY, scoreX, scoreY + fr.FONT_HEIGHT, this.getColor(false, true));
            this.drawString(s3, this.sidebarX, scoreY, getColor(false, false));
            this.drawString(s4, scoreX - fr.getStringWidth(s4), scoreY, getColor(false, false));
            if (index == scores.size()) {
                final String s5 = sidebar.getDisplayName();
                drawRect(this.sidebarX - 2, scoreY - fr.FONT_HEIGHT - 1, scoreX, scoreY - 1, this.getColor(true, true));
                drawRect(this.sidebarX - 2, scoreY - 1, scoreX, scoreY, this.getColor(false, true));
                if (this.chromaEnabled) {
                    this.drawString(s5, this.sidebarX + (this.sidebarWidth - fr.getStringWidth(s5)) / 2, scoreY - fr.FONT_HEIGHT, getColor(false, false));
                } else {
                    this.drawString(s5, this.sidebarX + (this.sidebarWidth - fr.getStringWidth(s5)) / 2, scoreY - fr.FONT_HEIGHT, 553648127);
                }
            }
        }
        GL11.glScalef(1.0f / this.scale, 1.0f / this.scale, 1.0f);
        GL11.glTranslatef(scalePointX * mscale, scalePointY * mscale, 0.0f);
    }

    private int getColor(final boolean darker, final boolean isBackground) {
        int rgb = this.color;
        if (this.chromaEnabled && isBackground) {
            long dif;
            float ff;
            switch (this.chromaType) {
                case ONE:
                    dif = 0;
                    ff = 1000.0f;
                    long l = System.currentTimeMillis() - dif;
                    l = l * chromaSpeed;
                    rgb = Color.HSBtoRGB((float) (l % (int) ff) / ff, 0.8F, 0.8F);
                    break;
                case TWO:
                    dif = (this.sidebarX * 10) - (this.sidebarY * 10);
                    ff = 2000.0f;
                    long l2 = System.currentTimeMillis() - dif;
                    rgb = Color.HSBtoRGB((float) (l2 % (int) ff) / ff, 0.8F, 0.8F);
                    break;
            }
        } else if (this.chromaEnabled && !isBackground) {
            long dif;
            float ff;
            switch (this.chromaType) {
                case THREE:
                    dif = 0;
                    ff = 1000.0f;
                    break;
                case FOUR:
                    dif = (this.sidebarX * 10) - (this.sidebarY * 10);
                    ff = 2000.0f;
                    break;
                default:
                    return 553648127;
            }
            long l = System.currentTimeMillis() - dif;
            rgb = Color.HSBtoRGB((float) (l % (int) ff) / ff, 0.8F, 0.8F);
        } else if (!isBackground) {
            rgb = 553648127;
        }
        return isBackground ? (darker ? this.getColorWithAlpha(rgb, Math.min(255, this.alpha + 10)) : this.getColorWithAlpha(rgb, this.alpha)) : rgb;
    }

    private int getColorWithAlpha(final int rgb, final int a) {
        final int r = rgb >> 16 & 0xFF;
        final int g = rgb >> 8 & 0xFF;
        final int b = rgb & 0xFF;
        return a << 24 | r << 16 | g << 8 | b;
    }

    private void drawString(final String str, final int x, final int y, final int color) {
        if (this.shadow) {
            this.fr.drawStringWithShadow(str, (float) x, (float) y, color);
        } else {
            this.fr.drawString(str, x, y, color);
        }
    }
}
