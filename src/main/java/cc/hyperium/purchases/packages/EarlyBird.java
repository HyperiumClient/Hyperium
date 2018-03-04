package cc.hyperium.purchases.packages;

import cc.hyperium.purchases.AbstractHyperiumPurchase;
import cc.hyperium.purchases.EnumPurchaseType;
import cc.hyperium.utils.JsonHolder;

public class EarlyBird extends AbstractHyperiumPurchase{
    public EarlyBird(EnumPurchaseType type, JsonHolder data) {
        super(type, data);
    }
}
