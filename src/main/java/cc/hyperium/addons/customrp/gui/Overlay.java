package cc.hyperium.addons.customrp.gui;

import cc.hyperium.addons.customrp.config.Config;
import cc.hyperium.addons.customrp.utils.Mode;
import cc.hyperium.addons.customrp.utils.RichPresenceUpdater;
import cc.hyperium.gui.main.HyperiumOverlay;
import cc.hyperium.gui.main.components.OverlayButton;
import cc.hyperium.gui.main.components.OverlaySelector;

public class Overlay extends HyperiumOverlay {

    public Overlay() {
        try {
            this.addToggle("Enabled", Config.class.getDeclaredField("ENABLED"), o -> {
                Config.ENABLED = (boolean) o;
                RichPresenceUpdater.callCustomRPUpdate();
            }, true);
            this.getComponents().add(new OverlayButton("RESET", () -> Mode.set("Addon")));
            this.getComponents().add(new OverlaySelector<>("Mode: ", Mode.modes.get()[0], a -> Config.CUSTOM_RP_MODE = a, Mode.modes));
            this.getComponents().add(new OverlayButton("Set", () -> Mode.set(Config.CUSTOM_RP_MODE)));
            this.getComponents().add(new OverlayButton("Preview", () -> Mode.preview(Config.CUSTOM_RP_MODE)));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
