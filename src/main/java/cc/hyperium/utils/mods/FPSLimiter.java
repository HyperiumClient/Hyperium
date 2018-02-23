/*
 * Hyperium Client, Free client with huds and popular mod
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

package cc.hyperium.utils.mods;

import cc.hyperium.config.ConfigOpt;
import cc.hyperium.gui.settings.items.GeneralSetting;
import cc.hyperium.event.*;
import org.lwjgl.opengl.Display;

public class FPSLimiter {

    private static FPSLimiter instance;

    private static boolean limbo;
    private static long time = 0L;
    @ConfigOpt
    private long secondsWait = 5;

    @ConfigOpt
    private int fpsLimit = 30;
    public static FPSLimiter getInstance() {
        if (instance == null)
            instance = new FPSLimiter();
        return instance;
    }

    public static boolean shouldLimitFramerate() {
        return getInstance().limit();
    }

    public boolean limit() {
        return (!Display.isActive() || limbo) && GeneralSetting.framerateLimiterEnabled && time * 20 >= secondsWait;
    }

    @InvokeEvent(priority = Priority.LOW)
    public void onChat(ChatEvent event) {
        if (event.getChat().getUnformattedText().trim().equals("You are currently in limbo")) {
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
        return fpsLimit;
    }
}
