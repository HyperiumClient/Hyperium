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

package com.hcc.mods.capturex.render;

import com.hcc.mods.capturex.CaptureCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.IntBuffer;

public class FrameRenderer {
    private static IntBuffer pixelBuffer;
    private static int[] pixelValues;
    public static File render(Long timestamp, int n, Framebuffer buffer) {
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution sr = new ScaledResolution(mc);
        int width = sr.getScaledWidth(), height = sr.getScaledHeight();
        try {
            File file1 = new File(CaptureCore.captureXDir, "kill-" + timestamp);
            file1.mkdir();

            if (mc.addScheduledTask(OpenGlHelper::isFramebufferEnabled).get()) {
                width = buffer.framebufferTextureWidth;
                height = buffer.framebufferTextureHeight;
            }

            int i = width * height;

            if (pixelBuffer == null || pixelBuffer.capacity() < i) {
                pixelBuffer = BufferUtils.createIntBuffer(i);
                pixelValues = new int[i];
            }

            mc.addScheduledTask(() -> {
                GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, 1);
                GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
            }).get();

            pixelBuffer.clear();

            int finalWidth = width;
            int finalHeight = height;
            mc.addScheduledTask(() -> {
                if (OpenGlHelper.isFramebufferEnabled()) {
                    GlStateManager.bindTexture(buffer.framebufferTexture);
                    GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, pixelBuffer);
                } else {
                    GL11.glReadPixels(0, 0, finalWidth, finalHeight, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, pixelBuffer);
                }
            }).get();

            pixelBuffer.get(pixelValues);
            TextureUtil.processPixelValues(pixelValues, width, height);
            BufferedImage bufferedimage = null;
            if (mc.addScheduledTask(OpenGlHelper::isFramebufferEnabled).get()) {
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
            file2 = new File(file1, String.format("img0%02d.png", n));

            ImageIO.write(bufferedimage, "png", file2);
            return file2;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
