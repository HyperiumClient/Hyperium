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
public class SpeedUHCStats extends AbstractHypixelStats {

    @Override
    public String getImage() {
        return "SpeedUHC-64";
    }

    @Override
    public String getName() {
        return "Speed UHC";
    }

    @Override
    public GameType getGameType() {
        return GameType.SPEED_UHC;
    }

    @Override
    public List<StatsDisplayItem> getPreview(HypixelApiPlayer player) {
        List<StatsDisplayItem> stats = new ArrayList<>();
        JsonHolder speedUhc = player.getStats(GameType.SPEED_UHC);

        stats.add(new DisplayLine(bold("Coins: ", speedUhc.optInt("coins"))));
        stats.add(new DisplayLine(bold("Kills: ", speedUhc.optInt("kills"))));
        stats.add(new DisplayLine(bold("Deaths: ", speedUhc.optInt("deaths"))));
        stats.add(new DisplayLine(bold("Wins: ", speedUhc.optInt("wins"))));
        stats.add(new DisplayLine(bold("Losses: ", speedUhc.optInt("losses"))));

        return stats;
    }

    @Override
    public List<StatsDisplayItem> getDeepStats(HypixelApiPlayer player) {
        List<StatsDisplayItem> stats = getPreview(player);
        JsonHolder speedUhc = player.getStats(GameType.SPEED_UHC);

        stats.add(new DisplayLine(""));
        stats.add(new DisplayLine(bold("K/D: ", WebsiteUtils
            .buildRatio(speedUhc.optInt("kills"), speedUhc.optInt("deaths")))));
        stats.add(new DisplayLine(bold("W/L: ",
            WebsiteUtils.buildRatio(speedUhc.optInt("wins"), speedUhc.optInt("losses")))));
        stats.add(new DisplayLine(""));
        stats.add(new DisplayLine(bold("Salt: ", speedUhc.optInt("salt"))));
        final String[] SpeedUHC_modes = {"Solo Normal", "Solo Insane", "Team Normal", "Team Insane"};
        stats.add(new DisplayLine(""));

        List<String[]> lines = new ArrayList<>();
        lines.add(new String[]{"Mode", "Kills", "Deaths", "Wins", "Losses", "K/D", "W/L"});
        for (String name : SpeedUHC_modes) {
            String api = name.toLowerCase().replace(" ", "_");
            lines.add(new String[]{name, String.valueOf(speedUhc.optInt("kills_" + api)),
                String.valueOf(speedUhc.optInt("deaths_" + api)),
                String.valueOf(speedUhc.optInt("wins_" + api)),
                String.valueOf(speedUhc.optInt("losses_" + api)),
                WebsiteUtils
                    .buildRatio(speedUhc.optInt("kills_" + api), speedUhc.optInt("deaths_" + api)),
                WebsiteUtils
                    .buildRatio(speedUhc.optInt("wins_" + api), speedUhc.optInt("losses_" + api))});
        }
        stats.add(new DisplayTable(lines));

        return stats;
    }
}
