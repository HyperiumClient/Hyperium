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
import cc.hyperium.handlers.handlers.stats.display.StatsDisplayItem;
import club.sk1er.website.api.requests.HypixelApiPlayer;
import net.hypixel.api.GameType;

import java.util.List;

public class CrazyWallsStats extends AbstractHypixelStats {
    @Override
    public String getImage() {
        return "CrazyWalls-64";
    }

    @Override
    public String getName() {
        return "Crazy Walls";
    }

    @Override
    public GameType getGameType() {
        return GameType.TRUE_COMBAT;
    }

    //I checked the api, there isn't much other useful info
    @Override
    public List<StatsDisplayItem> getDeepStats(HypixelApiPlayer player) {

        return super.getPreview(player);
    }
}
