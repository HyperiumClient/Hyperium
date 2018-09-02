package cc.hyperium.event;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class ActionPerformedEvent extends CancellableEvent {

    private GuiScreen screen;
    private GuiButton button;

    public ActionPerformedEvent(GuiScreen screen, GuiButton button) {

        this.screen = screen;
        this.button = button;
    }

    public GuiScreen getScreen() {
        return screen;
    }

    public GuiButton getButton() {
        return button;
    }
}
