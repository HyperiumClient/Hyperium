package cc.hyperium.addons.pingdisplay.overlay;

import cc.hyperium.Hyperium;
import cc.hyperium.addons.pingdisplay.gui.PingDisplayGui;
import cc.hyperium.event.EventBus;
import cc.hyperium.gui.main.HyperiumOverlay;
import cc.hyperium.gui.main.components.OverlayLabel;
import net.minecraft.client.Minecraft;

public class PingDisplayOverlay extends HyperiumOverlay {

    public PingDisplayOverlay() {
        this.getComponents().add(new OverlayLabel("Open Ping Display Gui", true, () -> {
            if (Minecraft.getMinecraft().thePlayer != null) {
                EventBus.INSTANCE.register(this);
            }
            Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new PingDisplayGui());
            EventBus.INSTANCE.unregister(this);
        }));
    }
}
