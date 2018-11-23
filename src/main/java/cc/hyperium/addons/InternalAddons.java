package cc.hyperium.addons;

import cc.hyperium.addons.bossbar.BossbarAddon;
import cc.hyperium.addons.customcrosshair.CustomCrosshairAddon;
import cc.hyperium.addons.sidebar.SidebarAddon;

/**
 * Class which contains instances of built-in addons
 */
public class InternalAddons {

    /**
     * The custom crosshair addon instance
     */
    private final CustomCrosshairAddon customCrosshairAddon;

    /**
     * The sidebar addon instance
     */
    private final SidebarAddon sidebarAddon;

    /**
     * The bossbar addon instance
     */
    private final BossbarAddon bossbarAddon;

    public InternalAddons() {
        this.customCrosshairAddon = ((CustomCrosshairAddon) new CustomCrosshairAddon().init());
        this.sidebarAddon = ((SidebarAddon) new SidebarAddon().init());
        this.bossbarAddon = ((BossbarAddon) new BossbarAddon().init());
    }

    /**
     * Returns the instance of the custom crosshair addon
     *
     * @return The custom crosshair addon instance
     */
    public CustomCrosshairAddon getCustomCrosshairAddon() {
        return customCrosshairAddon;
    }

    /**
     * Returns the instance of the sidebar addon
     *
     * @return The sidebar addon instance
     */
    public SidebarAddon getSidebarAddon() {
        return sidebarAddon;
    }

    /**
     * Returns the instance of the bossbar addon
     *
     * @return The bossbar addon instance
     */
    public BossbarAddon getBossbarAddon() {
        return bossbarAddon;
    }
}
