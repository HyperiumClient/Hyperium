package cc.hyperium.purchases;

import cc.hyperium.utils.JsonHolder;

public abstract class AbstractHyperiumPurchase {
    private EnumPurchaseType type;
    private JsonHolder data;

    public AbstractHyperiumPurchase(EnumPurchaseType type, JsonHolder data) {
        this.type = type;
        this.data = data;
    }

    public EnumPurchaseType getType() {
        return type;
    }

    public JsonHolder getData() {
        return data;
    }
}
