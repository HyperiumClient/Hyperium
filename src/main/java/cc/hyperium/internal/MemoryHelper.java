package cc.hyperium.internal;

import cc.hyperium.Hyperium;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.TickEvent;
import cc.hyperium.event.WorldUnloadEvent;
import cc.hyperium.mixins.client.renderer.IMixinTextureManager;
import cc.hyperium.mixins.client.renderer.IMixinThreadDownloadImageData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;
import net.minecraft.util.ResourceLocation;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MemoryHelper {

    private List<ResourceLocation> locations = new ArrayList<>();
    private int tickCounter = 0;

    public static MemoryHelper INSTANCE;

    public MemoryHelper() {
        INSTANCE = this;
    }

    @InvokeEvent
    public void worldEvent(WorldUnloadEvent event) {
        TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
        Map<ResourceLocation, ITextureObject> mapTextureObjects = ((IMixinTextureManager) textureManager).getMapTextureObjects();
        List<ResourceLocation> removes = new ArrayList<>();

        for (ResourceLocation resourceLocation : mapTextureObjects.keySet()) {
            ITextureObject iTextureObject = mapTextureObjects.get(resourceLocation);
            if (iTextureObject instanceof ThreadDownloadImageData) {
                IImageBuffer imageBuffer = ((IMixinThreadDownloadImageData) iTextureObject).getImageBuffer();
                Class<? extends IImageBuffer> aClass = imageBuffer.getClass();

                // Optifine
                if (aClass.getName().equalsIgnoreCase("CapeImageBuffer")) {
                    removes.add(resourceLocation);
                }
            }
        }

        removes.forEach(this::deleteSkin);
        locations.forEach(this::deleteSkin);
        Hyperium.LOGGER.info("Deleted " + (removes.size() + locations.size()) + " cosmetic items / skins");
        locations.clear();
    }

    @InvokeEvent
    public void tickEvent(TickEvent event) {
        if (tickCounter % 20 * 60 == 0) {
            Minecraft.memoryReserve = new byte[0];
            try {
                Field resourceCache = LaunchClassLoader.class.getDeclaredField("resourceCache");
                resourceCache.setAccessible(true);
                ((Map) resourceCache.get(Launch.classLoader)).clear();

                Field packageManifests = LaunchClassLoader.class.getDeclaredField("packageManifests");
                packageManifests.setAccessible(true);
                ((Map) packageManifests.get(Launch.classLoader)).clear();
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        }

        tickCounter++;
    }

    public void queueDelete(ResourceLocation location) {
        if (location == null) return;
        locations.add(location);
    }

    private void deleteSkin(ResourceLocation skinLocation) {
        if (skinLocation == null) return;

        TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
        Map<ResourceLocation, ITextureObject> mapTextureObjects = ((IMixinTextureManager) textureManager).getMapTextureObjects();
        textureManager.deleteTexture(skinLocation);
        mapTextureObjects.remove(skinLocation); // not needed with optifine but needed without it
    }
}
