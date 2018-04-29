package cc.hyperium.cosmetics;

import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.purchases.EnumPurchaseType;
import cc.hyperium.purchases.PurchaseApi;
import net.minecraft.client.Minecraft;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by mitchellkatz on 3/17/18. Designed for production use on Sk1er.club
 */
public abstract class AbstractCosmetic {
    private final boolean selfOnly;
    private final EnumPurchaseType purchaseType;
    private final boolean purchasable;
    private boolean selfUnlocked;
    private final Map<UUID, Boolean> purchasedBy = new ConcurrentHashMap<>();

    AbstractCosmetic(boolean selfOnly, EnumPurchaseType purchaseType, boolean purchaseable) {
        this.selfOnly = selfOnly;
        this.purchaseType = purchaseType;
        this.purchasable = purchaseable;
        if (purchaseable) {
            PurchaseApi.getInstance().getPackageAsync(Minecraft.getMinecraft().getSession().getProfile().getId(), hyperiumPurchase -> {
                if (hyperiumPurchase == null) {
                    System.out.println("WARNING COSMETIC NULL");
                    return;
                }
                selfUnlocked = hyperiumPurchase.hasPurchased(purchaseType);
            });
        }
    }

    public boolean isPurchasedBy(UUID uuid) {
        if (!isPurchasable()) {
            return true;
        }
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

    public boolean isPurchasable() {
        return purchasable;
    }

    public boolean isSelfUnlocked() {
        return selfUnlocked;
    }
}
