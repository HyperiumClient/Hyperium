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

package cc.hyperium.mixinsimp.util;

import cc.hyperium.Hyperium;
import cc.hyperium.config.Settings;
import cc.hyperium.handlers.handlers.keybinds.HyperiumBind;
import cc.hyperium.utils.ChatColor;
import cc.hyperium.utils.mods.AsyncScreenshotSaver;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.io.File;
import java.nio.IntBuffer;

public class HyperiumScreenshotHelper {

    public static IChatComponent saveScreenshot(int width, int height, Framebuffer buffer, IntBuffer pixelBuffer, int[] pixelValues) {
        final File file1 = new File(Minecraft.getMinecraft().mcDataDir, "screenshots");
        file1.mkdir();

        if (OpenGlHelper.isFramebufferEnabled()) {
            width = buffer.framebufferTextureWidth;
            height = buffer.framebufferTextureHeight;
        }

        final int i = width * height;

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

        boolean upload = true;
        pixelBuffer.get(pixelValues);

        if (!Settings.DEFAULT_UPLOAD_SS) {
            HyperiumBind uploadBind = Hyperium.INSTANCE.getHandlers().getKeybindHandler().getBinding("Upload Screenshot");
            int keyCode = uploadBind.getKeyCode();
            upload = keyCode < 0 ? Mouse.isButtonDown(keyCode + 100) : Keyboard.isKeyDown(keyCode);
        }

        new Thread(new AsyncScreenshotSaver(width, height, pixelValues, Minecraft.getMinecraft().getFramebuffer(),
            new File(Minecraft.getMinecraft().mcDataDir, "screenshots"), upload)).start();
        if (!upload) {
            return Settings.HYPERIUM_CHAT_PREFIX ? new ChatComponentText(ChatColor.RED + "[Hyperium] " + ChatColor.WHITE + "Capturing...") :
                new ChatComponentText(ChatColor.WHITE + "Capturing...");
        }
        return Settings.HYPERIUM_CHAT_PREFIX ? new ChatComponentText(ChatColor.RED + "[Hyperium] " + ChatColor.WHITE + "Uploading...") :
            new ChatComponentText(ChatColor.WHITE + "Uploading...");
    }
}
