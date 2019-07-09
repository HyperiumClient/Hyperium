package cc.hyperium.purchases.packages;

import cc.hyperium.purchases.AbstractHyperiumPurchase;
import cc.hyperium.purchases.EnumPurchaseType;
import cc.hyperium.utils.JsonHolder;

public class DragonHeadCosmetic extends AbstractHyperiumPurchase {

    public DragonHeadCosmetic(EnumPurchaseType type, JsonHolder data) {
        super(EnumPurchaseType.DRAGON_HEAD, data);
    }
}
