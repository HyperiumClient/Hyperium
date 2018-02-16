package com.hcc.handlers.handlers.chat;

import com.hcc.HCC;
import net.minecraft.util.IChatComponent;

import java.util.regex.Matcher;

public class PrivateMessageReader extends HCCChatHandler {
    @Override
    public boolean chatReceived(IChatComponent component, String text) {
        Matcher fromMatcher = privateMessageFrom.matcher(text);
        if (fromMatcher.matches()) {
            HCC.INSTANCE.getHandlers().getPrivateMessageHandler().inboundMessage(fromMatcher.group("player"), fromMatcher.group("message"));
        }

        Matcher toMatcher = privateMessageTo.matcher(text);
        if (toMatcher.matches()) {
            HCC.INSTANCE.getHandlers().getPrivateMessageHandler().outboundMessage(toMatcher.group("player"), toMatcher.group("message"));
        }

        return false;
    }
}
