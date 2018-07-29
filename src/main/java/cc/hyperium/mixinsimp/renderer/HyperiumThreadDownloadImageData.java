package cc.hyperium.mixinsimp.renderer;

import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.util.ResourceLocation;

import java.io.File;

public class HyperiumThreadDownloadImageData {


    public void loadTextureFromServer(String imageUrl, File cacheFile, IImageBuffer imageBuffer, ThreadDownloadImageData threadDownloadImageData, ResourceLocation textureLocation) {
        CachedThreadDownloader cachedThreadDownloader = new CachedThreadDownloader(imageUrl, cacheFile, imageBuffer, threadDownloadImageData, textureLocation);
        cachedThreadDownloader.process();
    }
}
