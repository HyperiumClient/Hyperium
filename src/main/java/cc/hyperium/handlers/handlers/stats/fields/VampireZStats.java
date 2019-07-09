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
public class VampireZStats extends AbstractHypixelStats {
    @Override
    public GameType getGameType() {
        return GameType.VAMPIREZ;
    }

    @Override
    public String getImage() {
        return "VampireZ-64";
    }

    @Override
    public String getName() {
        return "VampireZ";
    }

    @Override
    public List<StatsDisplayItem> getPreview(HypixelApiPlayer player) {
        List<StatsDisplayItem> stats = new ArrayList<>();
        JsonHolder vampireZ = player.getStats(GameType.VAMPIREZ);

        stats.add(new DisplayLine(bold("Coins: ", vampireZ.optInt("coins"))));

        return stats;
    }

    @Override
    public List<StatsDisplayItem> getDeepStats(HypixelApiPlayer player) {
        List<StatsDisplayItem> stats = getPreview(player);
        JsonHolder vampireZ = player.getStats(GameType.VAMPIREZ);

        stats.add(new DisplayLine(bold("Human Wins: ", vampireZ.optInt("human_wins"))));
        stats.add(new DisplayLine(bold("Human Kills: ", vampireZ.optInt("human_kills"))));
        stats.add(new DisplayLine(bold("Human Deaths: ", vampireZ.optInt("human_deaths"))));
        stats.add(new DisplayLine(bold("Zombie Kills: ", vampireZ.optInt("zombie_kills"))));
        stats.add(new DisplayLine(bold("Vampire Kills: ", vampireZ.optInt("vampire_kills"))));
        stats.add(new DisplayLine(bold("Vampire wins: ", vampireZ.optInt("vampire_wins"))));
        stats.add(new DisplayLine(bold("Vampire Deaths: ", vampireZ.optInt("vampire_deaths"))));

        return stats;
    }
}
