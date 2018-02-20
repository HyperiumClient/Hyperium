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
import java.util.regex.Pattern;

public class TypeJoin extends ToggleBase {

    private Pattern joinPattern = Pattern.compile("(?<player>\\S{1,16})(\\s+)(joined\\.)");

    private boolean enabled = true;

    @Override
    public String getName() {
        return "Join";
    }

    @Override
    public boolean shouldToggle(String message) {
        return this.joinPattern.matcher(message).matches();
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
                "Toggles all join",
                "notification messages",
                "or anything matching",
                "this format",
                "",
                "&ePlayer joined.",
                "",
                "This is good for",
                "people with a large",
                "friends list"
        );
    }
}