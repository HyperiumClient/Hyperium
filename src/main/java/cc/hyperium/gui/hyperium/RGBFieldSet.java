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

package cc.hyperium.gui.hyperium;

import cc.hyperium.config.Category;

import java.lang.reflect.Field;

public class RGBFieldSet {
    private Field red;
    private Field green;
    private Field blue;
    private Category category;
    private boolean mods;
    private Object parentObj;

    public Object getParentObj() {
        return parentObj;
    }

    public RGBFieldSet(Field red, Field green, Field blue, Category category, boolean mods, Object parentObj) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.category = category;
        this.mods = mods;
        this.parentObj = parentObj;

    }

    public boolean isMods() {
        return mods;
    }

    public Category getCategory() {
        return category;
    }

    public Field getRed() {
        return red;
    }

    public Field getGreen() {
        return green;
    }

    public Field getBlue() {
        return blue;
    }
}
