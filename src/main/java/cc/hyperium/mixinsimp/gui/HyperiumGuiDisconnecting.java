package cc.hyperium.mixinsimp.gui;

import cc.hyperium.event.EventBus;
import cc.hyperium.event.ServerLeaveEvent;

public class HyperiumGuiDisconnecting {
    public void init() {
        EventBus.INSTANCE.post(new ServerLeaveEvent());
    }
}
