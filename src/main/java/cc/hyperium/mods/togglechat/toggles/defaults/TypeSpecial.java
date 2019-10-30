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

public class TypeSpecial extends ToggleBase {

    private boolean enabled = true;

    @Override
    public String getName() {
        return "Special";
    }

    @Override
    public String getDisplayName() {
        return "UHC/Bedwars: %s";
    }

    @Override
    public boolean shouldToggle(String message) {
        char[] chars = message.toCharArray();
        boolean hasNum = false;

        // If the message length is less than 3 charaters or the message
        // doesn't start with "[", don't run and assume it isn't special
        if (chars.length < 3 || !message.startsWith("[")) {
            return false;
        }

        // Loop through all the characters after the "["
        for (int i = 1; i < chars.length; i++) {

            // If the character is a number, trigger the flag
            // and move to the next character
            if (Character.isDigit(chars[i])) {
                hasNum = true;
                continue;
            }

            // If the following character is defined in unicode,
            // the character following this character is "]" and
            // the message number flag has been triggered,
            // we'll assume it's special and toggle it.
            if (Character.isDefined(chars[i]) && hasNum) {
                try {
                    if (chars[i + 1] == ']') {
                        return true;
                    }
                } catch (ArrayIndexOutOfBoundsException ex) {
                    return false;
                }
            }
        }
        return false;
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
            "Toggles all uhc",
            "or bedwars chat",
            "",
            "Message format",
            "&6[1\u272B] &7Player&r: Hi",
            "&6[2\u272B] &a[VIP] Player&r: Hi",
            "&6[3\u272B] &b[MVP] Player&r: Hi"
        );
    }
}
