package cc.hyperium.mixins.renderer;

import cc.hyperium.mixinsimp.renderer.HyperiumThreadDownloadImageData;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.io.File;

@Mixin(ThreadDownloadImageData.class)
public abstract class MixinThreadDownloadImageData {

    @Shadow
    @Final
    private String imageUrl;
    @Shadow
    @Final
    private File cacheFile;
    @Shadow
    @Final
    private IImageBuffer imageBuffer;

    private HyperiumThreadDownloadImageData hyperiumThreadDownloadImageData = new HyperiumThreadDownloadImageData();

    /**
     * @author Sk1er
     */
    @Overwrite
    protected void loadTextureFromServer() {
        hyperiumThreadDownloadImageData.loadTextureFromServer(imageUrl, cacheFile, imageBuffer, (ThreadDownloadImageData) (Object) this);
    }
}
