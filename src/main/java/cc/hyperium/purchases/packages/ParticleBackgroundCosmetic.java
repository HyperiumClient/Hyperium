package cc.hyperium.purchases.packages;

import cc.hyperium.purchases.AbstractHyperiumPurchase;
import cc.hyperium.purchases.EnumPurchaseType;
import cc.hyperium.utils.JsonHolder;

/**
 * Created by mitchellkatz on 3/20/18. Designed for production use on Sk1er.club
 */
public class ParticleBackgroundCosmetic extends AbstractHyperiumPurchase {

    public ParticleBackgroundCosmetic(EnumPurchaseType type, JsonHolder data) {
        super(type, data);
    }
}
