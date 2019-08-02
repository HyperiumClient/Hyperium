/*
 *       Copyright (C) 2018-present Hyperium <https://hyperium.cc/>
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU Lesser General Public License as published
 *       by the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU Lesser General Public License for more details.
 *
 *       You should have received a copy of the GNU Lesser General Public License
 *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.internal;

import cc.hyperium.Hyperium;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.TickEvent;
import cc.hyperium.event.WorldUnloadEvent;
import cc.hyperium.mixins.client.renderer.texture.IMixinTextureManager;
import cc.hyperium.mixins.client.renderer.IMixinThreadDownloadImageData;
import cc.hyperium.mixinsimp.client.renderer.entity.IMixinRenderManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.entity.RenderPlayer;
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
    private int tickCounter;

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
//        locations.forEach(this::deleteSkin);

        int del = 0;
        for (RenderPlayer value : ((IMixinRenderManager) Minecraft.getMinecraft().getRenderManager()).getSkinMap().values()) {
            ModelPlayer mainModel = value.getMainModel();
            Class<?> superClass = mainModel.getClass().getSuperclass();
            for (Field field : superClass.getDeclaredFields()) {
                field.setAccessible(true);
                try {
                    Object o = field.get(mainModel);
                    if (o != null) {
                        try {
                            Field entityIn = o.getClass().getSuperclass().getDeclaredField("entityIn");
                            entityIn.setAccessible(true);
                            Object o1 = entityIn.get(o);
                            if (o1 != null) {
                                entityIn.set(o, null);
                                del++;
                            }
                        } catch (IllegalAccessException | NoSuchFieldException ignored) {
                        }

                        try {
                            Field clientPlayer = o.getClass().getSuperclass().getDeclaredField("clientPlayer");
                            clientPlayer.setAccessible(true);
                            Object o1 = clientPlayer.get(o);
                            if (o1 != null) {
                                clientPlayer.set(o, null);
                                del++;
                            }
                        } catch (IllegalAccessException | NoSuchFieldException ignored) {
                        }
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        Hyperium.LOGGER.info("Deleted " + (removes.size() + locations.size() + del) + " cosmetic items / skins");
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
