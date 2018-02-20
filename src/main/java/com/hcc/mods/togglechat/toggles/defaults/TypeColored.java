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

package com.hcc.mods.togglechat.toggles.defaults;

import com.hcc.mods.togglechat.toggles.ToggleBase;

import java.util.LinkedList;

public class TypeColored extends ToggleBase {

    private boolean enabled = true;

    @Override
    public String getName() {
        return "Colored team";
    }

    @Override
    public boolean shouldToggle(String message) {
        return message.startsWith("[BLUE] ") ||
                message.startsWith("[YELLOW] ") ||
                message.startsWith("[GREEN] ") ||
                message.startsWith("[RED] ") ||
                message.startsWith("[WHITE] ") ||
                message.startsWith("[PURPLE] "
        );
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public LinkedList<String> getDescription() {
        return asLinked(
                "Disables colored chat",
                "messages in multiple games",
                "",
                "Toggles things like these",
                "&e[YELLOW]",
                "&d[PURPLE]",
                "&a[GREEN]",
                "&c[RED]",
                "",
                "This is good for games",
                "such as paintball"
        );
    }
}
