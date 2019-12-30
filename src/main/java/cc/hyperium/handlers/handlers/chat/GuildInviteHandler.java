package cc.hyperium.handlers.handlers.chat;

import cc.hyperium.event.EventBus;
import cc.hyperium.event.network.server.hypixel.HypixelGuildInviteEvent;
import net.minecraft.util.IChatComponent;

import java.util.regex.Matcher;

public class GuildInviteHandler extends HyperiumChatHandler {

    @Override
    public boolean chatReceived(IChatComponent component, String text) {
        if (!text.toLowerCase().contains("their guild,")) {
            return false;
        }
        Matcher matcher = regexPatterns.get(ChatRegexType.GUILD_INVITE).matcher(text);

        if (matcher.find()) {
            EventBus.INSTANCE.post(new HypixelGuildInviteEvent(matcher.group("player"), matcher.group("guild")));
        }

        return false;
    }
}
