/*
 * Hyperium Client, Free client with huds and popular mod
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

package me.semx11.autotip.util;

import org.apache.commons.lang3.StringUtils;

public enum MessageOption {
    SHOWN, COMPACT, HIDDEN;

    public MessageOption next() {
        switch (this) {
            case SHOWN:
                return COMPACT;
            case COMPACT:
                return HIDDEN;
            case HIDDEN:
                return SHOWN;
            default:
                return SHOWN;
        }
    }

    @Override
    public String toString() {
        ChatColor color = ChatColor.GREEN;
        switch (this) {
            case COMPACT:
                color = ChatColor.YELLOW;
                break;
            case HIDDEN:
                color = ChatColor.RED;
                break;
        }
        return color + StringUtils.capitalize(this.name().toLowerCase());
    }
}