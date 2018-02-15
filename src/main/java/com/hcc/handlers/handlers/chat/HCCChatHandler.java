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
