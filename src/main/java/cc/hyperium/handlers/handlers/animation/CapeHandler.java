package cc.hyperium.handlers.handlers.animation;

import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.WorldChangeEvent;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.purchases.HyperiumPurchase;
import cc.hyperium.purchases.PurchaseApi;
import cc.hyperium.utils.CapeUtils;
import cc.hyperium.utils.JsonHolder;
import cc.hyperium.utils.UUIDUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

import java.awt.image.BufferedImage;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class CapeHandler {

    public static final ReentrantLock LOCK = new ReentrantLock();
    private final ConcurrentHashMap<UUID, ResourceLocation> capes = new ConcurrentHashMap<>();
    private final ResourceLocation loadingResource = new ResourceLocation("");

    @InvokeEvent
    public void worldSwap(WorldChangeEvent event) {
        UUID id = UUIDUtil.getClientUUID();
        ResourceLocation resourceLocation = capes.get(id);
        TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();

        for (ResourceLocation location : capes.values()) {
            if (location != null && location.equals(resourceLocation))
                continue;
            ITextureObject texture = textureManager.getTexture(location);
            if (texture instanceof ThreadDownloadImageData) {
                //Unlink the buffered image so garbage collector can do its magic
                ((ThreadDownloadImageData) texture).setBufferedImage(null);
            }
            textureManager.deleteTexture(location);
        }
        capes.clear();
        if (resourceLocation != null)
            capes.put(id, resourceLocation);
    }

    public void loadCape(final UUID uuid, String url) {
        if (capes.get(uuid) != null && !capes.get(uuid).equals(loadingResource))
            return;
        capes.put(uuid, loadingResource);

        ResourceLocation resourceLocation = new ResourceLocation(
                String.format("hyperium/capes/%s.png", System.nanoTime())
        );


        TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
        ThreadDownloadImageData threadDownloadImageData = new ThreadDownloadImageData(null, url, null, new IImageBuffer() {

            @Override
            public BufferedImage parseUserSkin(BufferedImage image) {
                return CapeUtils.parseCape(image);
            }

            @Override
            public void skinAvailable() {
                CapeHandler.this.setCape(uuid, resourceLocation);
            }
        });
        try {
            LOCK.lock();
            textureManager.loadTexture(resourceLocation, threadDownloadImageData);
        } catch (Exception e) {

        } finally {
            LOCK.unlock();
        }
    }

    public void setCape(UUID uuid, ResourceLocation resourceLocation) {
        capes.put(uuid, resourceLocation);
    }

    public ResourceLocation getCape(final AbstractClientPlayer player) {
        UUID uuid = player.getUniqueID();
        ResourceLocation orDefault = capes.getOrDefault(uuid, null);
        if (orDefault == null) {
            Multithreading.runAsync(() -> {
                HyperiumPurchase hyperiumPurchase = PurchaseApi.getInstance().getPackageSync(uuid);
                String s = hyperiumPurchase.getPurchaseSettings().optJSONObject("cape").optString("type");
                if (!s.isEmpty()) {
                    JsonHolder jsonHolder = PurchaseApi.getInstance().getCapeAtlas().optJSONObject(s);
                    String url = jsonHolder.optString("url");
                    if (!url.isEmpty()) {
                        loadCape(uuid, url);
                        return;
                    }
                }
                loadCape(uuid, "http://s.optifine.net/capes/" + player.getGameProfile().getName() + ".png");

            });
            return capes.get(uuid);
        }
        if (orDefault.equals(loadingResource)) {
            return null;
        }
        return orDefault;
    }


    public void deleteCape(UUID id) {
        this.capes.remove(id);
    }
}
