/*
 *     Hypixel Community Client, Client optimized for Hypixel Network
 *     Copyright (C) 2018 HCC Dev Team
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

import net.minecraft.client.gui.GuiButton;

import java.util.LinkedList;
import java.util.regex.Pattern;

public class TypeTeam extends ToggleBase {

    private Pattern teamPattern = Pattern.compile("\\[TEAM] (?<rank>\\[.+] )?(?<player>\\S{1,16}): (?<message>.*)");

    private boolean enabled = true;

    @Override
    public String getName() {
        return "Team";
    }

    @Override
    public boolean shouldToggle(String message) {
        return this.teamPattern.matcher(message).matches();
    }

    @Override
    public void onClick(GuiButton button) {
        this.enabled = !this.enabled;
        button.displayString = (String.format(getDisplayName(), getStatus(isEnabled())));
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
                "Toggles all",
                "incoming team messages",
                "",
                "Message format:",
                "&9[TEAM] &7Player&r: Hi",
                "&9[TEAM] &a[VIP] Player&r: Hi",
                "&9[TEAM] &b[MVP] Player&r: Hi",
                "",
                "Useful for large",
                "team games"
        );
    }
}