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
public class TKRStats extends AbstractHypixelStats {

    @Override
    public String getImage() {
        return "TurboKartRacers-64";
    }

    @Override
    public String getName() {
        return "Turbo Kart Racers";
    }

    @Override
    public GameType getGameType() {
        return GameType.GINGERBREAD;
    }

    @Override
    public List<StatsDisplayItem> getPreview(HypixelApiPlayer player) {
        List<StatsDisplayItem> stats = new ArrayList<>();
        JsonHolder gingerBread = player.getStats(GameType.GINGERBREAD);

        stats.add(new DisplayLine(bold("Coins: ", gingerBread.optInt("coins"))));
        stats.add(new DisplayLine(bold("Gold Trophies: ", gingerBread.optInt("gold_trophy"))));
        stats.add(new DisplayLine(bold("Silver Trophies: ", gingerBread.optInt("silver_trophy"))));
        stats.add(new DisplayLine(bold("Bronze Trophies: ", gingerBread.optInt("bronze_trophy"))));
        stats.add(new DisplayLine(bold("Laps Completed: ", gingerBread.optInt("laps_completed"))));
        stats.add(new DisplayLine(bold("Coins Picked Up: ", gingerBread.optInt("coins_picked_up"))));
        stats.add(new DisplayLine(bold("Power Ups Picked Up: ", gingerBread.optInt("box_pickups"))));

        return stats;
    }

    @Override
    public List<StatsDisplayItem> getDeepStats(HypixelApiPlayer player) {
        return getPreview(player);
    }
}
