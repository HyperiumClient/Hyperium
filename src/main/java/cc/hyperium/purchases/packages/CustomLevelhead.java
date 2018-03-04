package cc.hyperium.purchases.packages;

import cc.hyperium.purchases.AbstractHyperiumPurchase;
import cc.hyperium.purchases.EnumPurchaseType;
import cc.hyperium.utils.JsonHolder;

public class CustomLevelhead extends AbstractHyperiumPurchase {
    public CustomLevelhead(EnumPurchaseType type, JsonHolder data) {
        super(type, data);
    }
}
