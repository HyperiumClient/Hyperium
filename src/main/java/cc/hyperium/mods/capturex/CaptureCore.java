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

package cc.hyperium.mods.capturex;

import cc.hyperium.Hyperium;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.JoinMinigameEvent;
import cc.hyperium.event.KillEvent;
import cc.hyperium.event.RenderEvent;
import cc.hyperium.event.minigames.Minigame;
import cc.hyperium.gui.settings.items.CaptureXSetting;
import cc.hyperium.mods.capturex.render.FFMpegHelper;
import cc.hyperium.mods.capturex.render.FrameRenderer;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Util;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.Display;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class CaptureCore {
    public static final File captureXDir = new File(Minecraft.getMinecraft().mcDataDir, "captureX");
    private final ConcurrentLinkedDeque<FutureTask<?>> scheduledTasks = new ConcurrentLinkedDeque<>();
    private final ConcurrentLinkedDeque<FutureTask<?>> renderTasks = new ConcurrentLinkedDeque<>();
    private Queue<BufferedImage> backwardFrames = new ArrayDeque<>();
    private FFMpegHelper FFMpeg = new FFMpegHelper();

    private Minigame currentGame;
    private ScheduledExecutorService service = Executors.newScheduledThreadPool(1, new ThreadFactory() {
        private AtomicInteger counter = new AtomicInteger(0);

        @Override
        public Thread newThread(@NotNull Runnable r) {
            return new Thread(r, "CaptureX Worker " + counter.incrementAndGet());
        }
    });

    private Robot robot;

    {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public CaptureCore() {

        service.scheduleAtFixedRate(() -> {
            for (FutureTask<?> renderTask : renderTasks) {
                try {
                    Util.runTask((FutureTask<?>) renderTask, Hyperium.LOGGER);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            for (FutureTask<?> scheduledTask : this.scheduledTasks) {
                try {
                    Util.runTask((FutureTask<?>) scheduledTask, Hyperium.LOGGER);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        }, 15, 15, TimeUnit.MILLISECONDS);
    }

    @InvokeEvent
    public void onTick(RenderEvent event) {
        if (this.currentGame == null || CaptureXSetting.mode != CaptureMode.VIDEO) return;
        // 1 seconds backward
        if (this.backwardFrames.size() > CaptureXSetting.captureLength * 20)
            this.backwardFrames.poll();
        try {
            this.backwardFrames.add(addScheduledTask(() -> robot.createScreenCapture(new Rectangle(Display.getX(), Display.getY(), Display.getWidth(), Display.getHeight()))).get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    @InvokeEvent
    public void onKill(KillEvent event) {
        if (this.currentGame == null || CaptureXSetting.mode == CaptureMode.OFF) {
            return;
        }
        
        final Queue<BufferedImage> finalBackwardsBuffer = new ArrayDeque<>(this.backwardFrames);
        addRenderTask(() -> {
            switch (CaptureXSetting.mode) {
                case VIDEO:
                    try {
                        Hyperium.INSTANCE.getNotification().display("CaptureX", "Rendering kill", 3);
                        CapturePack pack = new CapturePack(finalBackwardsBuffer, "kill");
                        this.FFMpeg.run(pack, "C:\\FFmpeg\\bin\\ffmpeg.exe", "kill", "kill");
                        pack.cleanup();
                        Hyperium.INSTANCE.getNotification().display("CaptureX", "Kill captured", 3);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Hyperium.INSTANCE.getNotification().display("CaptureX", "Failed to capture kill", 3);
                    }
                    break;
                case SCREENSHOT:
                    try {
                        ImageIO.write(FrameRenderer.getImageFromFrameBuffer(Minecraft.getMinecraft().getFramebuffer(), Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight), "png", new File(captureXDir, "kill-" + System.currentTimeMillis() + ".png"));
                        Hyperium.INSTANCE.getNotification().display("CaptureX", "Kill captured", 3);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Hyperium.INSTANCE.getNotification().display("CaptureX", "Failed to capture kill", 3);
                    }
                    break;
            }
        });
    }

    private <V> ListenableFuture<V> addScheduledTask(Callable<V> callableToSchedule) {
        ListenableFutureTask<V> listenable = ListenableFutureTask.create(callableToSchedule);
        this.scheduledTasks.add(listenable);
        return listenable;
    }

    private ListenableFuture<Object> addScheduledTask(Runnable runnableToSchedule) {
        Validate.notNull(runnableToSchedule);
        return this.addScheduledTask(Executors.callable(runnableToSchedule));
    }

    private <V> ListenableFuture<V> addRenderTask(Callable<V> callableToSchedule) {
        ListenableFutureTask<V> listenable = ListenableFutureTask.create(callableToSchedule);

        this.renderTasks.add(listenable);
        return listenable;

    }

    private ListenableFuture<Object> addRenderTask(Runnable runnableToSchedule) {
        Validate.notNull(runnableToSchedule);
        return this.addRenderTask(Executors.callable(runnableToSchedule));
    }

    public void shutdown() {
        try {
            service.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @InvokeEvent
    public void onMinigameJoin(JoinMinigameEvent event) {
        this.currentGame = event.getMinigame();
    }
}
