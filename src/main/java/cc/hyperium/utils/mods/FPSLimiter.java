/*
 *       Copyright (C) 2018-present Hyperium <https://hyperium.cc/>
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU Lesser General Public License as published
 *       by the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU Lesser General Public License for more details.
 *
 *       You should have received a copy of the GNU Lesser General Public License
 *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.utils.mods;

import cc.hyperium.config.Settings;
import cc.hyperium.event.network.chat.ChatEvent;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.Priority;
import cc.hyperium.event.network.server.ServerLeaveEvent;
import cc.hyperium.event.world.SpawnpointChangeEvent;
import cc.hyperium.event.client.TickEvent;
import org.lwjgl.opengl.Display;

public class FPSLimiter {

    // Create an instance to be used in other classes
    private static FPSLimiter instance;

    // Is the user in limbo?
    private static boolean limbo;

    // How long have they been in limbo?
    private static long time;

    /**
     * Create the instance
     *
     * @return the class instance
     */
    public static FPSLimiter getInstance() {
        if (instance == null) instance = new FPSLimiter();
        return instance;
    }

    // Should the client limit the framerate?
    public static boolean shouldLimitFramerate() {
        return getInstance().limit();
    }

    // Check if it's been 5 seconds, if it has, apply the limited framerate
    public boolean limit() {
        long secondsWait = 5;
        return (!Display.isActive() || limbo) && Settings.FPS_LIMITER && time * 20 >= secondsWait;
    }

    /**
     * Check if any messages that are sent when the user is sent to Limbo have been sent
     *
     * @param event called whenever a message is sent through chat
     */
    @InvokeEvent(priority = Priority.LOW)
    public void onChat(ChatEvent event) {
        if (event.getChat().getUnformattedText().trim().equals("You were spawned in Limbo.") ||
            event.getChat().getUnformattedText().trim().equals("You are AFK. Move around to return from AFK.")) {
            limbo = true;
        }
    }

    /**
     * If the user is in limbo, add up the time, otherwise set the time to 0
     *
     * @param event called every tick
     */
    @InvokeEvent
    public void tick(TickEvent event) {
        if (limbo) time++;
        else time = 0;
    }

    /**
     * Once the spawnpoint has changed, assume the user is no longer in limbo and joined a new world
     *
     * @param event called whenever the user either switches worlds or the server has changed the world spawn
     */
    @InvokeEvent(priority = Priority.LOW)
    public void onWorldChange(SpawnpointChangeEvent event) {
        limbo = false;
    }

    @InvokeEvent
    public void leaveServer(ServerLeaveEvent event) {
        if (limbo) {
            limbo = false;
        }
    }

    /**
     * Get the users current fps limit
     *
     * @return the fps limit
     */
    public int getFpsLimit() {
        return Settings.FPS_LIMITER_AMOUNT;
    }

    /**
     * Used for developers to do something if they want to when the user is in Limbo
     *
     * @return true if the user is in limbo
     */
    public static boolean isInLimbo() {
        return limbo;
    }
}
