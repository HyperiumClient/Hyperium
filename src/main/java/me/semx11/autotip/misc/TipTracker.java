/*
 *  Hypixel Community Client, Client optimized for Hypixel Network
 *     Copyright (C) 2018  Hyperium Dev Team
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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