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
public class TNTGamesStats extends AbstractHypixelStats {
    @Override
    public String getImage() {
        return "TNT-64";
    }

    @Override
    public String getName() {
        return GameType.TNTGAMES.getName();
    }

    @Override
    public GameType getGameType() {
        return GameType.TNTGAMES;
    }

    @Override
    public List<StatsDisplayItem> getPreview(HypixelApiPlayer player) {
        List<StatsDisplayItem> stats = new ArrayList<>();
        JsonHolder tntGames = player.getStats(GameType.TNTGAMES);
        stats.add(new DisplayLine(bold("Coins: ", tntGames.optInt("coins"))));

        return stats;
    }

    @Override
    public List<StatsDisplayItem> getDeepStats(HypixelApiPlayer player) {
        List<StatsDisplayItem> stats = getPreview(player);
        JsonHolder tntGames = player.getStats(GameType.TNTGAMES);

        stats.add(new DisplayLine(bold("TNT Run Wins: ", tntGames.optInt("wins_tntrun"))));
        stats.add(new DisplayLine(bold("TNT Tag Wins: ", tntGames.optInt("wins_tnttag"))));
        stats.add(new DisplayLine(bold("PVP Run Wins: ", tntGames.optInt("wins_pvprun"))));
        stats.add(new DisplayLine(bold("Bowspleef Wins: ", tntGames.optInt("wins_bowspleef"))));
        stats.add(new DisplayLine(bold("Bowspleef W/L: ", WebsiteUtils
            .buildRatio(tntGames.optInt("wins_bowspleef"), tntGames.optInt("deaths_bowspleef")))));
        stats.add(new DisplayLine(bold("TNT Wizards Kills: ", tntGames.optInt("kills_capture"))));
        stats.add(new DisplayLine(bold("TNT Wizards Wins: ", tntGames.optInt("wins_capture"))));
        stats.add(new DisplayLine(bold("TNt Wizards K/D: ", WebsiteUtils.buildRatio(tntGames.optInt("kills_capsafture"), tntGames.optInt("deaths_capture")))));

        return stats;
    }
}
