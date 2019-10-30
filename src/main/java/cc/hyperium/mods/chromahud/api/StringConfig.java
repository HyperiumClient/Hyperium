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

import java.util.function.Consumer;

public class StringConfig {

    private final Consumer<DisplayItem> load;
    private final Consumer<DisplayItem> draw;
    private String string;

    public StringConfig(String string, Consumer<DisplayItem> load, Consumer<DisplayItem> draw) {
        this.string = string;
        this.load = load;
        this.draw = draw;
    }

    public StringConfig(String string) {
        this.string = string;
        load = (displayItem) -> {
        };
        draw = (displayItem) -> {
        };
    }

    public Consumer<DisplayItem> getLoad() {
        return load;
    }

    public Consumer<DisplayItem> getDraw() {
        return draw;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }
}
