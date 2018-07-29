package cc.hyperium.utils;

import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.TickEvent;

import java.util.ArrayList;
import java.util.List;

public class HyperiumScheduler {

    private List<ScheduledItem> items = new ArrayList<>();

    public HyperiumScheduler() {
        EventBus.INSTANCE.register(this);
    }

    public void schedule(int ticks, Runnable runnable) {
        items.add(new ScheduledItem(ticks, runnable));
    }

    @InvokeEvent
    public void tick(TickEvent event) {
        items.removeIf(ScheduledItem::run);
    }

    class ScheduledItem {
        int tickstill;
        Runnable runnable;

        public ScheduledItem(int tickstill, Runnable runnable) {
            this.tickstill = tickstill;
            this.runnable = runnable;
        }

        public boolean run() {
            if (tickstill <= 0) {
                runnable.run();
                return true;
            }
            tickstill--;
            return false;

        }
    }
}
