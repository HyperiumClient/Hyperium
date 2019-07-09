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

package cc.hyperium.handlers.handlers.animation.cape;

import cc.hyperium.config.Settings;
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
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

import java.awt.image.BufferedImage;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class CapeHandler {

    public static final ReentrantLock LOCK = new ReentrantLock();
    private final ConcurrentHashMap<UUID, ICape> capes = new ConcurrentHashMap<>();

    @InvokeEvent
    public void worldSwap(WorldChangeEvent event) {
        UUID id = UUIDUtil.getClientUUID();
        ICape selfCape = id == null ? null : capes.get(id);
        try {
            LOCK.lock();

            for (ICape cape : capes.values()) {
                if (selfCape != null && selfCape.equals(cape))
                    continue;
                cape.delete(Minecraft.getMinecraft().getTextureManager());
            }
            capes.clear();
            if (selfCape != null)
                capes.put(id, selfCape);
        } finally {
            LOCK.unlock();
        }
    }

    public void loadStaticCape(final UUID uuid, String url) {
        if (capes.get(uuid) != null && !capes.get(uuid).equals(NullCape.INSTANCE))
            return;
        capes.put(uuid, NullCape.INSTANCE);

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
                CapeHandler.this.setCape(uuid, new StaticCape(resourceLocation));
            }
        });
        try {
            LOCK.lock();
            textureManager.loadTexture(resourceLocation, threadDownloadImageData);
        } catch (Exception ignored) {
        } finally {
            LOCK.unlock();
        }
    }

    public void setCape(UUID uuid, ICape cape) {
        capes.put(uuid, cape);
    }


    public ResourceLocation getCape(final AbstractClientPlayer player) {
        UUID uuid = player.getUniqueID();

        if (isRealPlayer(uuid)) {
            ICape cape = capes.getOrDefault(uuid, null);
            if (cape == null) {
                setCape(player.getUniqueID(), NullCape.INSTANCE);
                Multithreading.runAsync(() -> {
                    HyperiumPurchase hyperiumPurchase = PurchaseApi.getInstance()
                        .getPackageSync(uuid);
                    JsonHolder holder = hyperiumPurchase.getPurchaseSettings().optJSONObject("cape");
                    String s = holder.optString("type");
                    if (s.equalsIgnoreCase("CUSTOM_IMAGE")) {
                        loadStaticCape(uuid, holder.optString("url"));
                        return;
                    } else if (!s.isEmpty()) {
                        JsonHolder jsonHolder = PurchaseApi.getInstance().getCapeAtlas()
                            .optJSONObject(s);
                        String url = jsonHolder.optString("url");
                        if (!url.isEmpty()) {
                            loadStaticCape(uuid, url);
                            return;
                        }
                    }

                    if (Settings.LOAD_OPTIFINE_CAPES) {
                        loadStaticCape(uuid, "http://s.optifine.net/capes/" + player.getGameProfile().getName() + ".png");
                    }
                });
                return capes.getOrDefault(uuid, NullCape.INSTANCE).get();
            }

            if (cape.equals(NullCape.INSTANCE)) {
                return null;
            }
            return cape.get();
        } else {
            return null;
        }
    }

    public boolean isRealPlayer(UUID uuid) {
        String s = uuid.toString().replace("-", "");
        return s.length() != 32 || s.charAt(12) == '4';
    }


    public void deleteCape(UUID id) {
        this.capes.remove(id);
    }
}
