/*
 *       Copyright (C) 2018-present Hyperium <https://hyperium.cc/>
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU Lesser General Public License as published
 *       by the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU Lesser General Public License for more details.
 *
 *       You should have received a copy of the GNU Lesser General Public License
 *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.purchases;

import cc.hyperium.utils.JsonHolder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import java.util.*;

public class HyperiumPurchase {
    private final UUID playerUUID;
    private final List<AbstractHyperiumPurchase> purchases = new ArrayList<>();
    private final JsonHolder response;
    private JsonHolder purchaseSettings = new JsonHolder();
    private boolean everything;
    private PurchaseSettings cachedSettings = new PurchaseSettings(new JsonHolder());

    public HyperiumPurchase(UUID playerUUID, JsonHolder response) {
        this.playerUUID = playerUUID;
        this.response = response;
        if (response.optBoolean("non_player"))
            return;
        everything = (response.optLong("everything") > System.currentTimeMillis());

        purchaseSettings = PurchaseApi.getInstance().get("https://api.hyperium.cc/purchaseSettings/" + (playerUUID.toString()));
        cachedSettings = new PurchaseSettings(purchaseSettings);
        for (JsonElement nicePackages : response.optJSONArray("hyperium")) {
            String asString = nicePackages.getAsString();
            EnumPurchaseType parse = EnumPurchaseType.parse(asString);
            if (parse != EnumPurchaseType.UNKNOWN)
                try {
                    AbstractHyperiumPurchase parse1 = PurchaseApi.getInstance().parse(parse, purchaseSettings
                        .optJSONObject(parse.name().toLowerCase()));
                    if (parse1 != null)
                        purchases.add(parse1);
                } catch (Exception wtf) {
//                    wtf.printStackTrace();
                }
        }
        if (everything) {
            Arrays.stream(EnumPurchaseType.values()).filter(enumPurchaseType -> getPurchase(enumPurchaseType) == null &&
                enumPurchaseType != EnumPurchaseType.UNKNOWN).map(enumPurchaseType -> PurchaseApi.getInstance().parse(enumPurchaseType,
                purchaseSettings.optJSONObject(enumPurchaseType.name().toLowerCase()))).filter(Objects::nonNull).forEach(purchases::add);
        }
    }

    public void refreshCachedSettings() {
        cachedSettings = new PurchaseSettings(purchaseSettings);
    }

    public PurchaseSettings getCachedSettings() {
        return cachedSettings;
    }

    public boolean isEverything() {
        return everything;
    }

    public JsonHolder getPurchaseSettings() {
        return purchaseSettings;
    }

    public boolean hasPurchased(EnumPurchaseType type) {
        if (type == EnumPurchaseType.UNKNOWN)
            return false;
        if (everything)
            return true;
        return getPurchase(type) != null;
    }

    public boolean hasPurchased(String key) {
        if (everything) return true;
        for (JsonElement element : response.optJSONArray("hyperium")) {
            if (element instanceof JsonPrimitive && element.getAsString().equalsIgnoreCase(key)) {
                return true;
            }
        }

        return false;
    }

    public List<AbstractHyperiumPurchase> getPurchases() {
        return purchases;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public JsonHolder getResponse() {
        return response;
    }

    @Override
    public String toString() {
        return "HyperiumPurchase{" +
            "playerUUID=" + playerUUID +
            ", response=" + response +
            '}';
    }

    public AbstractHyperiumPurchase getPurchase(EnumPurchaseType type) {
        return purchases.stream().filter(purchase -> purchase.getType() == type).findFirst().orElse(null);
    }
}
