/*
 *  Hypixel Community Client, Client optimized for Hypixel Network
 *     Copyright (C) 2018  Hyperium Dev Team
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.mods.chromahud.displayitems.hyperium;

import cc.hyperium.mods.chromahud.ElementRenderer;
import cc.hyperium.mods.chromahud.api.Dimension;
import cc.hyperium.mods.chromahud.api.DisplayItem;
import cc.hyperium.mods.statistics.GeneralStatisticsTracking;
import cc.hyperium.utils.JsonHolder;

/**
 * Created by mitchellkatz on 2/26/18. Designed for production use on Sk1er.club
 */
public class CoinsDisplay extends DisplayItem {


    public CoinsDisplay(JsonHolder data, int ordinal) {
        super(data, ordinal);

    }

    @Override
    public Dimension draw(int x, double y, boolean config) {

        String render = null;
        if (data.optInt("state") == 0) {
            render = "Daily Coins: " + GeneralStatisticsTracking.dailyCoins;
        }
        if (data.optInt("state") == 1) {
            render = "Monthly Coins: " + GeneralStatisticsTracking.monthlyCoins;
        }
        if (data.optInt("state") == 2) {
            render = "Lifetime Coins: " + GeneralStatisticsTracking.lifetimeCoins;
        }
        if (render == null) {
            render = "Error, " + data.optInt("type") + " invalid state";
        }
        ElementRenderer.draw(x, y, render);
        return new Dimension(config ? ElementRenderer.getFontRenderer().getStringWidth(render) : 0, 10);
    }
}
