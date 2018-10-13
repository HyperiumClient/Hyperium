package cc.hyperium.cosmetics;

import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.RenderPlayerEvent;
import cc.hyperium.purchases.EnumPurchaseType;

/**
 * Created by mitchellkatz on 3/17/18. Designed for production use on Sk1er.club
 */
class KillCounterMuscles extends AbstractCosmetic {
    public KillCounterMuscles() {
        super(false, EnumPurchaseType.KILL_TRACKER_MUSCLE);
    }

    @InvokeEvent
    public void renderPlayer(RenderPlayerEvent event) {

    }

}
