package me.semx11.autotip.misc;

import me.semx11.autotip.Autotip;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class TipTracker {

    public static Map<Long, String> tipsSentHistory = new TreeMap<>(Collections.reverseOrder());
    public static Map<String, Integer> tipsSentEarnings = new HashMap<>();
    public static Map<String, Integer> tipsReceivedEarnings = new HashMap<>();
    public static int tipsSent = 0;
    public static int tipsReceived = 0;
    public static int karmaCount = 0;

    public static void addTip(String username) {
        tipsSentHistory.put(System.currentTimeMillis(), username);
        tipsSent++;
        Autotip.totalTipsSent++;
        Autotip.alreadyTipped.add(username);
        System.out.println("Tipped: " + username);
        Writer.execute();
    }

}