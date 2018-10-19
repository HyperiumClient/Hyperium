package cc.hyperium.cosmetics;

import cc.hyperium.Hyperium;
import cc.hyperium.config.Settings;
import cc.hyperium.purchases.EnumPurchaseType;

public class CosmeticsUtil {

    public static boolean shouldShow(EnumPurchaseType type) {
        if (!Settings.SHOW_COSMETICS_EVERYWHERE) {
            if (Hyperium.INSTANCE.getHandlers().getLocationHandler().isLobbyOrHousing())
                return false;
        }
        return true;
    }
}
