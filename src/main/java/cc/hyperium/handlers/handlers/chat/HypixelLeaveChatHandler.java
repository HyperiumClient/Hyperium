package cc.hyperium.handlers.handlers.chat;

import cc.hyperium.event.EventBus;
import cc.hyperium.event.network.server.hypixel.PlayerLeaveHypixelEvent;
import net.minecraft.util.IChatComponent;

import java.util.regex.Matcher;

public class HypixelLeaveChatHandler extends HyperiumChatHandler {

    /**
     * @param component Entire component from event
     * @param text      Pure text for parsing
     * @return boolean to cancel event
     */
    @Override
    public boolean chatReceived(IChatComponent component, String text) {
        if (!text.toLowerCase().contains("left.")) return false;
        Matcher matcher = regexPatterns.get(ChatRegexType.PLAYER_LEAVE).matcher(text);

        if (matcher.find()) {
            EventBus.INSTANCE.post(new PlayerLeaveHypixelEvent(matcher.group("player")));
        }

        return false;
    }
}
