package cc.hyperium.addons;

import cc.hyperium.addons.customcrosshair.main.CustomCrosshairAddon;
import cc.hyperium.addons.customrp.CustomRP;
import cc.hyperium.event.EventBus;
import cc.hyperium.mods.glintcolorizer.GlintColorizer;

public class InternalAddons {
    private final CustomRP customrp;
    private final CustomCrosshairAddon customCrosshairAddon;

    public InternalAddons() {
        this.customrp = ((CustomRP) new CustomRP().init());
        this.customCrosshairAddon = ((CustomCrosshairAddon) new CustomCrosshairAddon().init());
    }

    public CustomRP getCustomRP() {
        return customrp;
    }

    public CustomCrosshairAddon getCustomCrosshairAddon() {
        return customCrosshairAddon;
    }
}
