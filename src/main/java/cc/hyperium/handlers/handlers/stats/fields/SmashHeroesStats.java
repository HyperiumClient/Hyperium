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
public class SmashHeroesStats extends AbstractHypixelStats {
    @Override
    public String getImage() {
        return "SmashHeroes-64";
    }

    @Override
    public String getName() {
        return "Smash Heroes";
    }

    @Override
    public GameType getGameType() {
        return GameType.SUPER_SMASH;
    }

    @Override
    public List<StatsDisplayItem> getPreview(HypixelApiPlayer player) {
        List<StatsDisplayItem> stats = new ArrayList<>();
        JsonHolder smashHeroes = player.getStats(GameType.SUPER_SMASH);

        stats.add(new DisplayLine(bold("Coins: ", smashHeroes.optInt("coins"))));
        stats.add(new DisplayLine(bold("Kills: ", smashHeroes.optInt("kills"))));
        stats.add(new DisplayLine(bold("Deaths: ", smashHeroes.optInt("deaths"))));
        stats.add(new DisplayLine(bold("Wins: ", smashHeroes.optInt("wins"))));
        stats.add(new DisplayLine(bold("Losses: ", smashHeroes.optInt("losses"))));

        return stats;
    }

    @Override
    public List<StatsDisplayItem> getDeepStats(HypixelApiPlayer player) {
        List<StatsDisplayItem> stats = getPreview(player);
        JsonHolder smashHeroes = player.getStats(GameType.SUPER_SMASH);

        stats.add(new DisplayLine(bold("K/D: ", WebsiteUtils
            .buildRatio(smashHeroes.optInt("kills"), smashHeroes.optInt("deaths")))));
        stats.add(new DisplayLine(bold("W/L: ", WebsiteUtils.buildRatio(smashHeroes.optInt("wins"), smashHeroes.optInt("losses")))));
        stats.add(new DisplayLine(""));
        stats.add(new DisplayLine(bold("Smash Level: ", smashHeroes.optInt("smashLevel"))));

        return stats;
    }
}
