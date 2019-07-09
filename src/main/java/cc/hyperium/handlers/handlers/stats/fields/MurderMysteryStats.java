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
public class MurderMysteryStats extends AbstractHypixelStats {

    @Override
    public String getImage() {
        return "MurderMystery-64";
    }

    @Override
    public String getName() {
        return "Murder Mystery";
    }

    @Override
    public GameType getGameType() {
        return GameType.MURDER_MYSTERY;
    }

    @Override
    public List<StatsDisplayItem> getPreview(HypixelApiPlayer player) {
        List<StatsDisplayItem> preview = new ArrayList<>();
        JsonHolder stats = player.getStats(GameType.MURDER_MYSTERY);
        preview.add(new DisplayLine(""));

        preview.add(new DisplayLine("General"));
        preview.add(new DisplayLine(bold("Coins: ", stats.optInt("coins"))));
        preview.add(new DisplayLine(""));
        preview.add(new DisplayLine("Overall Stats"));
        preview.add(new DisplayLine(bold("Kills: ", stats.optInt("kills"))));
        preview.add(new DisplayLine(bold("Wins: ", stats.optInt("wins"))));
        preview.add(new DisplayLine(bold("Deaths: ", stats.optInt("deaths"))));
        preview.add(new DisplayLine(bold("Bow Kills: ", stats.optInt("bow_kills"))));
        preview.add(new DisplayLine(
            bold("K/D: ", WebsiteUtils.buildRatio(stats.optInt("kills"), stats.optInt("deaths")))));
        preview.add(new DisplayLine(
            bold("W/L: ", WebsiteUtils.buildRatio(stats.optInt("wins"), stats.optInt("deaths")))));
        return preview;
    }

    @Override
    public List<StatsDisplayItem> getDeepStats(HypixelApiPlayer player) {
        List<StatsDisplayItem> preview = getPreview(player);
        JsonHolder stats = player.getStats(GameType.MURDER_MYSTERY);

        preview.add(new DisplayLine(""));
        preview.add(new DisplayLine("Classic Stats"));
        preview.add(new DisplayLine(bold("Kills: ", stats.optInt("kills_MURDER_CLASSIC"))));
        preview.add(new DisplayLine(bold("Wins: ", stats.optInt("wins_MURDER_CLASSIC"))));
        preview.add(new DisplayLine(bold("Deaths: ", stats.optInt("deaths_MURDER_CLASSIC"))));
        preview.add(new DisplayLine(bold("Bow Kills: ", stats.optInt("bow_kills"))));
        preview.add(new DisplayLine(bold("K/D: ", WebsiteUtils
            .buildRatio(stats.optInt("kills_MURDER_CLASSIC"), stats.optInt("deaths_MURDER_CLASSIC")))));
        preview.add(new DisplayLine(bold("W/L: ", WebsiteUtils
            .buildRatio(stats.optInt("wins_MURDER_CLASSIC"), stats.optInt("deaths_MURDER_CLASSIC")))));
        preview.add(new DisplayLine(""));
        preview.add(new DisplayLine("Hardcore Stats"));
        preview.add(new DisplayLine(bold("Kills: ", stats.optInt("kills_MURDER_HARDCORE"))));
        preview.add(new DisplayLine(bold("Wins: ", stats.optInt("wins_MURDER_HARDCORE"))));
        preview.add(new DisplayLine(bold("Deaths: ", stats.optInt("deaths_MURDER_HARDCORE"))));
        preview.add(new DisplayLine(bold("Bow Kills: ", stats.optInt("bow_kills"))));
        preview.add(new DisplayLine(bold("K/D: ", WebsiteUtils
            .buildRatio(stats.optInt("kills_MURDER_HARDCORE"),
                stats.optInt("deaths_MURDER_HARDCORE")))));
        preview.add(new DisplayLine(bold("W/L: ", WebsiteUtils
            .buildRatio(stats.optInt("wins_MURDER_HARDCORE"), stats.optInt("deaths_MURDER_HARDCORE")))));
        preview.add(new DisplayLine(""));
        preview.add(new DisplayLine("Assassins Stats"));
        preview.add(new DisplayLine(bold("Kills: ", stats.optInt("kills_MURDER_ASSASSINS"))));
        preview.add(new DisplayLine(bold("Wins: ", stats.optInt("wins_MURDER_ASSASSINS"))));
        preview.add(new DisplayLine(bold("Deaths: ", stats.optInt("deaths_MURDER_ASSASSINS"))));
        preview.add(new DisplayLine(bold("Bow Kills: ", stats.optInt("bow_kills"))));
        preview.add(new DisplayLine(bold("K/D: ", WebsiteUtils
            .buildRatio(stats.optInt("kills_MURDER_ASSASSINS"),
                stats.optInt("deaths_MURDER_ASSASSINS")))));
        preview.add(new DisplayLine(bold("W/L: ", WebsiteUtils
            .buildRatio(stats.optInt("wins_MURDER_ASSASSINS"),
                stats.optInt("deaths_MURDER_ASSASSINS")))));
        String[] murderMysteryModes = {"All", "Classic", "Assassins", "Hardcore"};
        String[] murderMysteryNames = {"", "_MURDER_CLASSIC", "_MURDER_ASSASIANS", "_MURDER_HARDCORE"};
        List<String[]> lines = new ArrayList<>();
        preview.add(new DisplayLine(""));

        lines.add(new String[]{"Mode", "Kills", "Wins", "Deaths", "Hero", "Bow Kills"});
        for (int i = 0; i < murderMysteryModes.length; i++) {
            String name = murderMysteryModes[i];
            String seed = murderMysteryNames[i];
            lines.add(new String[]{name,
                String.valueOf(stats.optInt("kills" + seed)),
                String.valueOf(stats.optInt("wins" + seed)),
                String.valueOf(stats.optInt("deaths" + seed)),
                String.valueOf(stats.optInt("was_hero" + seed)),
                String.valueOf(stats.optInt("bow_kills" + seed))});
        }
        preview.add(new DisplayTable(lines));

        return preview;
    }
}
