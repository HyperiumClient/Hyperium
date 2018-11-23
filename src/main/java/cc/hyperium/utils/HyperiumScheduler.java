package cc.hyperium.utils;

import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.TickEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Scheduler: Schedules tasks after the specified amount of ticks
 */
public class HyperiumScheduler {

    /**
     * The queued tasks to execute
     */
    private List<ScheduledItem> items = new ArrayList<>();

    public HyperiumScheduler() {
        EventBus.INSTANCE.register(this);
    }

    /**
     * Schedules the given task after the given amount of ticks
     *
     * @param ticks    Ticks to run after
     * @param runnable Task to run
     */
    public void schedule(int ticks, Runnable runnable) {
        items.add(new ScheduledItem(ticks, runnable));
    }

    @InvokeEvent
    public void onTick(TickEvent event) {
        items.removeIf(ScheduledItem::run);
    }

    /**
     * Represents a scheduled task item
     */
    class ScheduledItem {

        /**
         * Ticks to wait until the task is ran
         */
        int ticksLeft;

        /**
         * Task to run
         */
        Runnable runnable;

        /**
         * Initiates a new ScheduledItem
         *
         * @param ticksLeft Amount of ticks to wait
         * @param runnable  Task to run after the given amount of ticks
         */
        ScheduledItem(int ticksLeft, Runnable runnable) {
            this.ticksLeft = ticksLeft;
            this.runnable = runnable;
        }

        /**
         * Runs the task after waiting the appropriate amount of ticks
         *
         * @return {@code true} if the task has been ran and {@link #ticksLeft} is now 0,
         * or {@code false} otherwise.
         */
        public boolean run() {
            if (ticksLeft <= 0) { // Run immediately, so we don't wait any ticks
                runnable.run();
                return true;
            }
            ticksLeft--; // Reduce the amount of ticks left every tick until it's 0 or less
            return false;

        }
    }
}
