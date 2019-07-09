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

import java.util.ArrayList;
import java.util.List;

import net.hypixel.api.GameType;

/**
 * @author KodingKing
 */
public class MegaWallsStats extends AbstractHypixelStats {
    @Override
    public GameType getGameType() {
        return GameType.WALLS3;
    }

    @Override
    public String getImage() {
        return "MegaWalls-64";
    }

    @Override
    public String getName() {
        return "Mega Walls";
    }

    @Override
    public List<StatsDisplayItem> getPreview(HypixelApiPlayer player) {
        List<StatsDisplayItem> stats = new ArrayList<>();
        JsonHolder megaWalls = player.getStats(GameType.WALLS3);

        stats.add(new DisplayLine(bold("Coins: ", megaWalls.optInt("coins"))));
        stats.add(new DisplayLine(bold("Kills: ", megaWalls.optInt("kills"))));
        stats.add(new DisplayLine(bold("Deaths: ", megaWalls.optInt("deaths"))));
        stats.add(new DisplayLine(bold("Wins: ", megaWalls.optInt("wins"))));
        stats.add(new DisplayLine(bold("Losses: ", megaWalls.optInt("losses"))));

        return stats;
    }

    @Override
    public List<StatsDisplayItem> getDeepStats(HypixelApiPlayer player) {
        List<StatsDisplayItem> stats = getPreview(player);
        JsonHolder megaWalls = player.getStats(GameType.WALLS3);

        stats.add(new DisplayLine(""));
        stats.add(new DisplayLine(bold("Assists: ", megaWalls.optInt("assists"))));
        stats.add(new DisplayLine(bold("Final Kills: ", megaWalls.optInt("finalKills"))));
        stats.add(new DisplayLine(bold("Final Assists: ", megaWalls.optInt("finalAssists"))));
        stats.add(new DisplayLine(bold("Wither Damage: ", megaWalls.optInt("witherDamage"))));

        return stats;
    }
}
