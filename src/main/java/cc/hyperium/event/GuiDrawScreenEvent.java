package cc.hyperium.event;

import net.minecraft.client.gui.GuiScreen;

public class GuiDrawScreenEvent {
    private GuiScreen screen;
    private int mouseX;
    private int mouseY;
    private float partialTicks;

    public GuiDrawScreenEvent(GuiScreen screen, int mouseX, int mouseY, float partialTicks) {

        this.screen = screen;
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.partialTicks = partialTicks;
    }

    public GuiScreen getScreen() {
        return screen;
    }

    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }

    public float getPartialTicks() {
        return partialTicks;
    }
}
