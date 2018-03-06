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

package cc.hyperium.mods.chromahud.api;


import cc.hyperium.mods.chromahud.ChromaHUDApi;
import cc.hyperium.utils.JsonHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mitchellkatz on 1/8/18. Designed for production use on Sk1er.club
 */
public abstract class DisplayItem extends Dimension {

    private int ordinal;

    public JsonHolder getData() {
        save();
        return data;
    }
    public void save() {

    }
    protected JsonHolder data;

    public DisplayItem(JsonHolder data, int ordinal) {
        this.data = data;
        this.ordinal = ordinal;

    }

    public String name() {
        return ChromaHUDApi.getInstance().getName(data.optString("type"));
    }

    public int getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(int ordinal) {
        this.ordinal = ordinal;
    }

    public abstract void draw(int x, double y, boolean config);

    public List<ButtonConfig> configOptions() {
        return new ArrayList<>();
    }

    public String getType() {
        return data.optString("type");
    }
}
