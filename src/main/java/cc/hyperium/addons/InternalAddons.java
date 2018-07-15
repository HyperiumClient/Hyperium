package cc.hyperium.addons;

import cc.hyperium.addons.customrp.CustomRP;
import cc.hyperium.event.EventBus;

public class InternalAddons {
    //CustomRP
    private CustomRP customrp;
    EventBus.INSTANCE.register((customrp = new CustomRP()));


    public InternalAddons() {

    }
}
