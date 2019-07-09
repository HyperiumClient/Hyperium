package cc.hyperium.purchases.packages;

import cc.hyperium.purchases.AbstractHyperiumPurchase;
import cc.hyperium.purchases.EnumPurchaseType;
import cc.hyperium.utils.JsonHolder;

/**
 * Created by mitchellkatz on 3/17/18. Designed for production use on Sk1er.club
 */
public class EarsCosmetic extends AbstractHyperiumPurchase {
    private final boolean enabled;

    public EarsCosmetic(EnumPurchaseType type, JsonHolder data) {
        super(type, data);
        enabled = getData().optBoolean("enabled");
    }

    public boolean isEnabled() {
        return enabled;
    }
}

