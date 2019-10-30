package me.semx11.autotip.event.impl;

import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.client.TickEvent;
import me.semx11.autotip.Autotip;
import me.semx11.autotip.event.Event;

public class EventClientTick implements Event {

    private final Autotip autotip;

    public EventClientTick(Autotip autotip) {
        this.autotip = autotip;
    }

    @InvokeEvent
    public void onClientTick(TickEvent event) {
        autotip.getMessageUtil().flushQueues();
        if (autotip.isInitialized()) autotip.getStatsManager().saveCycle();
    }
}
