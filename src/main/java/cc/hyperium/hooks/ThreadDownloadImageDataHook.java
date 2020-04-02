package cc.hyperium.hooks;

import cc.hyperium.mods.sk1ercommon.Multithreading;
import java.awt.image.BufferedImage;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.TextureUtil;
import org.apache.commons.io.FileUtils;

public class ThreadDownloadImageDataHook {

  public static void loadMultithreadedTexture(ThreadDownloadImageData data) {
    Multithreading.runAsync(() -> {
      HttpURLConnection connection = null;

      try {
        connection = (HttpURLConnection) (new URL(data.imageUrl)).openConnection(
            Minecraft.getMinecraft().getProxy());
        connection.setRequestProperty("User-Agent", "Hyperium Client");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.connect();

        if (connection.getResponseCode() / 100 == 2) {
          BufferedImage image;

          if (data.cacheFile != null) {
            FileUtils.copyInputStreamToFile(connection.getInputStream(), data.cacheFile);
            image = ImageIO.read(data.cacheFile);
          } else {
            image = TextureUtil.readBufferedImage(connection.getInputStream());
          }

          if (data.imageBuffer != null) {
            image = data.imageBuffer.parseUserSkin(image);
          }
          data.setBufferedImage(image);
        }
      } catch (Exception ignored) {
      } finally {
        if (connection != null) {
          connection.disconnect();
        }
      }
    });
  }

}
