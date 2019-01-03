package cc.hyperium.addons;

import cc.hyperium.addons.bossbar.BossbarAddon;
import cc.hyperium.addons.customcrosshair.CustomCrosshairAddon;
import cc.hyperium.addons.sidebar.SidebarAddon;

public class InternalAddons {

    private final CustomCrosshairAddon customCrosshairAddon;
    private final SidebarAddon sidebarAddon;
    private final BossbarAddon bossbarAddon;

    public InternalAddons() {
        this.customCrosshairAddon = ((CustomCrosshairAddon) new CustomCrosshairAddon().init());
        this.sidebarAddon = ((SidebarAddon) new SidebarAddon().init());
        this.bossbarAddon = ((BossbarAddon) new BossbarAddon().init());
    }

    public CustomCrosshairAddon getCustomCrosshairAddon() {
        return customCrosshairAddon;
    }

    public SidebarAddon getSidebarAddon() {
        return sidebarAddon;
    }

    public BossbarAddon getBossbarAddon() {
        return bossbarAddon;
    }
}
