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
        this.playerUUID = playerUUID;
        this.response = response;
        for (JsonElement nicePackages : response.optJSONArray("nicePackages")) {
            String asString = nicePackages.getAsString();
            EnumPurchaseType parse = EnumPurchaseType.parse(asString);
            this.purchases.add(PurchaseApi.getInstance().parse(parse, PurchaseApi.getInstance().get(PurchaseApi.url + playerUUID.toString() + "/" + parse.name())));
        }
    }

    public List<AbstractHyperiumPurchase> getPurchases() {
        return purchases;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }
}
