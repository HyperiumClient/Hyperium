package cc.hyperium.cosmetics;

import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.KillEvent;
import cc.hyperium.event.RenderPlayerEvent;
import cc.hyperium.event.TickEvent;
import cc.hyperium.purchases.AbstractHyperiumPurchase;
import cc.hyperium.purchases.EnumPurchaseType;
import cc.hyperium.purchases.HyperiumPurchase;
import cc.hyperium.purchases.PurchaseApi;
import cc.hyperium.purchases.packages.DabOnKill;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by mitchellkatz on 3/17/18. Designed for production use on Sk1er.club
 */
public class DabOnKIllCosmetic extends AbstractCosmetic {


    private HashMap<UUID, Integer> dabTicks = new HashMap<>();
    private HashMap<UUID, Integer> dabAnimTicks = new HashMap<>();

    public DabOnKIllCosmetic() {
        super(false, EnumPurchaseType.DAB_ON_KILL, true);
    }

    public int getDabTicks(UUID uuid) {
        return dabTicks.getOrDefault(uuid, -1);
    }

    public int getDabAnimTicks(UUID uuid) {
        return dabAnimTicks.getOrDefault(uuid, 0);
    }

    @InvokeEvent
    public void tick(TickEvent event) {
        //idk if will throw concurrent mod excpt.
        for (UUID uuid : dabTicks.keySet()) {
            dabTicks.put(uuid, dabTicks.get(uuid) - 1);
        }
        for (UUID uuid : dabAnimTicks.keySet()) {
            dabAnimTicks.put(uuid, dabAnimTicks.get(uuid) - 1);
        }

    }

    @InvokeEvent
    public void onKill(KillEvent event) {
        UUID uuid = PurchaseApi.getInstance().nameToUUID(event.getUser());
        HyperiumPurchase purchase = PurchaseApi.getInstance().getPackageIfReady(uuid);
        if (purchase != null) {
            AbstractHyperiumPurchase dab = purchase.getPurchase(EnumPurchaseType.DAB_ON_KILL);
            if (dab != null) {
                dabTicks.put(uuid, 20 * ((DabOnKill) dab).getDuration());
                dabAnimTicks.put(uuid, 10);
            }
        }

    }

    @InvokeEvent
    public void renderPlayer(RenderPlayerEvent event) {

    }

}
