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

public class ArenaStats extends AbstractHypixelStats {
    @Override
    public String getImage() {
        return "Arena-64";
    }

    @Override
    public String getName() {
        return GameType.ARENA.getName();
    }

    @Override
    public GameType getGameType() {
        return GameType.ARENA;
    }

    @Override
    public List<StatsDisplayItem> getPreview(HypixelApiPlayer player) {
        ArrayList<StatsDisplayItem> items = new ArrayList<>();
        JsonHolder arena = player.getStats(GameType.ARENA);
        items.add(new DisplayLine(bold("Coins: ", arena.optInt("coins"))));
        int kills = arena.optInt("kills_1v1") + arena.optInt("kills_2v2") + arena.optInt("kills_4v4");
        items.add(new DisplayLine(bold("Kills: ", kills), Color.WHITE.getRGB()));
        int deaths = arena.optInt("deaths_1v1") + arena.optInt("deaths_2v2") + arena.optInt("deaths_4v4");
        items.add(new DisplayLine(bold("Deaths: ", deaths), Color.WHITE.getRGB()));
        int wins = arena.optInt("wins_1v1") + arena.optInt("wins_2v2") + arena.optInt("wins_4v4");
        items.add(new DisplayLine(bold("Wins: ", wins), Color.WHITE.getRGB()));
        int losses = arena.optInt("losses_1v1") + arena.optInt("losses_2v2") + arena.optInt("losses_4v4");
        items.add(new DisplayLine(bold("Losses: ", losses), Color.WHITE.getRGB()));
        items.add(new DisplayLine(bold("K/D: ", WebsiteUtils.buildRatio(kills, deaths)), Color.WHITE.getRGB()));
        items.add(new DisplayLine(bold("W/L: ", WebsiteUtils.buildRatio(wins, losses)), Color.WHITE.getRGB()));
        return items;
    }

    @Override
    public List<StatsDisplayItem> getDeepStats(HypixelApiPlayer player) {
        List<StatsDisplayItem> items = getPreview(player);
        items.add(new DisplayLine(""));
        List<String[]> tableItems = new ArrayList<>();
        JsonHolder arena = player.getStats(GameType.ARENA);
        tableItems.add(new String[]{"Mode", "Kills", "Deaths", "Wins", "Losses", "Healed", "K/D", "W/L"});
        String[] names = new String[]{"1v1", "2v2", "4v4"};
        for (String name : names) {
            tableItems.add(new String[]{
                name,
                WebsiteUtils.comma(arena.optInt("kills_" + name)),
                WebsiteUtils.comma(arena.optInt("deaths_" + name)),
                WebsiteUtils.comma(arena.optInt("wins_" + name)),
                WebsiteUtils.comma(arena.optInt("losses_" + name)),
                WebsiteUtils.comma(arena.optInt("healed_" + name)),
                WebsiteUtils.buildRatio(arena.optInt("kills_" + name), arena.optInt("deaths_" + name)),
                WebsiteUtils.buildRatio(arena.optInt("wins_" + name), arena.optInt("losses_" + name))
            });
        }
        items.add(new DisplayTable(tableItems));
        return items;
    }
}
