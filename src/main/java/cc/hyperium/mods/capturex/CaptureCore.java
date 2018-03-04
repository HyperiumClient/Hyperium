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

package cc.hyperium.mods.capturex;

import cc.hyperium.Hyperium;
import cc.hyperium.event.ChatEvent;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.HypixelFriendRequestEvent;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.JoinMinigameEvent;
import cc.hyperium.event.RenderEvent;
import cc.hyperium.event.minigames.Minigame;
import cc.hyperium.gui.settings.items.CaptureXSetting;
import cc.hyperium.mods.capturex.render.FFMpegHelper;
import cc.hyperium.mods.capturex.render.FrameRenderer;
import cc.hyperium.utils.ChatColor;
import com.google.common.collect.Queues;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import java.util.regex.Pattern;
import net.minecraft.client.Minecraft;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.Util;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.Validate;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class CaptureCore {
    public static final File captureXDir = new File(Minecraft.getMinecraft().mcDataDir, "captureX");
    private final Queue<FutureTask<?>> scheduledTasks = Queues.newArrayDeque();
    private Queue<BufferedImage> backwardFrames = new ArrayDeque<>();
    private FFMpegHelper FFMpeg = new FFMpegHelper();
    private Thread queueWorker;
    
    private Minigame currentGame;
    
    private Pattern friendRequestPattern;
    private Pattern rankBracketPattern;
    private Pattern swKillMsg;
    private Pattern bwKillMsg;
    private Pattern bwFinalKillMsg;
    private Pattern duelKillMsg;
    
    private Hyperium client;
    
    public CaptureCore(Hyperium hyperiumIn) {
        friendRequestPattern = Pattern.compile("Friend request from .+?");
        rankBracketPattern = Pattern.compile("[\\^] ");
        swKillMsg = Pattern.compile(".+? was .+? by .+?\\.");
        bwKillMsg = Pattern.compile(".+? by .+?\\.");
        bwFinalKillMsg = Pattern.compile(".+? by .+?\\. FINAL KILL!");
        duelKillMsg = Pattern.compile(".+? was kill by .+?\\.");
        
        this.client = hyperiumIn;
        
        queueWorker = new Thread(() -> {
            while (!Thread.interrupted())
                while (!this.scheduledTasks.isEmpty()) {
                    Util.runTask((FutureTask<?>) this.scheduledTasks.poll(), Hyperium.LOGGER);
                }
        }, "Capture Worker");
        queueWorker.start();
    }

    @InvokeEvent
    public void onTick(RenderEvent event){
        if(CaptureXSetting.mode != CaptureMode.VIDEO)return;
        // 1 seconds backward
        if(backwardFrames.size() > CaptureXSetting.captureLength * 20)
            backwardFrames.poll();
        Minecraft mc = Minecraft.getMinecraft();
        final Framebuffer fb = (Framebuffer) SerializationUtils.clone((Serializable) mc.getFramebuffer());
        try {
            backwardFrames.add(mc.addScheduledTask(()->FrameRenderer.getImageFromFrameBuffer(fb, mc.displayWidth, mc.displayHeight)).get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void onKill() {
        final Queue<BufferedImage> finalBackwardsBuffer = new ArrayDeque<>(backwardFrames);
        addScheduledTask(() -> {
            switch (CaptureXSetting.mode){
                case VIDEO:
                    try {
                        Hyperium.INSTANCE.getNotification().display("CaptureX", "Rendering kill", 3);
                        CapturePack pack = new CapturePack(finalBackwardsBuffer, "kill");
                        FFMpeg.run(pack, "C:\\FFmpeg\\bin\\ffmpeg.exe", "kill", "kill");
                        pack.cleanup();
                        Hyperium.INSTANCE.getNotification().display("CaptureX", "Kill captured", 3);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Hyperium.INSTANCE.getNotification().display("CaptureX", "Failed to capture kill", 3);
                    }
                    break;
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
    
    
    @InvokeEvent
    public void onMinigameJoin(JoinMinigameEvent event) {
        this.currentGame = event.getMinigame();
    }
    
    @InvokeEvent
    public void onChat(ChatEvent event) {
        if (friendRequestPattern.matcher(ChatColor.stripColor(event.getChat().getUnformattedText())).matches()) {
            String withoutRank = ChatColor.stripColor(event.getChat().getUnformattedText());
            withoutRank = withoutRank.replaceAll("Friend request from ", "");
            withoutRank = withoutRank.replaceAll(rankBracketPattern.pattern(), "");
            EventBus.INSTANCE.post(new HypixelFriendRequestEvent(withoutRank));
        }
        String msg = ChatColor.stripColor(event.getChat().getUnformattedText());
        if (this.client.getHandlers().getHypixelDetector().isHypixel()) {
            if (currentGame == null) {
                return;
            }
            switch (currentGame) {
                case SKYWARS:
                    if (swKillMsg.matcher(msg).matches()) {
                        if (msg.endsWith(Minecraft.getMinecraft().thePlayer.getName() + ".")) {
                            onKill();
                        }
                    }
                    break;
                case BEDWARS:
                    if (bwKillMsg.matcher(msg).matches() || bwFinalKillMsg.matcher(msg).matches()) {
                        msg = msg.replace(" FINAL KILL!", "");
                    }
                    if (msg.endsWith(Minecraft.getMinecraft().thePlayer.getName() + ".")) {
                        onKill();
                    }
                    break;
                case DUELS:
                    if (duelKillMsg.matcher(msg).matches()) {
                        if (msg.endsWith(Minecraft.getMinecraft().thePlayer.getName() + ".")) {
                            onKill();
                        }
                    }
            }
        }
    }
}
