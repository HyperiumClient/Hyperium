package cc.hyperium.handlers.handlers.chat;

import cc.hyperium.event.EventBus;
import cc.hyperium.event.HypixelFriendRequestEvent;
import net.minecraft.util.IChatComponent;

import java.util.regex.Matcher;

public class FriendRequestChatHandler extends HyperiumChatHandler {

    @Override
    public boolean chatReceived(IChatComponent component, String text) {
        if (!text.toLowerCase().contains("friend request")) {
            return false;
        }

        Matcher matcher = regexPatterns.get(ChatRegexType.FRIEND_REQUEST).matcher(text);
        if (matcher.find()) {
            EventBus.INSTANCE.post(new HypixelFriendRequestEvent(matcher.group("player")));
        }
        return false;
    }
}
