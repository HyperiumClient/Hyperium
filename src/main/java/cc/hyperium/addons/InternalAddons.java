package cc.hyperium.addons;

import cc.hyperium.addons.autodab.AutoDab;
import cc.hyperium.addons.autonico.AutoNico;
import cc.hyperium.addons.customcrosshair.CustomCrosshairAddon;
import cc.hyperium.addons.customrp.CustomRP;
import cc.hyperium.addons.sidebar.SidebarAddon;

public class InternalAddons {
    private final CustomRP customrp;
    private final CustomCrosshairAddon customCrosshairAddon;
    private final SidebarAddon sidebarAddon;
    private final AutoDab autoDab;
    private final AutoNico autoNico;

    public InternalAddons() {
        this.customrp = ((CustomRP) new CustomRP().init());
        this.customCrosshairAddon = ((CustomCrosshairAddon) new CustomCrosshairAddon().init());
        this.sidebarAddon = ((SidebarAddon) new SidebarAddon().init());
        this.autoDab = ((AutoDab) new AutoDab().init());
        this.autoNico = ((AutoNico) new AutoNico().init());
    }

    public CustomRP getCustomRP() {
        return customrp;
    }

    public CustomCrosshairAddon getCustomCrosshairAddon() {
        return customCrosshairAddon;
    }

    public SidebarAddon getSidebarAddon() {
        return sidebarAddon;
    }

    public AutoDab getAutoDab() {
        return autoDab;
    }

    public AutoNico getAutoNico() {
        return autoNico;
    }
}
