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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.hypixel.api.GameType;

/**
 * @author KodingKing
 */
public class SkyWarsStats extends AbstractHypixelStats {

    @Override
    public String getImage() {
        return "Skywars-64";
    }

    @Override
    public String getName() {
        return "SkyWars";
    }

    @Override
    public GameType getGameType() {
        return GameType.SKYWARS;
    }

    @Override
    public List<StatsDisplayItem> getPreview(HypixelApiPlayer player) {
        List<StatsDisplayItem> stats = new ArrayList<>();
        JsonHolder skyWars = player.getStats(GameType.SKYWARS);

        stats.add(new DisplayLine(bold("Coins: ", skyWars.optInt("coins"))));
        stats.add(new DisplayLine(bold("Kills: ", skyWars.optInt("kills"))));
        stats.add(new DisplayLine(bold("Assists: ", skyWars.optInt("assists"))));
        stats.add(new DisplayLine(bold("Deaths: ", skyWars.optInt("deaths"))));
        stats.add(new DisplayLine(bold("Wins: ", skyWars.optInt("wins"))));
        stats.add(new DisplayLine(bold("Losses: ", skyWars.optInt("losses"))));

        return stats;
    }

    @Override
    public List<StatsDisplayItem> getDeepStats(HypixelApiPlayer player) {
        List<StatsDisplayItem> stats = getPreview(player);
        JsonHolder skyWars = player.getStats(GameType.SKYWARS);
        SimpleDateFormat hhmmss = new SimpleDateFormat("HH:mm:ss");

        stats.add(new DisplayLine(""));
        stats.add(new DisplayLine(
            bold("K/D: ", WebsiteUtils.buildRatio(skyWars.optInt("kills"), skyWars.optInt("deaths")))));
        stats.add(new DisplayLine(
            bold("W/L: ", WebsiteUtils.buildRatio(skyWars.optInt("wins"), skyWars.optInt("losses")))));
        stats.add(new DisplayLine(""));
        stats.add(new DisplayLine(bold("Total Souls: ", skyWars.optInt("souls"))));
        stats.add(new DisplayLine(bold("Soul Well Usages: ", skyWars.optInt("soul_well"))));
        stats.add(
            new DisplayLine(bold("Soul Well Legendaries: ", skyWars.optInt("soul_well_legendaries"))));
        stats.add(new DisplayLine(bold("Soul Well Rares: ", skyWars.optInt("soul_well_rares"))));
        stats.add(new DisplayLine(bold("Souls Gathered: ", skyWars.optInt("souls_gathered"))));
        stats.add(new DisplayLine(bold("Souls Purchased: ", skyWars.optInt("paid_souls"))));

        int skywars_time_played = skyWars.optInt("time_played");
        int skywars_tmp_days = 0;

        //SKYWARS_TIME
        while (skywars_time_played > 24 * 60 * 60) {
            skywars_tmp_days++;
            skywars_time_played -= 24 * 60 * 60;
        }
        stats.add(new DisplayLine(""));
        stats.add(new DisplayLine(bold("Time played: (DD, HH:MM:SS): ",
            skywars_tmp_days + ", " + hhmmss.format(new Date(1000 * skywars_time_played)))));

        stats.add(new DisplayLine(""));
        stats.add(new DisplayLine(""));
        final String[] skywars_modes = {"Ranked", "Solo Normal", "Solo Insane", "Team Normal",
            "Team Insane", "Mega"};

        List<String[]> lines = new ArrayList<>();

        lines.add(new String[]{
            "Mode", "Kills", "Wins", "Deaths", "Losses", "K/D", "W/L"
        });

        for (String front : skywars_modes) {
            String api = front.toLowerCase().replace(" ", "_");
            lines.add(new String[]{front,
                String.valueOf(skyWars.optInt("kills_" + api)),
                String.valueOf(skyWars.optInt("wins_" + api)),
                String.valueOf(skyWars.optInt("deaths_" + api)),
                String.valueOf(skyWars.optInt("losses_" + api)),
                WebsiteUtils.buildRatio(skyWars.optInt("kills_" + api), skyWars.optInt("deaths_" + api)),
                WebsiteUtils.buildRatio(skyWars.optInt("wins_" + api), skyWars.optInt("losses_" + api))});
        }

        stats.add(new DisplayTable(lines));

        return stats;
    }
}
