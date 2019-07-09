package cc.hyperium.handlers.handlers.chat;

import cc.hyperium.event.EventBus;
import cc.hyperium.event.HypixelPartyInviteEvent;
import net.minecraft.util.IChatComponent;

import java.util.regex.Matcher;

public class PartyInviteChatHandler extends HyperiumChatHandler {

    @Override
    public boolean chatReceived(IChatComponent component, String text) {
        if (!text.toLowerCase().contains("their party!")) {
            return false;
        }

        Matcher matcher = regexPatterns.get(ChatRegexType.PARTY_INVITE).matcher(text);

        if (matcher.find()) {
            EventBus.INSTANCE.post(new HypixelPartyInviteEvent(matcher.group("player")));
        }

        return false;
    }
}
