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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import net.hypixel.api.GameType;

/**
 * @author KodingKing
 */
public class PaintballStats extends AbstractHypixelStats {
    @Override
    public String getImage() {
        return "Paintball-64";
    }

    @Override
    public String getName() {
        return "Paintball";
    }

    @Override
    public GameType getGameType() {
        return GameType.PAINTBALL;
    }

    @Override
    public List<StatsDisplayItem> getPreview(HypixelApiPlayer player) {
        List<StatsDisplayItem> stats = new ArrayList<>();
        JsonHolder paintball = player.getStats(GameType.PAINTBALL);

        stats.add(new DisplayLine(bold("Coins: ", paintball.optInt("kills"))));
        stats.add(new DisplayLine(bold("Kills: ", paintball.optInt("kills"))));
        stats.add(new DisplayLine(bold("Wins: ", paintball.optInt("wins"))));
        stats.add(new DisplayLine(bold("Deaths: ", paintball.optInt("deaths"))));

        return stats;
    }

    @Override
    public List<StatsDisplayItem> getDeepStats(HypixelApiPlayer player) {
        List<StatsDisplayItem> stats = getPreview(player);
        JsonHolder paintball = player.getStats(GameType.PAINTBALL);
        SimpleDateFormat hhmmss = new SimpleDateFormat("HH:mm:ss");

        stats.add(new DisplayLine(""));
        stats.add(new DisplayLine(bold("Shots fired: ", paintball.optInt("shots_fired"))));
        stats.add(new DisplayLine(bold("K/D: ", WebsiteUtils.buildRatio(paintball.optInt("kills"), paintball.optInt("deaths")))));
        stats.add(new DisplayLine(bold("Shot / kill: ", WebsiteUtils.buildRatio(paintball.optInt("shots_fired"), paintball.optInt("kills")))));

        hhmmss.setTimeZone(TimeZone.getTimeZone("GMT"));
        stats.add(new DisplayLine(bold("Time in forcefield (HH-MM-SS): ", hhmmss.format(new Date(1000 * paintball.optInt("forcefieldTime"))))));

        return stats;
    }
}
