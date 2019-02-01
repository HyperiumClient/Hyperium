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


import cc.hyperium.utils.ChatColor;
import com.chattriggers.ctjs.triggers.TriggerType;
import com.chattriggers.ctjs.utils.Cancellable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import org.apache.logging.log4j.LogManager;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Lagless screenshots
 *
 * @author OrangeMarshall
 */
public class AsyncScreenshotSaver implements Runnable {
    private final int width;
    private final int height;
    private final int[] pixelValues;
    private final Framebuffer frameBuffer;
    private final File screenshotDir;
    private final boolean upload;

    public AsyncScreenshotSaver(final int width, final int height, final int[] pixelValues, final Framebuffer frameBuffer, final File screenshotDir, final boolean upload) {
        this.width = width;
        this.height = height;
        this.pixelValues = pixelValues;
        this.frameBuffer = frameBuffer;
        this.screenshotDir = screenshotDir;
        this.upload = upload;
    }

    private static File getTimestampedPNGFileForDirectory(final File gameDirectory) {
        final String s = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(new Date());
        int i = 1;
        File screenshot;
        while (true) {
            screenshot = new File(gameDirectory, s + ((i == 1) ? "" : ("_" + i)) + ".png");
            if (!screenshot.exists()) {
                break;
            }
            ++i;
        }
        return screenshot;
    }

    private static void processPixelValues(final int[] values, final int displayWidth, final int displayHeight) {
        final int[] aint = new int[displayWidth];
        for (int i = displayHeight / 2, j = 0; j < i; ++j) {
            System.arraycopy(values, j * displayWidth, aint, 0, displayWidth);
            System.arraycopy(values, (displayHeight - 1 - j) * displayWidth, values, j * displayWidth, displayWidth);
            System.arraycopy(aint, 0, values, (displayHeight - 1 - j) * displayWidth, displayWidth);
        }
    }

    @Override
    public void run() {
        processPixelValues(this.pixelValues, this.width, this.height);
        BufferedImage bufferedimage;
        final File file2 = getTimestampedPNGFileForDirectory(this.screenshotDir);

        Cancellable cancellable = new Cancellable();
        TriggerType.SCREENSHOT_TAKEN.triggerAll(file2, cancellable);

        if (cancellable.isCancelled()) {
            return;
        }

        try {
            if (OpenGlHelper.isFramebufferEnabled()) {
                bufferedimage = new BufferedImage(this.frameBuffer.framebufferWidth, this.frameBuffer.framebufferHeight, 1);
                int k;
                for (int j = k = this.frameBuffer.framebufferTextureHeight - this.frameBuffer.framebufferHeight; k < this.frameBuffer.framebufferTextureHeight; ++k) {
                    for (int l = 0; l < this.frameBuffer.framebufferWidth; ++l) {
                        bufferedimage.setRGB(l, k - j, this.pixelValues[k * this.frameBuffer.framebufferTextureWidth + l]);
                    }
                }
            } else {
                bufferedimage = new BufferedImage(this.width, this.height, 1);
                bufferedimage.setRGB(0, 0, this.width, this.height, this.pixelValues, 0, this.width);
            }
            ImageIO.write(bufferedimage, "png", file2);
            if (!upload) {
                IChatComponent ichatcomponent = new ChatComponentText(
                    ChatColor.RED + "[Hyperium] " + ChatColor.WHITE + "Captured to " + ChatColor.UNDERLINE + file2.getName());
                ichatcomponent.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, file2.getCanonicalPath()));
                Minecraft.getMinecraft().thePlayer.addChatMessage(ichatcomponent);
            } else {
                new ImgurUploader("649f2fb48e59767", file2).run();
            }
        } catch (Exception exception) {
            LogManager.getLogger().warn("Couldn't save screenshot", exception);
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentTranslation("screenshot.failure", exception.getMessage()));
        }
    }
}
