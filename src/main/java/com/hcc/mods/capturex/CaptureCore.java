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

package com.hcc.mods.capturex;

import com.google.common.collect.Queues;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.hcc.HCC;
import com.hcc.event.HypixelKillEvent;
import com.hcc.event.InvokeEvent;
import com.hcc.event.RenderEvent;
import com.hcc.event.TickEvent;
import com.hcc.mods.capturex.render.FFMpegHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.Util;
import org.apache.commons.lang3.Validate;

import java.io.File;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class CaptureCore {
    public static final File captureXDir = new File(Minecraft.getMinecraft().mcDataDir, "captureX");
    private final Queue<FutureTask<?>> scheduledTasks = Queues.newArrayDeque();
    private Queue<Framebuffer> backwardsBuffer = new ArrayDeque<>();
    private FFMpegHelper FFMpeg = new FFMpegHelper();
    private Thread queueWorker;

    public CaptureCore() {
        queueWorker = new Thread(() -> {
            while (!Thread.interrupted())
                while (!this.scheduledTasks.isEmpty()) {
                    Util.runTask((FutureTask<?>) this.scheduledTasks.poll(), HCC.LOGGER);
                }
        }, "Capture Worker");
        queueWorker.start();
    }

    @InvokeEvent
    public void onTick(RenderEvent event){
        // 1 seconds backward
        if(backwardsBuffer.size() > 20)
            backwardsBuffer.poll();
        backwardsBuffer.add(Minecraft.getMinecraft().getFramebuffer());
    }

    @InvokeEvent
    public void onKill(HypixelKillEvent event) {
        addScheduledTask(() -> {
            try {
                HCC.INSTANCE.getNotification().display("CaptureX", "Rendering kill", 3);
                CapturePack pack = new CapturePack(backwardsBuffer);
                FFMpeg.run(pack, "C:\\FFmpeg\\bin\\ffmpeg.exe", "kill");
                pack.cleanup();
                HCC.INSTANCE.getNotification().display("CaptureX", "Kill captured", 3);
            } catch (Exception e) {
                e.printStackTrace();
                HCC.INSTANCE.getNotification().display("CaptureX", "Failed to capture kill", 3);
            }
        });
    }

    private <V> ListenableFuture<V> addScheduledTask(Callable<V> callableToSchedule) {
        ListenableFutureTask<V> listenable = ListenableFutureTask.create(callableToSchedule);

        synchronized (this.scheduledTasks) {
            this.scheduledTasks.add(listenable);
            return listenable;
        }
    }

    private ListenableFuture<Object> addScheduledTask(Runnable runnableToSchedule) {
        Validate.notNull(runnableToSchedule);
        return this.addScheduledTask(Executors.callable(runnableToSchedule));
    }

    public void shutdown() {
        queueWorker.interrupt();
    }
}
