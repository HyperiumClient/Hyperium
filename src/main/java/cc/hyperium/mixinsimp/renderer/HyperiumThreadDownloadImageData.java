package cc.hyperium.mixinsimp.renderer;

import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ThreadDownloadImageData;

import java.io.File;

public class HyperiumThreadDownloadImageData {


    public void loadTextureFromServer(String imageUrl, File cacheFile, IImageBuffer imageBuffer, ThreadDownloadImageData threadDownloadImageData) {
        CachedThreadDownloader cachedThreadDownloader = new CachedThreadDownloader(imageUrl, cacheFile, imageBuffer, (ThreadDownloadImageData) (Object) this);
        cachedThreadDownloader.process();
    }
}
