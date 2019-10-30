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

package cc.hyperium.gui;

import cc.hyperium.config.ConfigOpt;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.render.RenderScoreboardEvent;
import cc.hyperium.utils.RenderUtils;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumChatFormatting;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Sk1er
 */
public class ScoreboardRenderer {

    @ConfigOpt
    private double xLocation = 1.0D;
    @ConfigOpt
    private double yLocation = .5D;


    public void render(ScoreObjective objective, ScaledResolution resolution) {
        RenderScoreboardEvent renderEvent = new RenderScoreboardEvent(xLocation, yLocation, objective, resolution);
        EventBus.INSTANCE.post(renderEvent);
        if (!renderEvent.isCancelled()) {
            Scoreboard scoreboard = objective.getScoreboard();
            Collection<Score> collection = scoreboard.getSortedScores(objective);
            List<Score> list = collection.stream().filter(p_apply_1_ -> p_apply_1_.getPlayerName() != null
                && !p_apply_1_.getPlayerName().startsWith("#")).collect(Collectors.toList());

            collection = list.size() > 15 ? Lists.newArrayList(Iterables.skip(list, collection.size() - 15)) : list;
            int i = getFontRenderer().getStringWidth(objective.getDisplayName());

            for (Score score : collection) {
                ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(score.getPlayerName());
                String s = ScorePlayerTeam.formatPlayerName(scoreplayerteam, score.getPlayerName()) + ": " + EnumChatFormatting.RED + score.getScorePoints();
                i = Math.max(i, getFontRenderer().getStringWidth(s));
            }

            int i1 = collection.size() * getFontRenderer().FONT_HEIGHT;
            int j1 = (int) (resolution.getScaledHeight_double() * yLocation) + i1 / 3;
            int k1 = 3;
            int l1 = (int) (resolution.getScaledWidth_double() * xLocation) - i - k1;
            int j = 0;

            for (Score score1 : collection) {
                ++j;
                ScorePlayerTeam scoreplayerteam1 = scoreboard.getPlayersTeam(score1.getPlayerName());
                String s1 = ScorePlayerTeam.formatPlayerName(scoreplayerteam1, score1.getPlayerName());
                String s2 = EnumChatFormatting.RED.toString() + score1.getScorePoints();
                int k = j1 - j * getFontRenderer().FONT_HEIGHT;
                int l = (int) (resolution.getScaledWidth_double() * xLocation) - k1 + 2;
                RenderUtils.drawRect(l1 - 2, k, l, k + getFontRenderer().FONT_HEIGHT, 1342177280);
                getFontRenderer().drawString(s1, l1, k, 553648127);
                getFontRenderer().drawString(s2, l - getFontRenderer().getStringWidth(s2), k, 553648127);

                if (j == collection.size()) {
                    String s3 = objective.getDisplayName();
                    RenderUtils.drawRect(l1 - 2, k - getFontRenderer().FONT_HEIGHT - 1, l, k - 1, 1610612736);
                    RenderUtils.drawRect(l1 - 2, k - 1, l, k, 1342177280);
                    getFontRenderer().drawString(s3, l1 + i / 2 - getFontRenderer().getStringWidth(s3) / 2, k - getFontRenderer().FONT_HEIGHT, 553648127);
                }
            }
        }
    }

    private FontRenderer getFontRenderer() {
        return Minecraft.getMinecraft().fontRendererObj;
    }

}
