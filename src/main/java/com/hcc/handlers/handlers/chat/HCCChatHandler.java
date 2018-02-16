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

package com.hcc.handlers.handlers.chat;

import com.hcc.HCC;
import net.minecraft.util.IChatComponent;

import java.util.regex.Pattern;

/**
 * Created by mitchellkatz on 2/14/18. Designed for production use on Sk1er.club
 */
public abstract class HCCChatHandler {
    protected final Pattern guildChatParrern = Pattern.compile("Guild > (?<rank>\\[.+] )?(?<player>\\S{1,16}): (?<message>.*)");
    protected final Pattern partyChatPattern = Pattern.compile("Party > (?<rank>\\[.+] )?(?<player>\\S{1,16}): (?<message>.*)");
    protected final Pattern partyInvitePattern = Pattern.compile("(\\[.*\\] )?(?<player>\\w+) has invited you to join their party!");
    protected final Pattern coinsPatternTwo = Pattern.compile("\\+(?<coins>.+) coins!");
    protected final Pattern friendPattern = Pattern.compile("--+\\\\nFriend request from ((?<rank>\\[.+] )?(?<player>\\w+)).*");
    protected final Pattern questPattern = Pattern.compile("(Daily|Weekly)? Quest: (?<name>.+?(?= Completed!))");
    protected final Pattern expPattern = Pattern.compile(" \\+(?<exp>\\d+) Hypixel Experience");
    protected final Pattern coinPattern = Pattern.compile(" \\+(?<coin>\\d+) (?<game>.+) Coins");
    protected final Pattern skywarsRankedRating = Pattern.compile("(?<change>^-?[0-9]\\d*(\\.\\d+)?) Rating \\(\\S(?<rating>\\d+)\\)");
    protected final Pattern privateMessageTo = Pattern.compile("To (?<rank>\\[.+] )?(?<player>\\S{1,16}): (?<message>.*)");
    protected final Pattern privateMessageFrom = Pattern.compile("From (?<rank>\\[.+] )?(?<player>\\S{1,16}): (?<message>.*)");

    public HCC getHcc() {
        return HCC.INSTANCE;

    }

    /**
     * @param component Entire component from evnet
     * @param text      Pure text for parsign
     * @return boolean to cancel event
     */
    public abstract boolean chatReceived(IChatComponent component, String text);

}
