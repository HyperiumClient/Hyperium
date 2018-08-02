package cc.hyperium.addons.customcrosshair.gui.items;

import net.minecraft.client.gui.GuiScreen;

import java.util.ArrayList;
import java.util.List;

public class CCGuiItem {
    private int actionID;
    private int posX;
    private int posY;
    private int width;
    private int height;
    private String displayText;
    private GuiScreen currentScreen;
    protected List<String> helpText;

    public CCGuiItem(final GuiScreen screen) {
        this(screen, -1, "no-name", 0, 0, 10, 10);
    }

    public CCGuiItem(final GuiScreen screen, final int id) {
        this(screen, id, "no-name", 0, 0, 10, 10);
    }

    public CCGuiItem(final GuiScreen screen, final int id, final String displayText, final int posX, final int posY, final int width, final int height) {
        this.actionID = id;
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.displayText = displayText;
        this.setCurrentScreen(screen);
        this.helpText = new ArrayList<String>();
    }

    public void mouseClicked(final int mouseX, final int mouseY) {
    }

    public void mouseReleased(final int mouseX, final int mouseY) {
    }

    public void drawItem(final int mouseX, final int mouseY) {
    }

    public void setCurrentScreen(final GuiScreen screen) {
        this.currentScreen = screen;
    }

    public GuiScreen getCurrentScreen() {
        return this.currentScreen;
    }

    public int getActionID() {
        return this.actionID;
    }

    public void setPosition(final int newPosX, final int newPosY) {
        this.setPosX(newPosX);
        this.setPosY(newPosY);
    }

    public int getPosX() {
        return this.posX;
    }

    public void setPosX(final int newPosX) {
        this.posX = newPosX;
    }

    public int getPosY() {
        return this.posY;
    }

    public void setPosY(final int newPosY) {
        this.posY = newPosY;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(final int newWidth) {
        this.width = newWidth;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(final int newHeight) {
        this.height = newHeight;
    }

    public String getDisplayText() {
        return this.displayText;
    }

    public void setDisplayText(final String newDisplayText) {
        this.displayText = newDisplayText;
    }

    public List<String> getHelpText() {
        return this.helpText;
    }
}
