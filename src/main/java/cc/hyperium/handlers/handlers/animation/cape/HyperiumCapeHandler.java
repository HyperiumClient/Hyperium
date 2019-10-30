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

import cc.hyperium.purchases.HyperiumPurchase;
import cc.hyperium.purchases.PurchaseApi;
import cc.hyperium.utils.HyperiumCapeUtils;
import cc.hyperium.utils.JsonHolder;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

import java.awt.image.BufferedImage;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class HyperiumCapeHandler {

    public static final Queue<ResourceLocation> LOCATION_CACHE = new ConcurrentLinkedQueue<>();

    private ResourceLocation location;
    private boolean ready;

    public HyperiumCapeHandler(GameProfile profile) {
        Multithreading.runAsync(() -> {
            HyperiumPurchase purchase = PurchaseApi.getInstance().getPackageSync(profile.getId());
            JsonHolder holder = purchase.getPurchaseSettings().optJSONObject("cape");
            String type = holder.optString("type");
            String url = null;

            if (type.equals("CUSTOM_IMAGE")) {
                url = holder.optString("url");
            } else if (!type.isEmpty()) {
                JsonHolder atlasHolder = PurchaseApi.getInstance().getCapeAtlas().optJSONObject(type);
                url = atlasHolder.optString("url");
                if (url.isEmpty()) return;
            }

            ResourceLocation resourceLocation = new ResourceLocation(String.format("hyperium/capes/%s.png", profile.getId()));

            TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
            ThreadDownloadImageData threadDownloadImageData = new ThreadDownloadImageData(null, url, null, new IImageBuffer() {
                @Override
                public BufferedImage parseUserSkin(BufferedImage image) {
                    return HyperiumCapeUtils.parseCape(image);
                }

                @Override
                public void skinAvailable() {
                    LOCATION_CACHE.add(location = resourceLocation);
                    ready = true;
                }
            });

            try {
                textureManager.loadTexture(resourceLocation, threadDownloadImageData);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @SuppressWarnings("unused")
    public ResourceLocation getLocationCape() {
        if (ready) return location;
        return null;
    }

}
