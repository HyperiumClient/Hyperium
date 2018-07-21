package cc.hyperium.addons;

import cc.hyperium.addons.customcrosshair.main.CustomCrosshairAddon;
import cc.hyperium.addons.sidebar.SidebarAddon;

public class InternalAddons {
    private final CustomCrosshairAddon customCrosshairAddon;
    private final SidebarAddon sidebarAddon;

    public InternalAddons() {
        this.customCrosshairAddon = ((CustomCrosshairAddon) new CustomCrosshairAddon().init());
        this.sidebarAddon = ((SidebarAddon) new SidebarAddon().init());
    }

    public CustomCrosshairAddon getCustomCrosshairAddon() {
        return customCrosshairAddon;
    }

    public SidebarAddon getSidebarAddon() {
        return sidebarAddon;
    }
}
