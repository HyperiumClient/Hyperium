package cc.hyperium.mixins.renderer;

import cc.hyperium.mixinsimp.renderer.CachedThreadDownloader;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;

@Mixin(ThreadDownloadImageData.class)
public abstract class MixinThreadDownloadImageData {
    @Shadow
    @Final
    private static AtomicInteger threadDownloadCounter;
    @Shadow
    @Final
    private static Logger logger;
    @Shadow
    private Thread imageThread;
    @Shadow
    @Final
    private String imageUrl;
    @Shadow
    @Final
    private File cacheFile;
    @Shadow
    @Final
    private IImageBuffer imageBuffer;

    @Shadow
    public abstract void setBufferedImage(BufferedImage bufferedImageIn);

    /**
     * @author Sk1er
     */
    @Overwrite
    protected void loadTextureFromServer() {
        CachedThreadDownloader cachedThreadDownloader = new CachedThreadDownloader(imageUrl, cacheFile, imageBuffer, (ThreadDownloadImageData) (Object) this);
        cachedThreadDownloader.process();

    }
}
