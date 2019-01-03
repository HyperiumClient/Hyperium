package cc.hyperium.mixinsimp.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class CachedThreadDownloader {
    private static final AtomicInteger counter = new AtomicInteger();
    private static final ExecutorService THREAD_POOL = new ThreadPoolExecutor(0, 100,
        60L, TimeUnit.SECONDS,
        new SynchronousQueue<>(),
        r -> {
            Thread thread = new Thread(r, "Texture Downloader #" + counter.getAndIncrement());
            thread.setDaemon(true);
            return thread;
        });

    static {
        ((ThreadPoolExecutor) THREAD_POOL).setRejectedExecutionHandler((r, executor) -> {
            // this will block if the queue is full
            try {
                executor.getQueue().put(r);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    private BufferedImage image;
    private String imageUrl;
    private File cacheFile;
    private IImageBuffer imageBuffer;
    private ThreadDownloadImageData base;
    private int code;
    private ResourceLocation location;

    public CachedThreadDownloader(String imageUrl, File cacheFile, IImageBuffer imageBuffer, ThreadDownloadImageData base, ResourceLocation location) {
        this.imageUrl = imageUrl;
        this.cacheFile = cacheFile;
        this.imageBuffer = imageBuffer;
        this.base = base;
        this.location = location;
    }

    public void download() {

        HttpURLConnection httpurlconnection = null;
//        ThreadDownloadImageData.logger.debug("Downloading http texture from {} to {}", new Object[]{ThreadDownloadImageData.this.imageUrl, ThreadDownloadImageData.this.cacheFile});

        try {
            httpurlconnection = (HttpURLConnection) (new URL(imageUrl)).openConnection(Minecraft.getMinecraft().getProxy());
            httpurlconnection.setRequestProperty("User-Agent", "Hyperium Client");
            httpurlconnection.setDoInput(true);
            httpurlconnection.setDoOutput(false);
            httpurlconnection.setUseCaches(true);
            httpurlconnection.connect();
            httpurlconnection.setConnectTimeout(15000);
            httpurlconnection.setReadTimeout(15000);
            int responseCode = httpurlconnection.getResponseCode();
            this.code = responseCode;
            if (responseCode / 100 == 2) {
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
            exception.printStackTrace(System.out);
            return;
        } finally {
            if (httpurlconnection != null) {
                httpurlconnection.disconnect();
            }

        }
    }

    public void process() {

        THREAD_POOL.execute(() -> {
            try {
                download();
                if (code != 404)
                    base.setBufferedImage(image);
                else {
                    Minecraft.getMinecraft().getTextureManager().deleteTexture(location);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


    }
}
