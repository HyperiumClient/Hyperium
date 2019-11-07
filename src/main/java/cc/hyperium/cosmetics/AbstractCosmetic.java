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

package cc.hyperium.cosmetics;

import cc.hyperium.Hyperium;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.network.PurchaseLoadEvent;
import cc.hyperium.event.world.WorldChangeEvent;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.purchases.EnumPurchaseType;
import cc.hyperium.purchases.PurchaseApi;
import cc.hyperium.utils.UUIDUtil;

import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by mitchellkatz on 3/17/18. Designed for production use on Sk1er.club
 */
public abstract class AbstractCosmetic {

    private final boolean selfOnly;
    private final EnumPurchaseType purchaseType;
    private final Map<UUID, Boolean> purchasedBy = new ConcurrentHashMap<>();
    private boolean selfUnlocked;

    public AbstractCosmetic(boolean selfOnly, EnumPurchaseType purchaseType) {
        this.selfOnly = selfOnly;
        this.purchaseType = purchaseType;
        try {
            PurchaseApi.getInstance().getPackageAsync(UUIDUtil.getClientUUID(), hyperiumPurchase -> {
                if (hyperiumPurchase == null && !Hyperium.INSTANCE.isDevEnv) {
                    Hyperium.LOGGER.error("Detected {} is null!", purchaseType.toString().toLowerCase(Locale.ENGLISH));
                    return;
                }
                selfUnlocked = hyperiumPurchase.hasPurchased(purchaseType);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @InvokeEvent
    public void worldSwitch(WorldChangeEvent changeEvent) {
        UUID id = UUIDUtil.getClientUUID();
        if (id == null) {
            return;
        }
        Boolean aBoolean = purchasedBy.get(id);
        purchasedBy.clear();
        if (aBoolean != null) purchasedBy.put(id, aBoolean);
    }

    @InvokeEvent
    public void purchaseLoadEvent(PurchaseLoadEvent event) {
        purchasedBy.put(event.getUuid(), event.getPurchase().hasPurchased(purchaseType));
    }

    public boolean isPurchasedBy(UUID uuid) {
        if (purchasedBy.containsKey(uuid)) {
            return purchasedBy.get(uuid);
        } else {
            purchasedBy.put(uuid, false);
            Multithreading.runAsync(() -> purchasedBy.put(uuid, PurchaseApi.getInstance().getPackageSync(uuid).hasPurchased(purchaseType)));
            return false;
        }

    }

    public boolean isSelfOnly() {
        return selfOnly;
    }

    public EnumPurchaseType getPurchaseType() {
        return purchaseType;
    }

    public boolean isSelfUnlocked() {
        return selfUnlocked;
    }

    public float interpolate(final float yaw1, final float yaw2, final float percent) {
        float rotation = (yaw1 + (yaw2 - yaw1) * percent) % 360.0f;

        if (rotation < 0.0f) {
            rotation += 360.0f;
        }

        return rotation;
    }

}
