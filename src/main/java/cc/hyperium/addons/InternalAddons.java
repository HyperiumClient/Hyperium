package cc.hyperium.addons;

import cc.hyperium.addons.bossbar.BossbarAddon;
import cc.hyperium.addons.customcrosshair.CustomCrosshairAddon;
import cc.hyperium.addons.morefps.MoreFPSAddon;
import cc.hyperium.addons.pingdisplay.PingDisplayAddon;
import cc.hyperium.addons.sidebar.SidebarAddon;

public class InternalAddons {

    private final CustomCrosshairAddon customCrosshairAddon;
    private final SidebarAddon sidebarAddon;
    private final BossbarAddon bossbarAddon;
    private final MoreFPSAddon moreFPSAddon;
    private final PingDisplayAddon pingDisplayAddon;

    public InternalAddons() {
        this.customCrosshairAddon = ((CustomCrosshairAddon) new CustomCrosshairAddon().init());
        this.sidebarAddon = ((SidebarAddon) new SidebarAddon().init());
        this.bossbarAddon = ((BossbarAddon) new BossbarAddon().init());
        this.moreFPSAddon = ((MoreFPSAddon) new MoreFPSAddon().init());
        this.pingDisplayAddon = ((PingDisplayAddon) new PingDisplayAddon().init());
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

    public MoreFPSAddon getMoreFPSAddon() {
        return moreFPSAddon;
    }

    public PingDisplayAddon getPingDisplayAddon() {
        return pingDisplayAddon;
    }
}
