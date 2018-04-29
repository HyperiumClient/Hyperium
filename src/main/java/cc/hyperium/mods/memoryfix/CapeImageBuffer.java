package cc.hyperium.mods.memoryfix;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.util.ResourceLocation;

import java.awt.image.BufferedImage;
import java.lang.ref.WeakReference;

@SuppressWarnings("unused")
public class CapeImageBuffer implements IImageBuffer {

    public final WeakReference<AbstractClientPlayer> playerRef;
    public final ResourceLocation resourceLocation;
    public final ImageBufferDownload imageBufferDownload;

    public CapeImageBuffer(AbstractClientPlayer player, ResourceLocation resourceLocation) {
        playerRef = new WeakReference<>(player);
        this.resourceLocation = resourceLocation;
        imageBufferDownload = new ImageBufferDownload();
    }

    private static BufferedImage parseCape(BufferedImage image) {
        return null;
    }

    private static void setLocationOfCape(AbstractClientPlayer player, ResourceLocation resourceLocation) {
    }

    @Override
    public BufferedImage parseUserSkin(BufferedImage image) {
        // ClassTransformer will remap to:
        // CapeUtils.parseCape(image);
        return parseCape(image);
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
}