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

package cc.hyperium.handlers.handlers.stats.fields;

import cc.hyperium.handlers.handlers.stats.AbstractHypixelStats;
import cc.hyperium.handlers.handlers.stats.display.DisplayLine;
import cc.hyperium.handlers.handlers.stats.display.StatsDisplayItem;
import cc.hyperium.utils.JsonHolder;
import club.sk1er.website.api.requests.HypixelApiPlayer;
import club.sk1er.website.utils.WebsiteUtils;

import java.util.ArrayList;
import java.util.List;

import net.hypixel.api.GameType;

/**
 * @author KodingKing
 */
public class UHCStats extends AbstractHypixelStats {

    @Override
    public String getImage() {
        return "UHC-64";
    }

    @Override
    public String getName() {
        return GameType.UHC.getName();
    }

    @Override
    public GameType getGameType() {
        return GameType.UHC;
    }

    @Override
    public List<StatsDisplayItem> getPreview(HypixelApiPlayer player) {
        List<StatsDisplayItem> stats = new ArrayList<>();
        JsonHolder UHC = player.getStats(GameType.UHC);

        stats.add(new DisplayLine(bold("Coins: ", UHC.optInt("coins"))));
        stats.add(new DisplayLine(bold("Score: ", UHC.optInt("score"))));

        return stats;
    }

    @Override
    public List<StatsDisplayItem> getDeepStats(HypixelApiPlayer player) {
        List<StatsDisplayItem> stats = getPreview(player);
        JsonHolder UHC = player.getStats(GameType.UHC);

        stats.add(new DisplayLine(""));
        stats.add(new DisplayLine(bold("Teams", "")));
        stats.add(new DisplayLine(bold("Kills: ", UHC.optInt("kills"))));
        stats.add(new DisplayLine(bold("Deaths: ", UHC.optInt("deaths"))));
        stats.add(new DisplayLine(bold("Wins: ", UHC.optInt("wins"))));
        stats.add(new DisplayLine(bold("K/D: ", WebsiteUtils.buildRatio(UHC.optInt("kills"), UHC.optInt("deaths")))));
        stats.add(new DisplayLine(""));
        stats.add(new DisplayLine(bold("Solo", "")));
        stats.add(new DisplayLine(bold("Kills: ", UHC.optInt("kills_solo"))));
        stats.add(new DisplayLine(bold("Deaths: ", UHC.optInt("deaths_solo"))));
        stats.add(new DisplayLine(bold("Wins: ", UHC.optInt("wins_solo"))));
        stats.add(new DisplayLine(bold("K/D: ",
            WebsiteUtils.buildRatio(UHC.optInt("kills_solo"), UHC.optInt("deaths_solo")))));

        return stats;
    }
}
