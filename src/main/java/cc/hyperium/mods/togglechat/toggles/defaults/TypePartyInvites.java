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

public class TypePartyInvites extends ToggleBase {

    private final Pattern expiredPattern = Pattern.compile("The party invite (?<where>\\S{1,4}) (?<rank>\\[.+] )?(?<player>\\S{1,16}) has expired.");
    private final Pattern invitePattern = Pattern.compile("(?<rank>\\[.+] )?(?<player>\\S{1,16}) has invited you to join (?<meme>\\[.+] )?(?<meme2>\\S{1,16}) party!");
    private final Pattern otherInvitePattern = Pattern.compile("(?<inviteerank>\\[.+] )?(?<invitee>\\S{1,16}) invited (?<rank>\\[.+] )?(?<player>\\S{1,16}) to the party! They have 60 seconds to accept.");

    private boolean enabled = true;

    private boolean wasLastMessageToggled;

    @Override
    public String getName() {
        return "Party invites";
    }

    @Override
    public boolean shouldToggle(String message) {
        // noinspection SimplifiableIfStatement
        if (wasLastMessageToggled && containsIgnoreCase(message, "Click here to join! You have 60 seconds to accept.")) {
            return true;
        }

        return wasLastMessageToggled = expiredPattern.matcher(message).matches() || invitePattern.matcher(message).matches() || otherInvitePattern.matcher(message).matches();
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
            "Toggles the ability to see",
            "party invites from",
            "other players.",
            "",
            "This goes well with",
            "separators toggled"
        );
    }
}
