package cc.hyperium.addons;

import cc.hyperium.addons.customrp.CustomRP;
import cc.hyperium.event.EventBus;
import cc.hyperium.mods.glintcolorizer.GlintColorizer;

public class InternalAddons {
    //CustomRP
    private final CustomRP customrp;

    public InternalAddons() {
        //CustomRP
        this.customrp = ((CustomRP) new CustomRP().init());
    }

    //CustomRP
    public CustomRP getCustomRP() {
        return customrp;
    }
}
