package cc.hyperium.addons;

import cc.hyperium.addons.customrp.CustomRP;
import cc.hyperium.event.EventBus;

public class InternalAddons {
    //CustomRP
    EventBus.INSTANCE.register(new CustomRP());


    public InternalAddons() {

    }
}
