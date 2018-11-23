package cc.hyperium.addons.autodab.overlay;

import cc.hyperium.config.Settings;
import cc.hyperium.gui.main.HyperiumOverlay;
import cc.hyperium.gui.main.components.OverlaySlider;
import cc.hyperium.gui.main.components.OverlayToggle;

/**
 * Overlay for the AutoDab addon, in the overlay menus.
 */
public class AutoDabOverlay extends HyperiumOverlay {

    public AutoDabOverlay() {
        getComponents().add(new OverlayToggle("Enabled", Settings.AUTO_DAB_ENABLED, (b) -> Settings.AUTO_DAB_ENABLED = b, true));
        getComponents().add(new OverlayToggle("Auto third person", Settings.AUTO_DAB_THIRD_PERSON, (b) -> Settings.AUTO_DAB_ENABLED = b, true));
        getComponents().add(new OverlaySlider("Dab Length", 1.0f, 10.0f, Settings.AUTO_DAB_LENGTH, (a) -> Settings.AUTO_DAB_LENGTH = Math.round(a), true));
    }
}
