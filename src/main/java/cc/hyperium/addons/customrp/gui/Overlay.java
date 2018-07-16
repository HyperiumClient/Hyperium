package cc.hyperium.addons.customrp.gui;

import cc.hyperium.gui.main.HyperiumOverlay;
import cc.hyperium.gui.main.components.OverlayButton;
import cc.hyperium.gui.main.components.OverlaySelector;
import cc.hyperium.addons.customrp.utils.Mode;

public class Overlay extends HyperiumOverlay {
    boolean buttonMode = true;
    String localMode;

    public Overlay() {
        try {
            this.getComponents().add(new OverlayButton("Check for updates", () -> {
                AddonUpdateChecker.updateCheck();
            }));
            this.getComponents().add(new OverlayButton("RESET", () -> {
                Mode.set("addon");
            }));
            this.getComponents().add(new OverlaySelector<String>("Mode: ", Mode.modes.get()[0], a -> {
                localMode = a;
            }, Mode.modes));
            this.getComponents().add(new OverlayButton("Set", () -> {
                Mode.set(localMode);
            }));
            this.getComponents().add(new OverlayButton("Preview", () -> {
                Mode.preview(localMode);
            }));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
