package cc.hyperium.addons.morefps.gui;

import cc.hyperium.config.ConfigOpt;
import cc.hyperium.gui.main.HyperiumOverlay;
import cc.hyperium.gui.main.components.OverlayToggle;

public class Overlay extends HyperiumOverlay {

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

    public Overlay() {
        super.getComponents().add(new OverlayToggle("Hide Armor Stands", Overlay.hideArmorStands, b -> Overlay.hideArmorStands = b, true));
        super.getComponents().add(new OverlayToggle("Hide Skywars Balloons", Overlay.hideBalloons, b -> Overlay.hideBalloons = b, true));
        super.getComponents().add(new OverlayToggle("Hide Banners", Overlay.hideBanners, b -> Overlay.hideBanners = b, true));
        super.getComponents().add(new OverlayToggle("Hide Item Frames", Overlay.hideItemFrames, b -> Overlay.hideItemFrames = b, true));
        super.getComponents().add(new OverlayToggle("Hide Player Name Tags", Overlay.hidePlayerNames, b -> Overlay.hidePlayerNames = b, true));
        super.getComponents().add(new OverlayToggle("Hide Signs", Overlay.hideSigns, b -> Overlay.hideSigns = b, true));
        super.getComponents().add(new OverlayToggle("Hide NPC's", Overlay.hideNPCs, b -> Overlay.hideNPCs = b, true));
    }
}
