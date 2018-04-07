package cc.hyperium.cosmetics;

import cc.hyperium.Hyperium;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.KillEvent;
import cc.hyperium.event.RenderPlayerEvent;
import cc.hyperium.purchases.AbstractHyperiumPurchase;
import cc.hyperium.purchases.EnumPurchaseType;
import cc.hyperium.purchases.HyperiumPurchase;
import cc.hyperium.purchases.PurchaseApi;
import cc.hyperium.purchases.packages.DabOnKill;

import java.util.UUID;

/**
 * Created by mitchellkatz on 3/17/18. Designed for production use on Sk1er.club
 */
public class DabOnKIllCosmetic extends AbstractCosmetic {


    public DabOnKIllCosmetic() {
        super(false, EnumPurchaseType.DAB_ON_KILL, true);
    }



    @InvokeEvent
    public void onKill(KillEvent event) {
        UUID uuid = PurchaseApi.getInstance().nameToUUID(event.getUser());
        HyperiumPurchase purchase = PurchaseApi.getInstance().getPackageIfReady(uuid);
        if (purchase != null) {
            AbstractHyperiumPurchase dab = purchase.getPurchase(EnumPurchaseType.DAB_ON_KILL);
            if (dab != null) {
                Hyperium.INSTANCE.getHandlers().getDabHandler().get(uuid).ensureDabbingFor(((DabOnKill) dab).getDuration());
            }
        }

    }

    @InvokeEvent
    public void renderPlayer(RenderPlayerEvent event) {

    }

}
