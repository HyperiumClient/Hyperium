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

package cc.hyperium.mods.togglechat.toggles;

import cc.hyperium.mods.togglechat.toggles.defaults.*;

import java.util.LinkedHashMap;
import java.util.Map;

public class ToggleBaseHandler {

    private final Map<String, ToggleBase> toggles = new LinkedHashMap<>();

    public Map<String, ToggleBase> getToggles() {
        return new LinkedHashMap<>(toggles);
    }

    /**
     * Adds the developers own ToggleBase
     *
     * @param toggleBase the developers toggle
     */
    public void addToggle(ToggleBase toggleBase) {
        if (toggleBase != null && toggleBase.getName() != null) {
            toggles.put(toggleBase.getName().toLowerCase().replace(" ", "_"), toggleBase);
        }
    }

    /**
     * Run through all bases and check if the
     * given text should be toggled
     *
     * @param input text to test
     * @return the formatted text
     */
    public boolean shouldToggle(String input) {
        return toggles.values().stream().anyMatch(parser -> !parser.isEnabled() && parser.shouldToggle(input));
    }

    /**
     * Clears all toggles and readds default ones
     */
    public void remake() {
        toggles.clear();
        toggles.put("ads", new TypeAds());
        toggles.put("team", new TypeTeam());
        toggles.put("join", new TypeJoin());
        toggles.put("leave", new TypeLeave());
        toggles.put("guild", new TypeGuild());
        toggles.put("party", new TypeParty());
        toggles.put("shout", new TypeShout());
        toggles.put("soul", new TypeSoulWell());
        toggles.put("housing", new TypeHousing());
        toggles.put("messages", new TypeMessages());
        toggles.put("global", new TypeGlobal(this));
        toggles.put("ez_messages", new TypeEasy());
        toggles.put("special", new TypeSpecial());
        toggles.put("colored_team", new TypeColored());
        toggles.put("party_invites", new TypePartyInvites());
        toggles.put("build_battle", new TypeBuildBattle());
        toggles.put("mystery_box", new TypeMysteryBox());
        toggles.put("spectator", new TypeSpectator());
        toggles.put("lobby_join", new TypeLobbyJoin());
        toggles.put("separators", new TypeMessageSeparator());
        toggles.put("friend_requests", new TypeFriendRequests());
        toggles.put("officer", new TypeOfficer());
        toggles.put("generosity_messages", new TypeAfterGift());
        toggles.put("boop", new TypeBoop());
    }

    /**
     * Gets a toggle by the given name, may return null
     *
     * @param name the toggle's name
     * @return a ToggleBase instance if found, or else null
     */
    public ToggleBase getToggle(String name) {
        return toggles.getOrDefault(name, null);
    }

    /**
     * Checks to see if the registered parsers contains a parser
     * with the given name.
     *
     * @param name The toggle's name to test for
     * @return true if it is registered
     */
    public boolean hasToggle(String name) {
        return toggles.containsKey(name) && getToggle(name) != null;
    }
}
