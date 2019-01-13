package cc.hyperium.mixins.renderer;

import cc.hyperium.mixinsimp.renderer.HyperiumThreadDownloadImageData;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.awt.image.BufferedImage;
import java.io.File;

@Mixin(ThreadDownloadImageData.class)
public abstract class MixinThreadDownloadImageData extends SimpleTexture {

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

    public MixinThreadDownloadImageData(ResourceLocation textureResourceLocation) {
        super(textureResourceLocation);
    }

    /**
     * @author Sk1er
     * @reason Create thread pool for ThreadDownloadImageData to stop excessive concurrency and not create thousands
     */
    @Overwrite
    protected void loadTextureFromServer() {
        hyperiumThreadDownloadImageData.loadTextureFromServer(imageUrl, cacheFile, imageBuffer, (ThreadDownloadImageData) (Object) this, textureLocation);
    }
}
