package cc.hyperium.handlers.handlers.chat;

import cc.hyperium.event.EventBus;
import cc.hyperium.event.FriendRemoveEvent;
import cc.hyperium.event.HypixelFriendRequestEvent;
import net.minecraft.util.IChatComponent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FriendRequestChatHandler extends HyperiumChatHandler {

    @Override
    public boolean chatReceived(IChatComponent component, String text) {
        Matcher matcher1 = Pattern.compile("You removed ((?<rank>\\[.+] )?(?<player>\\w+)) from your friends list!").matcher(text);
        if (matcher1.find()) {
            String rank = "";
            try {
                rank = matcher1.group("rank");
            } catch (Exception ignored) {

            }
            String player = matcher1.group("player");
            EventBus.INSTANCE.post(new FriendRemoveEvent(rank + player, player));
        }
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
