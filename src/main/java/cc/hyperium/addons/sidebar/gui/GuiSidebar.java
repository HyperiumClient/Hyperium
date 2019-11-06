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

package cc.hyperium.addons.sidebar.gui;

import cc.hyperium.addons.sidebar.config.Configuration;
import cc.hyperium.config.ConfigOpt;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.scoreboard.*;
import net.minecraft.util.EnumChatFormatting;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GuiSidebar extends Gui {
    public enum ChromaType {
        ONE("Background 1"),
        TWO("Background 2"),
        THREE("Text 1"),
        FOUR("Text 2");
        private String name;

        ChromaType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public static ChromaType next(ChromaType current) {
            return current == ChromaType.ONE ? ChromaType.TWO : (current == ChromaType.TWO ? ChromaType.THREE : (current == ChromaType.THREE ? ChromaType.FOUR : ChromaType.ONE));
        }
    }

    private FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
    private int sidebarX;
    private int sidebarY;
    private int sidebarWidth;
    private int sidebarHeight;

    @ConfigOpt
    public boolean enabled = Configuration.enabled;
    @ConfigOpt
    public boolean redNumbers = Configuration.redNumbers;
    @ConfigOpt
    public boolean shadow = Configuration.redNumbers;
    @ConfigOpt
    public float scale = Configuration.scale;
    @ConfigOpt
    public int offsetX = Configuration.offsetX;
    @ConfigOpt
    public int offsetY = Configuration.offsetY;
    @ConfigOpt
    public int color = Configuration.rgb;
    @ConfigOpt
    public int alpha = Configuration.alpha;
    @ConfigOpt
    public boolean chromaEnabled = Configuration.chromaEnabled;
    @ConfigOpt
    public ChromaType chromaType = Configuration.chromaType;
    @ConfigOpt
    public int chromaSpeed = Configuration.chromaSpeed;

    public boolean contains(int mouseX, int mouseY) {
        float mscale = scale - 1.0f;
        float minX = sidebarX - sidebarWidth * mscale;
        float maxX = minX + sidebarWidth * scale;
        float maxY = sidebarY + (sidebarHeight >> 1) * mscale;
        float minY = maxY - sidebarHeight * scale;
        return mouseX > minX && mouseX < maxX && mouseY > minY - fr.FONT_HEIGHT * scale && mouseY < maxY;
    }

    public void drawSidebar(ScoreObjective sidebar, ScaledResolution res) {
        if (!enabled) {
            return;
        }
        FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
        Scoreboard scoreboard = sidebar.getScoreboard();
        List<Score> scores = new ArrayList<>();
        sidebarWidth = fr.getStringWidth(sidebar.getDisplayName());

        for (Score score : scoreboard.getSortedScores(sidebar)) {
            String name = score.getPlayerName();
            if (scores.size() < 15 && name != null && !name.startsWith("#")) {
                Team team = scoreboard.getPlayersTeam(name);
                String s2 = redNumbers ? (": " + EnumChatFormatting.RED + score.getScorePoints()) : "";
                String str = ScorePlayerTeam.formatPlayerName(team, name) + s2;
                sidebarWidth = Math.max(sidebarWidth, fr.getStringWidth(str));
                scores.add(score);
            }
        }

        sidebarHeight = scores.size() * fr.FONT_HEIGHT;
        sidebarX = res.getScaledWidth() - sidebarWidth - 3 + offsetX;
        sidebarY = res.getScaledHeight() / 2 + sidebarHeight / 3 + offsetY;
        int scalePointX = sidebarX + sidebarWidth;
        int scalePointY = sidebarY - sidebarHeight / 2;
        float mscale = scale - 1.0f;

        GlStateManager.translate(-scalePointX * mscale, -scalePointY * mscale, 0.0f);
        GlStateManager.scale(scale, scale, 1.0F);

        int index = 0;
        for (Score score2 : scores) {
            ++index;
            ScorePlayerTeam team2 = scoreboard.getPlayersTeam(score2.getPlayerName());
            String s3 = ScorePlayerTeam.formatPlayerName(team2, score2.getPlayerName());
            String s4 = EnumChatFormatting.RED + "" + score2.getScorePoints();

            if (!redNumbers) {
                s4 = "";
            }

            int scoreX = sidebarX + sidebarWidth + 1;
            int scoreY = sidebarY - index * fr.FONT_HEIGHT;
            drawRect(sidebarX - 2, scoreY, scoreX, scoreY + fr.FONT_HEIGHT, getColor(false, true));
            drawString(s3, sidebarX, scoreY, getColor(false, false));
            drawString(s4, scoreX - fr.getStringWidth(s4), scoreY, getColor(false, false));
            if (index == scores.size()) {
                String s5 = sidebar.getDisplayName();
                drawRect(sidebarX - 2, scoreY - fr.FONT_HEIGHT - 1, scoreX, scoreY - 1, getColor(true, true));
                drawRect(sidebarX - 2, scoreY - 1, scoreX, scoreY, getColor(false, true));

                drawString(s5, sidebarX + (sidebarWidth - fr.getStringWidth(s5)) / 2, scoreY - fr.FONT_HEIGHT,
                    chromaEnabled ? getColor(false, false) : 553648127);
            }
        }

        GlStateManager.scale(1.0F / scale, 1.0F / scale, 1.0F);
        GlStateManager.translate(scalePointX * mscale, scalePointY * mscale, 0.0f);
    }

    private int getColor(boolean darker, boolean isBackground) {
        int rgb = color;

        if (chromaEnabled && isBackground) {
            long dif;
            float time;

            switch (chromaType) {
                case ONE:
                    dif = 0;
                    time = 1000.0f;
                    long millis = System.currentTimeMillis() - dif;
                    millis = millis * chromaSpeed;
                    rgb = Color.HSBtoRGB((float) (millis % (int) time) / time, 0.8F, 0.8F);
                    break;

                case TWO:
                    dif = (sidebarX * 10) - (sidebarY * 10);
                    time = 2000.0f;
                    long millisDif = System.currentTimeMillis() - dif;
                    rgb = Color.HSBtoRGB((float) (millisDif % (int) time) / time, 0.8F, 0.8F);
                    break;
            }

        } else if (chromaEnabled) {
            long dif;
            float time;

            switch (chromaType) {
                case THREE:
                    dif = 0;
                    time = 1000.0f;
                    break;
                case FOUR:
                    dif = (sidebarX * 10) - (sidebarY * 10);
                    time = 2000.0f;
                    break;
                default:
                    return 553648127;
            }

            long millis = System.currentTimeMillis() - dif;
            rgb = Color.HSBtoRGB((float) (millis % (int) time) / time, 0.8F, 0.8F);
        } else if (!isBackground) {
            rgb = 553648127;
        }

        return isBackground ? (darker ? getColorWithAlpha(rgb, Math.min(255, alpha + 10)) : getColorWithAlpha(rgb, alpha)) : rgb;
    }

    private int getColorWithAlpha(int rgb, int a) {
        int r = rgb >> 16 & 0xFF;
        int g = rgb >> 8 & 0xFF;
        int b = rgb & 0xFF;
        return a << 24 | r << 16 | g << 8 | b;
    }

    private void drawString(String str, int x, int y, int color) {
        if (shadow) fr.drawStringWithShadow(str, (float) x, (float) y, color);
        else fr.drawString(str, x, y, color);
    }
}
