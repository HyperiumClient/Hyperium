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
import cc.hyperium.mods.togglechat.toggles.ToggleBaseHandler;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TypeGlobal extends ToggleBase {

    private final ToggleBaseHandler base;

    private final Pattern chatPattern = Pattern.compile("(?<rank>\\[.+] )?(?<player>\\S{1,16}): (?<message>.*)");

    private boolean enabled = true;

    public TypeGlobal(ToggleBaseHandler handler) {
        base = handler;
    }

    @Override
    public String getName() {
        return "Global";
    }

    @Override
    public String getDisplayName() {
        return "Global Chat: %s";
    }

    @Override
    public boolean shouldToggle(String message) {
        // A system to prevent accidentally toggling UHC or bedwars chat
        if (base.getToggle("special") != null) {
            ToggleBase base = this.base.getToggle("special");

            if (base.isEnabled() && base.shouldToggle(message)) {
                return false;
            }
        }

        Matcher matcher = chatPattern.matcher(message);

        return matcher.matches() && isNotOtherChat(matcher);
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
            "Turns all general player",
            "chat on or off",
            "",
            "These are the formats",
            "&7Player: Hi",
            "&a[VIP] Player&r: Hi",
            "&a[VIP&6+&a] Player&r: Hi",
            "&b[MVP] Player&r: Hi",
            "&b[MVP&c+&b] Player&r: Hi",
            "",
            "Useful to prevent spam",
            "or any unwanted chat",
            "messages"
        );
    }

    private boolean isNotOtherChat(Matcher input) {
        String rank;

        try {
            rank = input.group("rank");
        } catch (Exception ex) {
            return true;
        }

        switch (rank) {
            case "[TEAM] ":
            case "[SHOUT] ":
            case "[SPECTATOR] ":
                return false;
        }
        return true;
    }
}
