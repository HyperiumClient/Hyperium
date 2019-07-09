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
import net.hypixel.api.GameType;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class CVCStats extends AbstractHypixelStats {
    @Override
    public String getImage() {
        return "CVC-64";
    }

    @Override
    public String getName() {
        return "Cops And Crims";
    }

    @Override
    public GameType getGameType() {
        return GameType.MCGO;
    }

    @Override
    public List<StatsDisplayItem> getPreview(HypixelApiPlayer player) {
        JsonHolder stats = player.getStats(GameType.MCGO);
        ArrayList<StatsDisplayItem> items = new ArrayList<>();
        items.add(new DisplayLine(bold("Coins: ", stats.optInt("coins")), Color.WHITE.getRGB()));
        items.add(new DisplayLine(bold("Kills: ", stats.optInt("kills")), Color.WHITE.getRGB()));
        items.add(new DisplayLine(bold("Headshots: ", stats.optInt("headshot_kills")), Color.WHITE.getRGB()));
        items.add(new DisplayLine(bold("Deaths: ", stats.optInt("deaths")), Color.WHITE.getRGB()));
        items.add(new DisplayLine(bold("Wins: ", stats.optInt("game_wins")), Color.WHITE.getRGB()));
        items.add(new DisplayLine(bold("Losses: ", stats.optInt("losses")), Color.WHITE.getRGB()));
        items.add(new DisplayLine(bold("K/D: ", WebsiteUtils.buildRatio(stats.optInt("kills"), stats.optInt("deaths"))), Color.WHITE.getRGB()));
        return items;
    }

    @Override
    public List<StatsDisplayItem> getDeepStats(HypixelApiPlayer player) {
        List<StatsDisplayItem> preview = getPreview(player);
        JsonHolder stats = player.getStats(GameType.MCGO);
        preview.add(new DisplayLine(""));
        preview.add(new DisplayLine(bold("Bombs Planted: ", stats.optInt("bombs_planted")), Color.WHITE.getRGB()));
        preview.add(new DisplayLine(bold("Bombs Defused: ", stats.optInt("bombs_defused")), Color.WHITE.getRGB()));
        preview.add(new DisplayLine(bold("Round Wins: ", stats.optInt("round_wins")), Color.WHITE.getRGB()));
        preview.add(new DisplayLine(bold("Shots Fired: ", stats.optInt("shots_fired")), Color.WHITE.getRGB()));

        return preview;
    }
}
