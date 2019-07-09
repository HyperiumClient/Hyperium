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
import net.hypixel.api.GameType;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class BedWarsStats extends AbstractHypixelStats {
    @Override
    public String getName() {
        return "Bedwars";
    }

    @Override
    public String getImage() {
        return "BedWars-64";
    }

    @Override
    public GameType getGameType() {
        return GameType.BEDWARS;
    }

    @Override
    public List<StatsDisplayItem> getPreview(HypixelApiPlayer player) {
        ArrayList<StatsDisplayItem> items = new ArrayList<>();
        JsonHolder bedwars = player.getStats(GameType.BEDWARS);
        items.add(new DisplayLine(bold("Coins: ", bedwars.optInt("coins")), Color.WHITE.getRGB()));
        items.add(new DisplayLine(bold("Kills: ", bedwars.optInt("kills_bedwars")), Color.WHITE.getRGB()));
        items.add(new DisplayLine(bold("Deaths: ", bedwars.optInt("deaths_bedwars")), Color.WHITE.getRGB()));
        items.add(new DisplayLine(bold("Wins: ", bedwars.optInt("wins_bedwars")), Color.WHITE.getRGB()));
        items.add(new DisplayLine(bold("Losses: ", bedwars.optInt("losses_bedwars")), Color.WHITE.getRGB()));
        items.add(new DisplayLine(bold("K/D: ", WebsiteUtils.buildRatio(bedwars.optInt("kills_bedwars"), bedwars.optInt("deaths_bedwars"))), Color.WHITE.getRGB()));
        items.add(new DisplayLine(bold("W/L: ", WebsiteUtils.buildRatio(bedwars.optInt("wins_bedwars"), bedwars.optInt("losses_bedwars"))), Color.WHITE.getRGB()));
        items.add(new DisplayLine(""));
        items.add(new DisplayLine(bold("Bedwars level: ", WebsiteUtils.getBedwarsLevel(bedwars.optInt("Experience") + bedwars.optInt("Experience_new")))));
        items.add(new DisplayLine(bold("Beds Broken: ", bedwars.optInt("beds_broken_bedwars"))));
        items.add(new DisplayLine(""));
        items.add(new DisplayLine(bold("Final Kills: ", bedwars.optInt("final_kills_bedwars"))));
        items.add(new DisplayLine(bold("Final Deaths: ", bedwars.optInt("final_deaths_bedwars"))));
        items.add(new DisplayLine(bold("Final K/D: ", WebsiteUtils.buildRatio(bedwars.optInt("final_kills_bedwars"), bedwars.optInt("final_deaths_bedwars")))));
        items.add(new DisplayLine(bold("Final Kill / Normal deaths: ", WebsiteUtils.buildRatio(bedwars.optInt("final_kills_bedwars"), bedwars.optInt("deaths_bedwars")))));
        return items;
    }

    @Override
    public List<StatsDisplayItem> getDeepStats(HypixelApiPlayer player) {
        List<StatsDisplayItem> preview = getPreview(player);
        preview.add(new DisplayLine(""));
        List<String[]> lines = new ArrayList<>();
        JsonHolder bedwars = player.getStats(GameType.BEDWARS);
        String[] bedwarsnames = {"Solo", "Doubles", "3v3v3v3", "4v4v4v4"};
        String[] bedwarsBackend = {"eight_one", "eight_two", "four_three", "four_four"};
        lines.add(new String[]{"Mode", "Kills", "Deaths", "Wins", "Losses", "Final Kills", "Final Deaths", "Beds Broken", "K/D", "Final K/D", "W/L"});
        for (int i = 0; i < bedwarsnames.length; i++) {
            lines.add(new String[]{bedwarsnames[i],
                WebsiteUtils.comma(bedwars.optInt(bedwarsBackend[i] + "_kills_bedwars")),
                WebsiteUtils.comma(bedwars.optInt(bedwarsBackend[i] + "_deaths_bedwars")),
                WebsiteUtils.comma(bedwars.optInt(bedwarsBackend[i] + "_wins_bedwars")),
                WebsiteUtils.comma(bedwars.optInt(bedwarsBackend[i] + "_losses_bedwars")),
                WebsiteUtils.comma(bedwars.optInt(bedwarsBackend[i] + "_final_kills_bedwars")),
                WebsiteUtils.comma(bedwars.optInt(bedwarsBackend[i] + "_final_deaths_bedwars")),
                WebsiteUtils.comma(bedwars.optInt(bedwarsBackend[i] + "_beds_broken_bedwars")),
                WebsiteUtils.buildRatio(bedwars.optInt(bedwarsBackend[i] + "_kills_bedwars"), bedwars.optInt(bedwarsBackend[i] + "_deaths_bedwars")),
                WebsiteUtils.buildRatio(bedwars.optInt(bedwarsBackend[i] + "_final_kills_bedwars"), bedwars.optInt(bedwarsBackend[i] + "_final_deaths_bedwars")),
                WebsiteUtils.buildRatio(bedwars.optInt(bedwarsBackend[i] + "_wins_bedwars"), bedwars.optInt(bedwarsBackend[i] + "_losses_bedwars"))
            });
        }
        preview.add(new DisplayTable(lines));
        return preview;
    }
}
