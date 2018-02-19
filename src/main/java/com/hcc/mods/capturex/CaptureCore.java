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
import net.minecraft.client.Minecraft;
import net.minecraft.util.ScreenShotHelper;
import net.minecraft.util.Util;
import org.apache.commons.lang3.Validate;

import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class CaptureCore {
    private final Queue<FutureTask<?>> scheduledTasks = Queues.newArrayDeque();
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
    public void onKill(HypixelKillEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        addScheduledTask(() -> {
            mc.addScheduledTask(()->ScreenShotHelper.saveScreenshot(mc.mcDataDir, mc.displayWidth, mc.displayHeight, mc.getFramebuffer()));
            HCC.INSTANCE.sendMessage("Kill captured!");
            HCC.INSTANCE.getNotification().display("CaptureX", "Kill captured!", 2);
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
