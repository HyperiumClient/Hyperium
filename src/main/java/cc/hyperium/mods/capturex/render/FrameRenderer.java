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

package cc.hyperium.mods.capturex.render;

import cc.hyperium.mods.capturex.CaptureCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
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
    public static File render(final Long timestamp, final int n, final BufferedImage buffer, String prefix) {
        try {
            File file1 = new File(CaptureCore.captureXDir, prefix+"-" + timestamp);
            file1.mkdir();
            File file2;
            file2 = new File(file1, String.format("img%03d.png", n));
            ImageIO.write(buffer, "png", file2);
            return file2;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    public synchronized static BufferedImage getImageFromFrameBuffer(Framebuffer buffer, int width, int height) {
        int k = buffer.framebufferTextureWidth * buffer.framebufferTextureHeight;
        if (pixelBuffer == null || pixelBuffer.capacity() < k) {
            pixelBuffer = BufferUtils.createIntBuffer(k);
            pixelValues = new int[k];
        }
        GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, 1);
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
        pixelBuffer.clear();
        GlStateManager.bindTexture(buffer.framebufferTexture);
        GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, pixelBuffer);
        pixelBuffer.get(pixelValues);
        TextureUtil.processPixelValues(pixelValues, buffer.framebufferTextureWidth, buffer.framebufferTextureHeight);
        BufferedImage bufferedimage = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        int l = buffer.framebufferTextureHeight - buffer.framebufferHeight;
        bufferedimage.setRGB(0, 0, width, height, pixelValues, l*buffer.framebufferTextureWidth, buffer.framebufferTextureWidth);
        return bufferedimage;
    }
}
