package cc.hyperium.event.network.server.hypixel;

import cc.hyperium.event.Event;

public class HypixelGuildInviteEvent extends Event {

    private final String from;
    private final String guild;

    public HypixelGuildInviteEvent(String from, String guild) {
        this.from = from;
        this.guild = guild;
    }

    public String getFrom() {
        return from;
    }

    public String getGuild() {
        return guild;
    }
}
