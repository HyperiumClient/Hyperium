package cc.hyperium.handlers.handlers.animation;

import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.SpawnpointChangeEvent;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.purchases.PurchaseApi;
import cc.hyperium.utils.CapeUtils;
import cc.hyperium.utils.JsonHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CapeHandler {

    private final ConcurrentHashMap<UUID, ResourceLocation> capes = new ConcurrentHashMap<>();
    private final ResourceLocation loadingResource = new ResourceLocation("");

    @InvokeEvent
    public void worldSwap(SpawnpointChangeEvent event) {
        UUID id = Minecraft.getMinecraft().getSession().getProfile().getId();
        ResourceLocation resourceLocation = capes.get(id);
        capes.clear();
//        if (resourceLocation != null)
//            capes.put(id, resourceLocation);
    }

    public void loadCape(final UUID uuid,  String url) {
        if (capes.get(uuid) != null && !capes.get(uuid).equals(loadingResource))
            return;
        capes.put(uuid, loadingResource);

        ResourceLocation resourceLocation = new ResourceLocation(
                String.format("hyperium/capes/%s.png", new Date().getTime())
        );
        TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
        System.out.println("help");
        ThreadDownloadImageData threadDownloadImageData = new ThreadDownloadImageData(null, url, null, new IImageBuffer() {

            @Override
            public BufferedImage parseUserSkin(BufferedImage image) {
                System.out.println("Parsed");
                return CapeUtils.parseCape(image);
            }

            @Override
            public void skinAvailable() {
                System.out.println("Set cape");
                CapeHandler.this.setCape(uuid, resourceLocation);
            }
        });
        textureManager.loadTexture(resourceLocation, threadDownloadImageData);
    }

    public void setCape(UUID uuid, ResourceLocation resourceLocation) {
        capes.put(uuid, resourceLocation);
    }

    public ResourceLocation getCape(final UUID uuid) {
        ResourceLocation orDefault = capes.getOrDefault(uuid, null);
        if (orDefault == null) {
            Multithreading.runAsync(() -> PurchaseApi.getInstance().getPackageAsync(uuid, hyperiumPurchase -> {
                String s = hyperiumPurchase.getPurchaseSettings().optJSONObject("cape").optString("type");
                System.out.println("s = " + s);
                if (!s.isEmpty()) {
                    JsonHolder jsonHolder = PurchaseApi.getInstance().getCapeAtlas().optJSONObject(s);
                    String url = jsonHolder.optString("url");
                    System.out.println("url = " + url);
                    if (!url.isEmpty()) {
                        loadCape(uuid, url);
                    }
                }
            }));
            System.out.println("Starting load");
            return null;
        }
        if (orDefault.equals(loadingResource)) {
            return null;
        }
        return orDefault;
    }


    public void resetCapes() {

        for (UUID userId : capes.keySet()) {
            capes.put(userId, null);
        }
    }


}
