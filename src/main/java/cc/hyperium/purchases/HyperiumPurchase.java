/*
 *     Copyright (C) 2018  Hyperium <https://hyperium.cc/>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.purchases;

import cc.hyperium.utils.JsonHolder;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HyperiumPurchase {
    private UUID playerUUID;
    private List<AbstractHyperiumPurchase> purchases = new ArrayList<>();
    private JsonHolder response;

    public HyperiumPurchase(UUID playerUUID, JsonHolder response) {
        System.out.println("Loaded purchases for " + playerUUID + " (" + response + ")");
        this.playerUUID = playerUUID;
        this.response = response;
        for (JsonElement nicePackages : response.optJSONArray("hyperium")) {
            String asString = nicePackages.getAsString();
            EnumPurchaseType parse = EnumPurchaseType.parse(asString);
            if (parse != EnumPurchaseType.UNKNOWN)
                try {
                    this.purchases.add(PurchaseApi.getInstance().parse(parse, PurchaseApi.getInstance().get(PurchaseApi.url + playerUUID.toString() + "/" + parse.name())));
                } catch (Exception wtf) {
                wtf.printStackTrace();
                }
        }
    }

    public boolean hasPurchased(EnumPurchaseType type) {
        return getPurchase(type) != null;
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

    public AbstractHyperiumPurchase getPurchase(EnumPurchaseType type) {
        for (AbstractHyperiumPurchase purchase : purchases) {
            if (purchase.getType() == type) {
                return purchase;
            }
        }
        return null;
    }
}
