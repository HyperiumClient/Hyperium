/*
 *     Hypixel Community Client, Client optimized for Hypixel Network
 *     Copyright (C) 2018  HCC Dev Team
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

package com.hcc.mods.chromahud;


import com.hcc.mods.chromahud.api.ChromaHUDDescription;
import com.hcc.mods.chromahud.api.ChromaHUDParser;
import com.hcc.mods.chromahud.api.DisplayItem;
import com.hcc.mods.chromahud.displayitems.hcc.HypixelDisplay;
import com.hcc.mods.chromahud.displayitems.hcc.LocationDisplay;
import com.hcc.mods.chromahud.displayitems.hcc.RatingDisplay;
import com.hcc.utils.JsonHolder;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mitchellkatz on 1/8/18. Designed for production use on Sk1er.club
 */
public class HCCChromaHudParser implements ChromaHUDParser {
    private HashMap<String, String> names = new HashMap<>();

    public HCCChromaHudParser() {
        names.put("LOCATION", "Location");
        names.put("HYPIXEL", "Hypixel");
        names.put("RATING", "Rating");
    }

    @Override
    public DisplayItem parse(String type, int ord, JsonHolder item) {
        switch (type) {

            case "LOCATION":
                return new LocationDisplay(item, ord);
            case "HYPIXEL":
                return new HypixelDisplay(item, ord);
            case "RATING":
                return new RatingDisplay(item, ord);

        }
        return null;
    }

    @Override
    public Map<String, String> getNames() {

        return names;
    }

    @Override
    public ChromaHUDDescription description() {
        return new ChromaHUDDescription("DEFAULT", "1.0", "HCC", "Default Items in HCC.");
    }
}
