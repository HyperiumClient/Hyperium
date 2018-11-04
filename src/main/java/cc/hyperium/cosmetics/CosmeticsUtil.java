package cc.hyperium.cosmetics;

import cc.hyperium.Hyperium;
import cc.hyperium.config.Settings;
import cc.hyperium.purchases.EnumPurchaseType;

public class CosmeticsUtil {

    public static boolean shouldHide(EnumPurchaseType type) {
        if (Settings.SHOW_COSMETICS_EVERYWHERE)
            return false;
        return !Hyperium.INSTANCE.getHandlers().getLocationHandler().isLobbyOrHousing();
    }
}
