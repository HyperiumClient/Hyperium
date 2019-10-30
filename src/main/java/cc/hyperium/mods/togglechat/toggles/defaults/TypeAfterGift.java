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

public class TypeAfterGift extends ToggleBase {

    private boolean enabled;

    @Override
    public String getName() {
        return "Generosity";
    }

    @Override
    public String getDisplayName() {
        return "Generosity: %s";
    }

    @Override

    public boolean shouldToggle(String message) {
        return message.contains("radiating with Generosity");
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
            "Removes all messages saying",
            "a user is radiating",
            "with Generosity.",
            "",
            "&cSk1er &ris radiating",
            "with &bGenerosity!"
        );
    }
}
