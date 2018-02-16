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
                HCC.logger.info("Sending chat: " + poll);
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
