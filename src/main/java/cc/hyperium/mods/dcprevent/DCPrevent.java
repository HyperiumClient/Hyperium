package cc.hyperium.mods.dcprevent;

import cc.hyperium.config.Settings;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.MouseButtonEvent;
import cc.hyperium.event.TickEvent;
import cc.hyperium.mods.AbstractMod;

public class DCPrevent extends AbstractMod {

    private boolean hasClickedThisTick;

    @Override
    public AbstractMod init() {
        hasClickedThisTick = false;
        EventBus.INSTANCE.register(this);
        return this;
    }

    @Override
    public Metadata getModMetadata() {
        return new Metadata(this, "Double Click Prevent", "1.0", "asbyth");
    }

    @InvokeEvent
    public void mouseClick(MouseButtonEvent event) {
        if (event.getState() && hasClickedThisTick && Settings.PREVENT_DOUBLECLICK) {
            event.setCancelled(true);
            return;
        }

        if (event.getState()) {
            hasClickedThisTick = true;
        }
    }

    @InvokeEvent
    public void tickEvent(TickEvent event) {
        hasClickedThisTick = false;
    }
}
