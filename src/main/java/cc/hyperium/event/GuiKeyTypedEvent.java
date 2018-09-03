package cc.hyperium.event;

import net.minecraft.client.gui.GuiScreen;

public class GuiKeyTypedEvent {

    private GuiScreen screen;
    private char typedChar;
    private int keyCode;

    public GuiKeyTypedEvent(GuiScreen screen, char typedChar, int keyCode) {
        this.screen = screen;
        this.typedChar = typedChar;
        this.keyCode = keyCode;
    }

    public GuiScreen getScreen() {
        return screen;
    }

    public char getTypedChar() {
        return typedChar;
    }

    public int getKeyCode() {
        return keyCode;
    }
}
