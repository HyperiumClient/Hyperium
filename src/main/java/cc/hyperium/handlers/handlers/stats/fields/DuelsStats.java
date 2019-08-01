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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DuelsStats extends AbstractHypixelStats {
    private final String[] tournaments = new String[]{"sw_tournament", "uhc_tournament", "sumo_tournament"};
    private final String[] tournament_names = new String[]{"SkyWars Tournament", "UHC Tournament", "Sumo Tournament"};

    @Override
    public String getImage() {
        return "Duels-64";
    }

    @Override
    public String getName() {
        return "Duels";
    }

    @Override
    public GameType getGameType() {
        return GameType.DUELS;
    }

    @Override
    public List<StatsDisplayItem> getPreview(HypixelApiPlayer player) {
        List<StatsDisplayItem> preview = super.getPreview(player);
        preview.add(new DisplayLine(""));
        JsonHolder stats = player.getStats(GameType.DUELS);
        preview.add(new DisplayLine(bold("Tournament Wins: ", Arrays.stream(tournaments).mapToInt(tournament -> stats.optInt(tournament + "_wins")).sum())));
        return preview;
    }

    @Override
    public List<StatsDisplayItem> getDeepStats(HypixelApiPlayer player) {
        List<StatsDisplayItem> preview = getPreview(player);
        JsonHolder stats = player.getStats(GameType.DUELS);
        preview.add(new DisplayLine(""));
        String[] modes = new String[]{"uhc_duel", "uhc_doubles", "sw_duel", "sw_doubles", "classic_duel", "op_duel", "combo", "mw_duel", "mw_doubles", "blitz_duels", "potion_duel", "sumo_duel"};
        String[] names = new String[]{"UHC Solo", "UHC Doubles", "SW Solo", "SW Doubles", "Classic Duel", "OP duel", "Combo", "MegaWalls Solo", "MegaWalls Doubles", "Blitz Solo", "Potion Duel", "Sumo"};
        List<String[]> lines = new ArrayList<>();
        lines.add(new String[]{"Mode", "Kills", "Wins", "Deaths", "Losses", "K/D", "W/L"});
        for (int i = 0; i < tournaments.length; i++) {
            String mode = tournaments[i];
            lines.add(new String[]{tournament_names[i],
                WebsiteUtils.comma(stats.optInt(mode + "_kills")),
                WebsiteUtils.comma(stats.optInt(mode + "_wins")),
                WebsiteUtils.comma(stats.optInt(mode + "_deaths")),
                WebsiteUtils.comma(stats.optInt(mode + "_losses")),
                WebsiteUtils.buildRatio(stats.optInt(mode + "_kills"), stats.optInt(mode + "_deaths")),
                WebsiteUtils.buildRatio(stats.optInt(mode + "_wins"), stats.optInt(mode + "_losses"))
            });
        }

        preview.add(new DisplayTable(lines));
        preview.add(new DisplayLine(""));
        lines = new ArrayList<>();
        lines.add(new String[]{"Mode", "Kills", "Wins", "Deaths", "Losses", "K/D", "W/L", "Best Winstreak"});
        for (int i = 0; i < modes.length; i++) {
            String mode = modes[i];
            lines.add(new String[]{names[i],
                WebsiteUtils.comma(stats.optInt(mode + "_kills")),
                WebsiteUtils.comma(stats.optInt(mode + "_wins")),
                WebsiteUtils.comma(stats.optInt(mode + "_deaths")),
                WebsiteUtils.comma(stats.optInt(mode + "_losses")),
                WebsiteUtils.buildRatio(stats.optInt(mode + "_kills"), stats.optInt(mode + "_deaths")),
                WebsiteUtils.buildRatio(stats.optInt(mode + "_wins"), stats.optInt(mode + "_losses")),
                WebsiteUtils.comma(stats.optInt("duels_winstreak_best_" + mode)),
            });
        }
        preview.add(new DisplayTable(lines));
        return preview;
    }
}
