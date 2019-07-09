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

import cc.hyperium.Hyperium;
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

    private static File getTimestampedPNGFileForDirectory(File gameDirectory) {
        String dateFormatting = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(new Date());
        int screenshotCount = 1;
        File screenshot;

        while (true) {
            screenshot = new File(gameDirectory, dateFormatting + ((screenshotCount == 1) ? "" : ("_" + screenshotCount)) + ".png");
            if (!screenshot.exists()) {
                break;
            }

            ++screenshotCount;
        }

        return screenshot;
    }

    private static void processPixelValues(final int[] pixels, final int displayWidth, final int displayHeight) {
        final int[] aint = new int[displayWidth];
        for (int yValues = displayHeight / 2, val = 0; val < yValues; ++val) {
            System.arraycopy(pixels, val * displayWidth, aint, 0, displayWidth);
            System.arraycopy(pixels, (displayHeight - 1 - val) * displayWidth, pixels, val * displayWidth, displayWidth);
            System.arraycopy(aint, 0, pixels, (displayHeight - 1 - val) * displayWidth, displayWidth);
        }
    }

    @Override
    public void run() {
        processPixelValues(pixelValues, width, height);

        BufferedImage image;
        File screenshot = getTimestampedPNGFileForDirectory(screenshotDir);

        Cancellable cancellable = new Cancellable();
        TriggerType.SCREENSHOT_TAKEN.triggerAll(screenshot, cancellable);

        if (cancellable.isCancelled()) {
            return;
        }

        try {
            if (OpenGlHelper.isFramebufferEnabled()) {
                image = new BufferedImage(frameBuffer.framebufferWidth, frameBuffer.framebufferHeight, 1);

                int texHeight;

                for (int heightSize = texHeight = frameBuffer.framebufferTextureHeight - frameBuffer.framebufferHeight;
                     texHeight < frameBuffer.framebufferTextureHeight; ++texHeight) {
                    for (int widthSize = 0; widthSize < frameBuffer.framebufferWidth; ++widthSize) {
                        image.setRGB(widthSize, texHeight - heightSize, pixelValues[texHeight * frameBuffer.framebufferTextureWidth + widthSize]);
                    }
                }
            } else {
                image = new BufferedImage(width, height, 1);
                image.setRGB(0, 0, width, height, pixelValues, 0, width);
            }

            ImageIO.write(image, "png", screenshot);

            if (!upload) {
                IChatComponent chatComponent = new ChatComponentText(
                    ChatColor.RED + "[Hyperium] " + ChatColor.WHITE + "Captured to " + ChatColor.UNDERLINE + screenshot.getName());
                chatComponent.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, screenshot.getCanonicalPath()));
                Minecraft.getMinecraft().thePlayer.addChatMessage(chatComponent);
            } else {
                new ImgurUploader("649f2fb48e59767", screenshot).run();
            }
        } catch (Exception e) {
            Hyperium.LOGGER.warn("Couldn't save {} : {}", screenshot, e);
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentTranslation("screenshot.failure", e.getMessage()));
        }
    }
}
