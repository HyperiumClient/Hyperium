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
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumChatFormatting;

/**
 * @author Sk1er
 */
public class ScoreboardRenderer {

  private final FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;

  public void render(ScoreObjective objective, ScaledResolution resolution) {
    double yLocation = .5D;
    double xLocation = 1.0D;
    RenderScoreboardEvent renderEvent = new RenderScoreboardEvent(xLocation, yLocation, objective, resolution);
    EventBus.INSTANCE.post(renderEvent);
    if (!renderEvent.isCancelled()) {
      Scoreboard scoreboard = objective.getScoreboard();
      Collection<Score> collection = scoreboard.getSortedScores(objective);
      List<Score> list = new ArrayList<>();
      for (Score score : collection) {
        if (score.getPlayerName() != null && !score.getPlayerName().startsWith("#")) {
          list.add(score);
        }
      }

      collection = list.size() > 15 ? Lists.newArrayList(Iterables.skip(list, collection.size() - 15)) : list;
      int i = fontRenderer.getStringWidth(objective.getDisplayName());

      for (Score score : collection) {
        ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(score.getPlayerName());
        String s = ScorePlayerTeam.formatPlayerName(scoreplayerteam, score.getPlayerName()) + ": " + EnumChatFormatting.RED + score.getScorePoints();
        i = Math.max(i, fontRenderer.getStringWidth(s));
      }

      int i1 = collection.size() * fontRenderer.FONT_HEIGHT;
      int j1 = (int) (resolution.getScaledHeight_double() * yLocation) + i1 / 3;
      int k1 = 3;
      int l1 = (int) (resolution.getScaledWidth_double() * xLocation) - i - k1;
      int j = 0;

      for (Score score1 : collection) {
        ++j;
        ScorePlayerTeam scoreplayerteam1 = scoreboard.getPlayersTeam(score1.getPlayerName());
        String s1 = ScorePlayerTeam.formatPlayerName(scoreplayerteam1, score1.getPlayerName());
        String s2 = EnumChatFormatting.RED.toString() + score1.getScorePoints();
        int k = j1 - j * fontRenderer.FONT_HEIGHT;
        int l = (int) (resolution.getScaledWidth_double() * xLocation) - k1 + 2;
        Gui.drawRect(l1 - 2, k, l, k + fontRenderer.FONT_HEIGHT, 1342177280);
        fontRenderer.drawString(s1, l1, k, 553648127);
        fontRenderer.drawString(s2, l - fontRenderer.getStringWidth(s2), k, 553648127);

        if (j == collection.size()) {
          String s3 = objective.getDisplayName();
          Gui.drawRect(l1 - 2, k - fontRenderer.FONT_HEIGHT - 1, l, k - 1, 1610612736);
          Gui.drawRect(l1 - 2, k - 1, l, k, 1342177280);
          fontRenderer.drawString(s3, l1 + i / 2 - fontRenderer.getStringWidth(s3) / 2, k - fontRenderer.FONT_HEIGHT, 553648127);
        }
      }
    }
  }

}
