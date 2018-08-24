package cc.hyperium.event;

import net.minecraft.client.gui.GuiScreen;

/**
 * Invoked when a user clicks within a GuiScreen.
 *
 * @param mouseX x position of the mouse on click
 * @param mouseY y position of the mouse on click
 * @param button Mouse button clicked (0 = left, 1 = right, 2 = middle)
 * @param gui GUI that detected the click
 */
public class GuiClickEvent extends CancellableEvent {

    private int mouseX;
    private int mouseY;
    private int button;

    private GuiScreen gui;

    /**
     * Invoked when a user clicks within a GuiScreen.
     *
     * @param mouseX x position of the mouse on click
     * @param mouseY y position of the mouse on click
     * @param button Mouse button clicked (0 = left, 1 = right, 2 = middle)
     * @param gui GUI that detected the click
     */
    public GuiClickEvent(int mouseX, int mouseY, int button, GuiScreen gui) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.button = button;
        this.gui = gui;
    }

    public int getMouseX() {
        return this.mouseX;
    }

    public int getMouseY() {
        return this.mouseY;
    }

    public int getButton() {
        return this.button;
    }

    public GuiScreen getGui() {
        return this.gui;
    }
}
