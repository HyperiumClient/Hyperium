package cc.hyperium.mixinsimp;

import cc.hyperium.config.Settings;
import cc.hyperium.utils.ChatColor;
import cc.hyperium.utils.mods.AsyncScreenshotSaver;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ScreenShotHelper;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.io.File;
import java.nio.IntBuffer;

public class HyperiumScreenshotHelper {

    private ScreenShotHelper parent;

    public HyperiumScreenshotHelper(ScreenShotHelper parent) {
        this.parent = parent;
    }


    public static IChatComponent saveScreenshot(File gameDirectory, String screenshotName, int width, int height, Framebuffer buffer, IntBuffer pixelBuffer, int[] pixelValues) {
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
        GL11.glPixelStorei(3333, 1);
        GL11.glPixelStorei(3317, 1);
        pixelBuffer.clear();
        if (OpenGlHelper.isFramebufferEnabled()) {
            GlStateManager.bindTexture(buffer.framebufferTexture);
            GL11.glGetTexImage(3553, 0, 32993, 33639, pixelBuffer);
        } else {
            GL11.glReadPixels(0, 0, width, height, 32993, 33639, pixelBuffer);
        }
        boolean upload = true;
        pixelBuffer.get(pixelValues);

        if (!Settings.DEFAULT_UPLOAD_SS) {
            upload = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
        }
        new Thread(new AsyncScreenshotSaver(width, height, pixelValues, Minecraft.getMinecraft().getFramebuffer(), new File(Minecraft.getMinecraft().mcDataDir, "screenshots"), upload)).start();
        if (!upload) return new ChatComponentText(ChatColor.RED + "[Hyperium] " + ChatColor.WHITE + "Capturing...");
        return new ChatComponentText(ChatColor.RED + "[Hyperium] " + ChatColor.WHITE + "Uploading...");
    }
}
