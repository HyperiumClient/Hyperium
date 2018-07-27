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
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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

    @Shadow
    public abstract void setBufferedImage(BufferedImage bufferedImageIn);

    /**
     * @author Sk1er
     */
    @Overwrite
    protected void loadTextureFromServer() {
        hyperiumThreadDownloadImageData.loadTextureFromServer(imageUrl, cacheFile, imageBuffer, (ThreadDownloadImageData) (Object) this,textureLocation);
    }

    @Inject(method = "checkTextureUploaded", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/TextureUtil;uploadTextureImage(ILjava/awt/image/BufferedImage;)I", shift = At.Shift.AFTER))
    public void test(CallbackInfo info) {
        setBufferedImage(null);
    }
}
