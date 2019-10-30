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

public class TypeLobbyJoin extends ToggleBase {

    private boolean enabled = true;

    @Override
    public String getName() {
        return "Lobby Join";
    }

    @Override
    public String getDisplayName() {
        return "Lobby join: %s";
    }

    @Override
    public boolean shouldToggle(String message) {
        return message.endsWith("joined the lobby!") || // normal join messages
            (message.contains("joined the lobby") && message.startsWith(" >>>")) || // mvp++ join messages
            message.endsWith("spooked in the lobby!"); // halloween join messages
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
            "Removes all &bMVP&c+",
            "and &6MVP&c++&r lobby join",
            "messages",
            "",
            "Such as:",
            "&b[MVP&c+&b] I &6joined the lobby!",
            "&6[MVP&c++&6] I joined the lobby!"
        );
    }
}
