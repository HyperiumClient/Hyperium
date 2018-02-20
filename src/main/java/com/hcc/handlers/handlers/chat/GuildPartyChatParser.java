package com.hcc.handlers.handlers.chat;

import com.hcc.HCC;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IChatComponent;

import java.util.regex.Matcher;

/**
 * Created by mitchellkatz on 2/19/18. Designed for production use on Sk1er.club
 */
public class GuildPartyChatParser extends HCCChatHandler {
    @Override
    public boolean chatReceived(IChatComponent component, String text) {
        Matcher guildMatcher = guildChatPattern.matcher(text);
        if (guildMatcher.matches()) {
            String player = guildMatcher.group("player");
            String message = guildMatcher.group("message");
            HCC.INSTANCE.getHandlers().getPrivateMessageHandler().getChat("guild").newMessage(message, player, Minecraft.getMinecraft().getSession().getUsername().equals(player));
        }
        Matcher partyMatcher = partyChatPattern.matcher(text);
        if (partyMatcher.matches()) {
            String player = partyMatcher.group("player");
            String message = partyMatcher.group("message");
            HCC.INSTANCE.getHandlers().getPrivateMessageHandler().getChat("party").newMessage(message, player, Minecraft.getMinecraft().getSession().getUsername().equals(player));
        }
        return false;
    }
}
