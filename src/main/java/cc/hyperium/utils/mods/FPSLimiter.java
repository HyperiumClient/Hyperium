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

package cc.hyperium.utils.mods;

import cc.hyperium.config.Settings;
import cc.hyperium.event.ChatEvent;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.Priority;
import cc.hyperium.event.SpawnpointChangeEvent;
import cc.hyperium.event.TickEvent;
import org.lwjgl.opengl.Display;

public class FPSLimiter {

    private static FPSLimiter instance;

    private static boolean limbo;
    private static long time = 0L;

    public static FPSLimiter getInstance() {
        if (instance == null)
            instance = new FPSLimiter();
        return instance;
    }

    public static boolean shouldLimitFramerate() {
        return getInstance().limit();
    }

    public boolean isLimbo() {
        return limbo;
    }

    public boolean limit() {
        long secondsWait = 5;
        return (!Display.isActive() || limbo) && Settings.FPS_LIMITER && time * 20 >= secondsWait;
    }

    @InvokeEvent(priority = Priority.LOW)
    public void onChat(ChatEvent event) {
        if (event.getChat().getUnformattedText().trim().equals("You were spawned in Limbo.") || event.getChat().getUnformattedText().trim().equals("You are AFK. Move around to return from AFK.")) {
            limbo = true;
        }
    }

    @InvokeEvent
    public void tick(TickEvent event) {
        if (limbo)
            time++;
        else time = 0;
    }

    @InvokeEvent(priority = Priority.LOW)
    public void onWorldChange(SpawnpointChangeEvent event) {
        limbo = false;
    }

    public int getFpsLimit() {
        return Settings.FPS_LIMITER_AMOUNT;
    }
}
