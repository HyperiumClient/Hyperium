/*
 *     Hypixel Community Client, Client optimized for Hypixel Network
 *     Copyright (C) 2018  HCC Dev Team
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

package com.hcc.handlers.handlers;

import com.hcc.HCC;
import com.hcc.mods.sk1ercommon.Multithreading;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by mitchellkatz on 2/14/18. Designed for production use on Sk1er.club
 */
public class CommandQueue {

    private final long DELAY = 1000;
    private ConcurrentLinkedQueue<String> commands = new ConcurrentLinkedQueue<>();
    private long last = System.currentTimeMillis();
    private ConcurrentHashMap<String, Runnable> asyncCallbacks = new ConcurrentHashMap<>();

    public CommandQueue() {
        Multithreading.schedule(CommandQueue.this::check, 0, DELAY, TimeUnit.MILLISECONDS);
    }

    public void register(String chat, Runnable task) {
        System.out.println("Queued callback for " + chat);
        asyncCallbacks.put(chat, task);
        queue(chat);
    }

    private void check() {
        if (!commands.isEmpty()) {
            EntityPlayerSP thePlayer = Minecraft.getMinecraft().thePlayer;
            if (thePlayer != null) {
                String poll = commands.poll();
                HCC.LOGGER.info("Sending chat: " + poll);
                Runnable runnable = asyncCallbacks.get(poll);
                thePlayer.sendChatMessage(poll);
                if (runnable != null) {
                    System.out.println("Running runnable for " + poll);
                    runnable.run();
                }

            }
        }
        last = System.currentTimeMillis();
    }

    public void queue(String message) {
        System.out.println("queued command: " + message);
        commands.add(message);
    }
}
