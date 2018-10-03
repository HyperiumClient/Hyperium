package cc.hyperium.event;

import net.minecraft.client.gui.GuiScreen;

public class InitGuiEvent {
    private GuiScreen screen;

    public InitGuiEvent(GuiScreen screen) {
        this.screen = screen;
    }

    public GuiScreen getScreen() {
        return screen;
    }
}
