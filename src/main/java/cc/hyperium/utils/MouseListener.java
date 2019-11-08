package cc.hyperium.utils;

import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.gui.GuiOpenEvent;
import net.minecraft.client.gui.inventory.GuiChest;

public class MouseListener {

    private Class lastOpenedInventory = null;
    private long lastClosedInv = -1;

    @InvokeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        if (event.getGui() == null && GuiChest.class.equals(lastOpenedInventory)) {
            lastClosedInv = System.currentTimeMillis();
            lastOpenedInventory = null;
        }

        if (event.getGui() != null) {
            lastOpenedInventory = event.getGui().getClass();
        }
    }

    public boolean shouldResetMouse() {
        return System.currentTimeMillis() - lastClosedInv > 100;
    }
}
