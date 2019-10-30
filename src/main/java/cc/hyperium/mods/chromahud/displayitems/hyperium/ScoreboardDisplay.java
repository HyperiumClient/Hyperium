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

package cc.hyperium.mods.chromahud.displayitems.hyperium;

import cc.hyperium.mods.chromahud.ElementRenderer;
import cc.hyperium.mods.chromahud.api.DisplayItem;
import cc.hyperium.utils.JsonHolder;
import cc.hyperium.utils.RenderUtils;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumChatFormatting;

import java.awt.*;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ScoreboardDisplay extends DisplayItem {

    public static ScoreObjective objective;
    public static ScaledResolution resolution;

    public ScoreboardDisplay(JsonHolder raw, int ordinal) {
        super(raw, ordinal);
    }

    @Override
    public void draw(int starX, double startY, boolean config) {
        if (objective != null) {
            boolean guiF = false;

            Scoreboard scoreboard = objective.getScoreboard();
            Collection<Score> collection = scoreboard.getSortedScores(objective);
            List<Score> list = collection.stream().filter(p_apply_1_ -> p_apply_1_.getPlayerName() != null &&
                !p_apply_1_.getPlayerName().startsWith("#")).collect(Collectors.toList());

            collection = list.size() > 15 ? Lists.newArrayList(Iterables.skip(list, collection.size() - 15)) : list;

            int i = Minecraft.getMinecraft().fontRendererObj.getStringWidth(objective.getDisplayName());

            for (Score score : collection) {
                ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(score.getPlayerName());
                String s = ScorePlayerTeam.formatPlayerName(scoreplayerteam, score.getPlayerName()) + ": " + EnumChatFormatting.RED + score.getScorePoints();
                i = Math.max(i, Minecraft.getMinecraft().fontRendererObj.getStringWidth(s));
            }

            int i1 = collection.size() * Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT;
            int l1 = starX;
            if (ElementRenderer.getCurrent().isRightSided()) l1 -= i;
            int j = 0;

            int k;
            for (Score score1 : collection) {
                ++j;
                ScorePlayerTeam scoreplayerteam1 = scoreboard.getPlayersTeam(score1.getPlayerName());
                String s1 = ScorePlayerTeam.formatPlayerName(scoreplayerteam1, score1.getPlayerName());
                String s2 = EnumChatFormatting.RED + "" + score1.getScorePoints();
                k = ((int) startY) + (collection.size() - j + 1) * Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT;

                if (ElementRenderer.getCurrent().isHighlighted()) {
                    if (guiF) {
                        Gui.drawRect(l1 - 2, k, l1 + i, k + Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT, 1342177280);
                    } else {
                        RenderUtils.drawRect(l1 - 2, k, l1 + i, k + Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT, 1342177280);
                    }
                }

                Minecraft.getMinecraft().fontRendererObj.drawString(s1, l1, k, Color.WHITE.getRGB());
                if (data.optBoolean("numbers")) {
                    Minecraft.getMinecraft().fontRendererObj.drawString(s2, l1 + i - Minecraft.getMinecraft().fontRendererObj.getStringWidth(s2), k, Color.WHITE.getRGB());
                }

                if (j == collection.size()) {
                    String s3 = objective.getDisplayName();
                    if (ElementRenderer.getCurrent().isHighlighted()) {
                        if (guiF) {
                            Gui.drawRect(l1 - 2, k - Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT - 1, l1 + i, k - 1, 1610612736);
                            Gui.drawRect(l1 - 2, k - 1, l1 + i, k, 1342177280);
                        } else {
                            RenderUtils.drawRect(l1 - 2, k - Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT - 1, l1 + i, k - 1, 1610612736);
                            RenderUtils.drawRect(l1 - 2, k - 1, l1 + i, k, 1342177280);
                        }
                    }

                    Minecraft.getMinecraft().fontRendererObj.drawString(s3, l1 + i / 2 - Minecraft.getMinecraft().fontRendererObj.getStringWidth(s3) / 2, k - Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT, Color.WHITE.getRGB());
                }
            }

            width = i - (data.optBoolean("numbers") ? 0 : 10);
            height = i1 + 10;
        }
    }
}
