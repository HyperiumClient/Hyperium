package cc.hyperium.cosmetics;

import cc.hyperium.purchases.EnumPurchaseType;

public class FlipCosmetic extends AbstractCosmetic {
    public FlipCosmetic() {
        super(true, EnumPurchaseType.FLIP_COSMETIC);
    }
}
