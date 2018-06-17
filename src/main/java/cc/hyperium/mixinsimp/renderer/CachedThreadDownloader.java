package cc.hyperium.mixinsimp.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.TextureUtil;
import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;

public class CachedThreadDownloader {

    private BufferedImage image;
    private String imageUrl;
    private File cacheFile;
    private IImageBuffer imageBuffer;
    private ThreadDownloadImageData base;

    public CachedThreadDownloader(String imageUrl, File cacheFile, IImageBuffer imageBuffer, ThreadDownloadImageData base) {
        this.imageUrl = imageUrl;
        this.cacheFile = cacheFile;
        this.imageBuffer = imageBuffer;
        this.base = base;
    }

    public void download() {

        HttpURLConnection httpurlconnection = null;
//        ThreadDownloadImageData.logger.debug("Downloading http texture from {} to {}", new Object[]{ThreadDownloadImageData.this.imageUrl, ThreadDownloadImageData.this.cacheFile});

        try {
            httpurlconnection = (HttpURLConnection) (new URL(imageUrl)).openConnection(Minecraft.getMinecraft().getProxy());
            httpurlconnection.setDoInput(true);
            httpurlconnection.setDoOutput(false);
            httpurlconnection.connect();

            if (httpurlconnection.getResponseCode() / 100 == 2) {
                BufferedImage bufferedimage;

                if (cacheFile != null) {
                    FileUtils.copyInputStreamToFile(httpurlconnection.getInputStream(), cacheFile);
                    bufferedimage = ImageIO.read(cacheFile);
                } else {
                    bufferedimage = TextureUtil.readBufferedImage(httpurlconnection.getInputStream());
                }

                if (imageBuffer != null) {
                    bufferedimage = imageBuffer.parseUserSkin(bufferedimage);
                }
                this.image = bufferedimage;
                return;
            }
        } catch (Exception exception) {
            return;
        } finally {
            if (httpurlconnection != null) {
                httpurlconnection.disconnect();
            }

        }
    }

    public Runnable create() {
        return () -> {
            download();
            base.setBufferedImage(image);
        };


    }
}
