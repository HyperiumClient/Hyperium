package cc.hyperium.mods.memoryfix;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.util.ResourceLocation;

import java.awt.image.BufferedImage;
import java.lang.ref.WeakReference;

@SuppressWarnings("unused")
public class CapeImageBuffer implements IImageBuffer {

    public ImageBufferDownload imageBufferDownload;
    public final WeakReference<AbstractClientPlayer> playerRef;
    public final ResourceLocation resourceLocation;

    public CapeImageBuffer(AbstractClientPlayer player, ResourceLocation resourceLocation) {
        playerRef = new WeakReference<>(player);
        this.resourceLocation = resourceLocation;
        imageBufferDownload = new ImageBufferDownload();
    }

    @Override
    public BufferedImage parseUserSkin(BufferedImage image) {
        // ClassTransformer will remap to:
        // CapeUtils.parseCape(image);
        return parseCape(image);
    }

    private static BufferedImage parseCape(BufferedImage image) {
        return null;
    }

    @Override
    public void skinAvailable() {
        AbstractClientPlayer player = playerRef.get();
        if (player != null) {
            // ClassTransformer will remap to:
            // player.setLocationOfCape(resourceLocation);
            setLocationOfCape(player, resourceLocation);
        }
    }

    private static void setLocationOfCape(AbstractClientPlayer player, ResourceLocation resourceLocation) {
    }
}