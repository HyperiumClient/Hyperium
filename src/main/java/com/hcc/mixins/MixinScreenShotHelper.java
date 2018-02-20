package com.hcc.mixins;

import com.hcc.utils.ChatColor;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ScreenShotHelper;
import org.apache.logging.log4j.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.IntBuffer;

@Mixin(ScreenShotHelper.class)
public class MixinScreenShotHelper {

    @Shadow
    private static IntBuffer pixelBuffer;

    @Shadow
    private static int[] pixelValues;

    @Shadow
    @Final
    private static Logger logger;

    @Shadow
    private static File getTimestampedPNGFileForDirectory(File gameDirectory) {
        return null;
    }


    /**
     * Saves a screenshot in the game directory with the given file name (or null to generate a time-stamped name).
     * Fixes MC-113208 and MC-117793
     * TODO: Imgur uploader
     *
     * @param gameDirectory
     * @param screenshotName
     * @param width
     * @param height
     * @param buffer
     * @return
     * @author Kevin Brewster & Mojang
     */
    @Overwrite
    public static IChatComponent saveScreenshot(File gameDirectory, String screenshotName, int width, int height, Framebuffer buffer) {
        try {
            File file1 = new File(gameDirectory, "screenshots");
            file1.mkdir();

            if (OpenGlHelper.isFramebufferEnabled()) {
                width = buffer.framebufferTextureWidth;
                height = buffer.framebufferTextureHeight;
            }

            int i = width * height;

            if (pixelBuffer == null || pixelBuffer.capacity() < i) {
                pixelBuffer = BufferUtils.createIntBuffer(i);
                pixelValues = new int[i];
            }

            GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, 1);
            GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
            pixelBuffer.clear();

            if (OpenGlHelper.isFramebufferEnabled()) {
                GlStateManager.bindTexture(buffer.framebufferTexture);
                GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, pixelBuffer);
            } else {
                GL11.glReadPixels(0, 0, width, height, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, pixelBuffer);
            }

            pixelBuffer.get(pixelValues);
            TextureUtil.processPixelValues(pixelValues, width, height);
            BufferedImage bufferedimage = null;

            if (OpenGlHelper.isFramebufferEnabled()) {
                bufferedimage = new BufferedImage(buffer.framebufferWidth, buffer.framebufferHeight, 1);
                int j = buffer.framebufferTextureHeight - buffer.framebufferHeight;

                for (int k = j; k < buffer.framebufferTextureHeight; ++k) {
                    for (int l = 0; l < buffer.framebufferWidth; ++l) {
                        bufferedimage.setRGB(l, k - j, pixelValues[k * buffer.framebufferTextureWidth + l]);
                    }
                }
            } else {
                bufferedimage = new BufferedImage(width, height, 1);
                bufferedimage.setRGB(0, 0, width, height, pixelValues, 0, width);
            }

            File file2;

            if (screenshotName == null) {
                file2 = getTimestampedPNGFileForDirectory(file1);
            } else {
                file2 = new File(file1, screenshotName);
            }

            ImageIO.write(bufferedimage, "png", file2);
            IChatComponent ichatcomponent = new ChatComponentText(
                ChatColor.RED + "[HCC] " + ChatColor.WHITE + "Uploaded to " + ChatColor.UNDERLINE + file2.getName());
            ichatcomponent.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, file2.getCanonicalPath()));
            return ichatcomponent;
        } catch (Exception exception) {
            logger.warn("Couldn\'t save screenshot", exception);
            return new ChatComponentTranslation("screenshot.failure", exception.getMessage());
        }
    }

}
