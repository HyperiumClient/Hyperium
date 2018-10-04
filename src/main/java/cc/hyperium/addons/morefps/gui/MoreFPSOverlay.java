package cc.hyperium.addons.morefps.gui;

import cc.hyperium.config.ConfigOpt;
import cc.hyperium.gui.main.HyperiumOverlay;
import cc.hyperium.gui.main.components.OverlayToggle;

public class MoreFPSOverlay extends HyperiumOverlay {

    @ConfigOpt
    public static boolean hideArmorStands;
    @ConfigOpt
    public static boolean hideBalloons;
    @ConfigOpt
    public static boolean hideBanners;
    @ConfigOpt
    public static boolean hideItemFrames;
    @ConfigOpt
    public static boolean hidePlayerNames;
    @ConfigOpt
    public static boolean hideSigns;
    @ConfigOpt
    public static boolean hideNPCs;

    public MoreFPSOverlay() {
        super.getComponents().add(new OverlayToggle("Hide Armor Stands", MoreFPSOverlay.hideArmorStands, b -> MoreFPSOverlay.hideArmorStands = b, true));
        super.getComponents().add(new OverlayToggle("Hide Skywars Balloons", MoreFPSOverlay.hideBalloons, b -> MoreFPSOverlay.hideBalloons = b, true));
        super.getComponents().add(new OverlayToggle("Hide Banners", MoreFPSOverlay.hideBanners, b -> MoreFPSOverlay.hideBanners = b, true));
        super.getComponents().add(new OverlayToggle("Hide Item Frames", MoreFPSOverlay.hideItemFrames, b -> MoreFPSOverlay.hideItemFrames = b, true));
        super.getComponents().add(new OverlayToggle("Hide Player Name Tags", MoreFPSOverlay.hidePlayerNames, b -> MoreFPSOverlay.hidePlayerNames = b, true));
        super.getComponents().add(new OverlayToggle("Hide Signs", MoreFPSOverlay.hideSigns, b -> MoreFPSOverlay.hideSigns = b, true));
        super.getComponents().add(new OverlayToggle("Hide NPC's", MoreFPSOverlay.hideNPCs, b -> MoreFPSOverlay.hideNPCs = b, true));
    }
}
