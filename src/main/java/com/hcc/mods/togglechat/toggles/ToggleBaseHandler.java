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

package com.hcc.mods.togglechat.toggles;

import com.hcc.mods.togglechat.toggles.defaults.*;

import java.util.LinkedHashMap;

public class ToggleBaseHandler {

    private LinkedHashMap<String, ToggleBase> toggles = new LinkedHashMap<>();

    private boolean terminated;
    
    public LinkedHashMap<String, ToggleBase> getToggles() {
        return this.terminated ? new LinkedHashMap<>() : new LinkedHashMap<>(this.toggles);
    }

    /**
     * Adds the developers own ToggleBase
     *
     * @param toggleBase the developers toggle
     */
    public void addToggle(ToggleBase toggleBase) {
        if (this.terminated) {
            return;
        }
        
        if (toggleBase != null && toggleBase.getName() != null)  {
            this.toggles.put(toggleBase.getName().toLowerCase().replace(" ", "_"), toggleBase);
        }
    }

    /**
     * Run through all bases and check if the
     *      given text should be toggled
     *
     * @param input text to test
     * @return the formatted text
     */
    public boolean shouldToggle(String input) {
        if (this.terminated) {
            return false;
        }
        
        for (ToggleBase parser : this.toggles.values()) {
            if (!parser.isEnabled() && parser.shouldToggle(input)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Clears all toggles and readds default ones
     */
    public void remake() {
        if (this.terminated) {
            return;
        }
        
        this.toggles.clear();
        this.toggles.put("ads", new TypeAds());
        this.toggles.put("team", new TypeTeam());
        this.toggles.put("join", new TypeJoin());
        this.toggles.put("leave", new TypeLeave());
        this.toggles.put("guild", new TypeGuild());
        this.toggles.put("party", new TypeParty());
        this.toggles.put("shout", new TypeShout());
        this.toggles.put("soul", new TypeSoulWell());
        this.toggles.put("housing", new TypeHousing());
        this.toggles.put("messages", new TypeMessages());
        this.toggles.put("global", new TypeGlobal(this));
        this.toggles.put("ez_messages", new TypeEasy());
        this.toggles.put("special", new TypeSpecial());
        this.toggles.put("colored_team", new TypeColored());
        this.toggles.put("party_invites", new TypePartyInvites());
        this.toggles.put("build_battle", new TypeBuildBattle());
        this.toggles.put("mystery_box", new TypeMysteryBox());
        this.toggles.put("spectator", new TypeSpectator());
        this.toggles.put("lobby_join", new TypeLobbyJoin());
        this.toggles.put("separators", new TypeMessageSeparator());
        this.toggles.put("friend_requests", new TypeFriendRequests());
    }

    /**
     * Gets a toggle by the given name, may return null
     *
     * @param name the toggle's name
     * @return a ToggleBase instance if found, or else null
     */
    public ToggleBase getToggle(String name) {
        return this.terminated ? null : this.toggles.getOrDefault(name, null);
    }

    /**
     * Checks to see if the registered parsers contains a parser
     *      with the given name.
     *
     * @param name The toggle's name to test for
     * @return true if it is registered
     */
    public boolean hasToggle(String name) {
        return !this.terminated && this.toggles.containsKey(name) && getToggle(name) != null;
    }
    
    public void terminate() {
        this.terminated = true;
        
        this.toggles.clear();
    }
}
