package cc.hyperium.mixinsimp.gui;

import cc.hyperium.event.EventBus;
import cc.hyperium.event.ServerJoinEvent;

public class HyperiumGuiConnecting {
    public void connect(String ip, int port) {
        EventBus.INSTANCE.post(new ServerJoinEvent(ip, port));
    }
}
