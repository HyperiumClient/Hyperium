package cc.hyperium.handlers.handlers.chat;

import cc.hyperium.event.EventBus;
import cc.hyperium.event.network.server.hypixel.HypixelDuelRequestEvent;
import net.minecraft.util.IChatComponent;

import java.util.Locale;
import java.util.regex.Matcher;

public class DuelRequestHandler extends HyperiumChatHandler {

    /**
     * @param component Entire component from event
     * @param text      Pure text for parsing
     * @return boolean to cancel event
     */
    @Override
    public boolean chatReceived(IChatComponent component, String text) {
        if (!text.toLowerCase(Locale.ENGLISH).contains("has invited you to")) return false;
        Matcher matcher = regexTypePatternMap.get(ChatRegexType.DUEL_REQUEST).matcher(text);

        if (matcher.find()) {
            EventBus.INSTANCE.post(new HypixelDuelRequestEvent(matcher.group("player"), matcher.group("gametype")));
        }

        return false;
    }
}
