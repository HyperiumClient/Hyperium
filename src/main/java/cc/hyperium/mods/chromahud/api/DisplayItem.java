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

package cc.hyperium.mods.chromahud.api;


import cc.hyperium.mods.chromahud.ChromaHUDApi;
import cc.hyperium.utils.JsonHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sk1er
 */
public abstract class DisplayItem extends Dimension {

    protected final JsonHolder data;
    private int ordinal;

    public DisplayItem(JsonHolder data, int ordinal) {
        this.data = data;
        this.ordinal = ordinal;
    }

    public JsonHolder getData() {
        save();
        return data;
    }

    public void save() {

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
