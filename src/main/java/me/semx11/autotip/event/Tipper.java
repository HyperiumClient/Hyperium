/*
 *     Copyright (C) 2018  Hyperium <https://hyperium.cc/>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package me.semx11.autotip.event;

import cc.hyperium.Hyperium;
import cc.hyperium.config.Settings;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.TickEvent;
import me.semx11.autotip.Autotip;
import me.semx11.autotip.misc.FetchBoosters;

import java.util.ArrayList;
import java.util.List;

public class Tipper {

    public static final int waveLength = 915;
    public static final List<String> tipQueue = new ArrayList<>();
    public static int waveCounter = 910;
    private static int tipDelay = 4;
    private long unixTime;

    @InvokeEvent
    public void gameTick(TickEvent event) {
        if (Autotip.onHypixel && Settings.AUTO_TIP && (unixTime
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
                Hyperium.LOGGER.info("Attempting to tip: " + tipQueue.get(0));
                Hyperium.INSTANCE.getHandlers().getCommandQueue().queue("/tip " + tipQueue.get(0));
                tipQueue.remove(0);
                tipDelay = 0;
            }
            waveCounter++;
        }
        unixTime = System.currentTimeMillis() / 1000L;
    }
}