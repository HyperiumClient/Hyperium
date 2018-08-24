package cc.hyperium.event;

import net.minecraft.client.gui.GuiScreen;

/**
 * Invoked when a gui screen is opened
 */
public class GuiOpenEvent extends CancellableEvent {

    private GuiScreen gui;

    /**
     * @param gui the gui that is being opened
     */
    public GuiOpenEvent(GuiScreen gui) {
        this.gui = gui;
    }

    public GuiScreen getGui() {
        return this.gui;
    }

    public void setGui(GuiScreen gui) {
        this.gui = gui;
    }
}
