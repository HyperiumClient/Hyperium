package cc.hyperium.hooks;

import cc.hyperium.Hyperium;
import cc.hyperium.config.Settings;
import cc.hyperium.handlers.handlers.keybinds.HyperiumKeybind;
import cc.hyperium.utils.ChatColor;
import cc.hyperium.utils.mods.AsyncScreenshotSaver;
import java.io.File;
import java.nio.IntBuffer;
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

public class ScreenShotHelperHook {

  public static IChatComponent saveScreenshot(int width, int height, Framebuffer buffer,
      IntBuffer pixelBuffer, int[] pixelValues) {
    File file1 = new File(Minecraft.getMinecraft().mcDataDir, "screenshots");
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
      GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV,
          pixelBuffer);
    } else {
      GL11.glReadPixels(0, 0, width, height, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV,
          pixelBuffer);
    }

    boolean upload = true;
    pixelBuffer.get(pixelValues);

    if (!Settings.DEFAULT_UPLOAD_SS) {
      HyperiumKeybind uploadBind = Hyperium.INSTANCE.getHandlers().getKeybindHandler()
          .getBind("Upload Screenshot");
      int keyCode = uploadBind.getKeyCode();
      upload = keyCode < 0 ? Mouse.isButtonDown(keyCode + 100) : Keyboard.isKeyDown(keyCode);
    }

    new Thread(new AsyncScreenshotSaver(width, height, pixelValues,
        Minecraft.getMinecraft().getFramebuffer(),
        new File(Minecraft.getMinecraft().mcDataDir, "screenshots"), upload)).start();

    if (!upload) {
      return Settings.HYPERIUM_CHAT_PREFIX ? new ChatComponentText(
          ChatColor.RED + "[Hyperium] " + ChatColor.WHITE + "Capturing...") :
          new ChatComponentText(ChatColor.WHITE + "Capturing...");
    }

    return Settings.HYPERIUM_CHAT_PREFIX ? new ChatComponentText(
        ChatColor.RED + "[Hyperium] " + ChatColor.WHITE + "Uploading...") :
        new ChatComponentText(ChatColor.WHITE + "Uploading...");
  }
}
