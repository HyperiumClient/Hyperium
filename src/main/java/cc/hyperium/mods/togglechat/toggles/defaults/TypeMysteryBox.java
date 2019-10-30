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

package cc.hyperium.mods.togglechat.toggles.defaults;

import cc.hyperium.mods.togglechat.toggles.ToggleBase;

import java.util.LinkedList;
import java.util.regex.Pattern;

public class TypeMysteryBox extends ToggleBase {

    private final Pattern mysteryPattern = Pattern.compile("(?<player>\\S{1,16}) found a (?<star>\\S{1,5}) Mystery Box!");
    private final Pattern mysteryFoundPattern = Pattern.compile("\\[Mystery Box] (?<player>\\S{1,16}) found a (?<thing>.*)!");

    private boolean enabled = true;

    @Override
    public String getName() {
        return "Mystery box";
    }

    @Override
    public String getDisplayName() {
        return "Mystery Box: %s";
    }

    @Override
    public boolean shouldToggle(String message) {
        return mysteryPattern.matcher(message).matches() || mysteryFoundPattern.matcher(message).matches();
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public LinkedList<String> getDescription() {
        return asLinked(
            "Turns finding mystery box",
            "messages on or off",
            "",
            "&7I &rfound a &e\u2730\u2730 &bMystery Box&r!",
            "&b[Mystery Box] &7I &rfound a &6Dab&r!",
            "",
            "Useful to prevent those",
            "weird box opening messages"
        );
    }
}
