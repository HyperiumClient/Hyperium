package com.hcc.handlers.handlers.privatemessages;

import java.util.HashMap;

public class PrivateMessageHandler {

    private HashMap<String, PrivateMessageChat> chats = new HashMap<>();

    public PrivateMessageChat getChat(String with) {
        return chats.computeIfAbsent(with.toLowerCase(), tmp -> new PrivateMessageChat(with));
    }

    public void outboundMessage(String to, String message) {
        getChat(to).newMessage(message, to, true);
    }

    public void inboundMessage(String from, String message) {
        getChat(from).newMessage(message, from, false);
    }


}
