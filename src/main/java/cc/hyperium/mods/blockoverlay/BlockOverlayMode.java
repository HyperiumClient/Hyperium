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

package cc.hyperium.mods.blockoverlay;

public enum BlockOverlayMode {
    NONE("None"),
    DEFAULT("Default"),
    OUTLINE("Outline"),
    FULL("Full");

    private String name;

    BlockOverlayMode(final String name) {
        this.name = name;
    }

    public static BlockOverlayMode getNextMode(final BlockOverlayMode mode) {
        return values()[(mode.ordinal() + 1) % values().length];
    }

    public String getName() {
        return name;
    }
}
