package cc.hyperium.handlers.handlers.animation;

import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.SpawnpointChangeEvent;
import cc.hyperium.utils.CapeUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import java.awt.image.BufferedImage;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CapeHandler {

    private static final Map<UUID, ResourceLocation> capes = new HashMap<>();

    private static final ArrayList<UUID> pendingRequests = new ArrayList<>();

    public static void loadCape(final UUID uuid) {

        if (hasPendingRequests(uuid)) {
            return;
        }

        setCape(uuid, null);
        // change this shit @Sk1er
        String url = "http://s.optifine.net/capes/Plancke.png";
        ResourceLocation resourceLocation = new ResourceLocation(
                String.format("hyperium/capes/%s.png", new Date().getTime())
        );
        TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
        ThreadDownloadImageData threadDownloadImageData = new ThreadDownloadImageData(null, url, null, new IImageBuffer() {

            @Override
            public BufferedImage parseUserSkin(BufferedImage image) {

                return CapeUtils.parseCape(image);
            }

            @Override
            public void skinAvailable() {
                CapeHandler.setCape(uuid, resourceLocation);
                CapeHandler.pendingRequests.remove(uuid);
            }
        });
        textureManager.loadTexture(resourceLocation, threadDownloadImageData);
        CapeHandler.pendingRequests.add(uuid);
    }

    public static void setCape(UUID uuid, ResourceLocation resourceLocation) {

        capes.put(uuid, resourceLocation);
    }

    public static void deleteCape(UUID uuid) {

        capes.remove(uuid);
    }

    public static ResourceLocation getCape(UUID uuid) {
        return capes.getOrDefault(uuid, null);
    }

    public static boolean hasCape(UUID uuid) {

        boolean hasCape = capes.containsKey(uuid);
        ResourceLocation resourceLocation = capes.get(uuid);

        if (hasCape && resourceLocation == null && !hasPendingRequests(uuid)) {
            loadCape(uuid);
            return false;
        }

        return hasCape;
    }

    public static void resetCapes() {

        for (UUID userId : capes.keySet()) {
            capes.put(userId, null);
        }
    }
    private static boolean hasPendingRequests(UUID uuid) {

        return pendingRequests.contains(uuid);
    }

}
