package me.semx11.autotip.event;

import cc.hyperium.Hyperium;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.TickEvent;
import me.semx11.autotip.Autotip;
import me.semx11.autotip.misc.FetchBoosters;

import java.util.ArrayList;
import java.util.List;

public class Tipper {

    public static int waveCounter = 910;
    public static int waveLength = 915;
    public static List<String> tipQueue = new ArrayList<>();
    private static int tipDelay = 4;
    private long unixTime;

    @InvokeEvent
    public void gameTick(TickEvent event) {
        if (Autotip.onHypixel && Autotip.toggle && (unixTime
                != System.currentTimeMillis() / 1000L)) {
            if (waveCounter == waveLength) {
                Autotip.THREAD_POOL.submit(new FetchBoosters());
                waveCounter = 0;
            }

            if (!tipQueue.isEmpty()) {
                tipDelay++;
            } else {
                tipDelay = 4;
            }

            if (!tipQueue.isEmpty() && (tipDelay % 5 == 0)) {
                System.out.println("Attempting to tip: " + tipQueue.get(0));
                Hyperium.INSTANCE.getHandlers().getCommandQueue().queue("/tip " + tipQueue.get(0));
                tipQueue.remove(0);
                tipDelay = 0;
            }
            waveCounter++;
        }
        unixTime = System.currentTimeMillis() / 1000L;
    }
}