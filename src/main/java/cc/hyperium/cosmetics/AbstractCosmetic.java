package cc.hyperium.cosmetics;

import cc.hyperium.purchases.EnumPurchaseType;

/**
 * Created by mitchellkatz on 3/17/18. Designed for production use on Sk1er.club
 */
public abstract class AbstractCosmetic {
    private boolean selfOnly;
    private EnumPurchaseType purchaseType;
    private boolean purchased;
}
