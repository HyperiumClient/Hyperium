package com.hcc.handlers.handlers;

import com.hcc.mods.sk1ercommon.Multithreading;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by mitchellkatz on 2/14/18. Designed for production use on Sk1er.club
 */
public class CommandQueue {

    private final long DELAY = 1000;
    private ConcurrentLinkedQueue<String> commands = new ConcurrentLinkedQueue<>();
    private long last = System.currentTimeMillis();

    public CommandQueue() {
        Multithreading.schedule(CommandQueue.this::check, 0, DELAY, TimeUnit.MILLISECONDS);
    }

    private void check() {
        if (!commands.isEmpty()) {
            EntityPlayerSP thePlayer = Minecraft.getMinecraft().thePlayer;
            if (thePlayer != null)
                thePlayer.sendChatMessage(commands.poll());
        }
        last = System.currentTimeMillis();
    }

    public void queue(String message) {
        commands.add(message);
    }
}
