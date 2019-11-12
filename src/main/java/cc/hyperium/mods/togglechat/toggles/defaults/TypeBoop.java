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
import java.util.regex.Pattern;

import java.util.LinkedList;

public class TypeBoop extends ToggleBase {

    private boolean enabled = true;
    private final Pattern boopPattern = Pattern.compile("From (?<rank>\\[.+] )?(?<player>\\S{1,16}): Boop!");


    @Override
    public String getName() {
        return "Boop";
    }

    @Override
    public boolean shouldToggle(String message) {
        return boopPattern.matcher(message).matches();
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
            "Toggles all incoming",
            "boops",
            "",
            "No more annoying boops",
            "disturbing your amazing game!",
            "&dFrom &7Player&r: &d&lBoop!",
            ""
        );
    }
}
