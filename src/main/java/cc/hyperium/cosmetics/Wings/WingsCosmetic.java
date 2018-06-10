package cc.hyperium.cosmetics.Wings;

import cc.hyperium.cosmetics.AbstractCosmetic;
import cc.hyperium.purchases.EnumPurchaseType;

/**
 * @author ConorTheDev
 */
public class WingsCosmetic extends AbstractCosmetic {

    public int scale = 150;

    public WingsCosmetic() {
        super(true, EnumPurchaseType.WING_COSMETIC, true);
    }

    //add custom colours soon
    public float[] getColours() {
        return new float[]{1.0f, 1.0f, 1.0f};
    }
}
