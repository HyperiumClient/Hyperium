package cc.hyperium.cosmetics;

import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.PreCopyPlayerModelAnglesEvent;
import cc.hyperium.event.Priority;
import cc.hyperium.mixinsimp.renderer.model.IMixinModelPlayer;
import cc.hyperium.purchases.EnumPurchaseType;
import cc.hyperium.purchases.HyperiumPurchase;
import cc.hyperium.purchases.PurchaseApi;

public class ButtCosmetic extends AbstractCosmetic {
    public ButtCosmetic() {
        super(false, EnumPurchaseType.BUTT);
    }

    @InvokeEvent(priority = Priority.HIGH)
    public void preCopy(PreCopyPlayerModelAnglesEvent event) {

        boolean b = event.getModel() instanceof IMixinModelPlayer;
        if (!b)
            return;
        boolean purchasedBy = isPurchasedBy(event.getEntity().getUniqueID());
        if (!purchasedBy) {
            ((IMixinModelPlayer) event.getModel()).getButt().showModel = false;
            return;
        }
        HyperiumPurchase packageIfReady = PurchaseApi.getInstance().getPackageIfReady(event.getEntity().getUniqueID());
        if (packageIfReady == null) {
            return;
        }
        boolean disabled = packageIfReady.getCachedSettings().isButtDisabled();
        ((IMixinModelPlayer) event.getModel()).getButt().showModel = !disabled;
        ((IMixinModelPlayer) event.getModel()).getButt().offsetY = event.getEntity().isSneaking() ? -.45F : -.4F;
        ((IMixinModelPlayer) event.getModel()).getButt().offsetZ = event.getEntity().isSneaking() ? .3F : 0F;

    }
}
