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
import cc.hyperium.handlers.handlers.stats.display.DisplayTable;
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
public class SkyClashStats extends AbstractHypixelStats {

    @Override

    public String getImage() {
        return "SkyClash-64";
    }

    @Override
    public String getName() {
        return "SkyClash";
    }

    @Override
    public GameType getGameType() {
        return GameType.SKYCLASH;
    }

    @Override
    public List<StatsDisplayItem> getPreview(HypixelApiPlayer player) {
        List<StatsDisplayItem> stats = new ArrayList<>();
        JsonHolder skyClash = player.getStats(GameType.SKYCLASH);

        stats.add(new DisplayLine(bold("Coins: ", skyClash.optInt("coins"))));
        stats.add(new DisplayLine(bold("Kills: ", skyClash.optInt("kills"))));
        stats.add(new DisplayLine(bold("Wins: ", skyClash.optInt("wins"))));
        stats.add(new DisplayLine(bold("Deaths: ", skyClash.optInt("deaths"))));
        stats.add(new DisplayLine(bold("Losses: ", skyClash.optInt("losses"))));

        return stats;
    }

    @Override
    public List<StatsDisplayItem> getDeepStats(HypixelApiPlayer player) {
        List<StatsDisplayItem> stats = getPreview(player);
        JsonHolder skyClash = player.getStats(GameType.SKYCLASH);

        stats.add(new DisplayLine(bold("Assists: ", skyClash.optInt("assists"))));
        stats.add(new DisplayLine(bold("K/D: ", WebsiteUtils
            .buildRatio(skyClash.optInt("kills"), skyClash.optInt("deaths")))));
        stats.add(new DisplayLine(
            bold("W/L: ", WebsiteUtils.buildRatio(skyClash.optInt("wins"), skyClash.optInt("losses")))));
        stats.add(new DisplayLine(""));
        stats.add(new DisplayLine(""));
        stats.add(new DisplayLine(bold("Mobs Spawned: ", skyClash.optInt("mobs_spawned"))));
        stats.add(new DisplayLine(""));

        List<String[]> lines = new ArrayList<>();
        lines.add(new String[]{"Mode",
            "Fastest Win",
            "Kills",
            "Wins",
            "Losses",
            "Deaths"});

        String[] SKYCLASH_MODES = {"Solo", "Doubles", "Team War"};
        for (String front : SKYCLASH_MODES) {
            String b = front.toLowerCase().replace(" ", "_");
            lines.add(new String[]{front,
                String.valueOf(skyClash.optInt("fastest_win_" + b)),
                String.valueOf(skyClash.optInt("kills_" + b)),
                String.valueOf(skyClash.optInt("wins_" + b)),
                String.valueOf(skyClash.optInt("losses_" + b)),
                String.valueOf(skyClash.optInt("deaths_" + b))});
        }
        stats.add(new DisplayTable(lines));

        return stats;
    }
}
