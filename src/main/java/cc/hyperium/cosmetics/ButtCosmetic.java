package cc.hyperium.cosmetics;

import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.PreCopyPlayerModelAnglesEvent;
import cc.hyperium.event.Priority;
import cc.hyperium.purchases.EnumPurchaseType;

public class ButtCosmetic extends AbstractCosmetic {
    public ButtCosmetic() {
        super(false, EnumPurchaseType.BUTT, true);
    }

    @InvokeEvent(priority = Priority.HIGH)
    public void preCopy(PreCopyPlayerModelAnglesEvent event) {

        boolean purchasedBy = isPurchasedBy(event.getEntity().getUniqueID());
        event.getModel().getButt().showModel = purchasedBy;
        event.getModel().getButt().offsetY = event.getEntity().isSneaking() ? -.45F : -.4F;
       event.getModel().getButt().offsetZ = event.getEntity().isSneaking() ? .3F : 0F;

    }
}
