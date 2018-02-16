package com.hcc.handlers.handlers.privatemessages;

import club.sk1er.website.api.requests.HypixelApiPlayer;
import com.hcc.HCC;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PrivateMessageChat implements Comparator<PrivateMessage> {
    private String to;
    private List<PrivateMessage> messages;

    public PrivateMessageChat(String to) {
        this.to = to;
        messages = new ArrayList<>();
    }

    public List<PrivateMessage> getMessages() {
        return messages;
    }

    public HypixelApiPlayer getOtherPlayer() {
        return HCC.INSTANCE.getHandlers().getDataHandler().getPlayer(getTo());
    }

    public void newMessage(String message, String person, boolean self) {
        System.out.println("Message: " + message + ". self:" + self);
        messages.add(new PrivateMessage(person, message, self));
    }

    public String getToLower() {
        return getTo().toLowerCase();
    }

    public String getTo() {
        return to;
    }

    @Override
    public int compare(PrivateMessage o1, PrivateMessage o2) {
        return Long.compare(o2.getTime(), o1.getTime());
    }
}
