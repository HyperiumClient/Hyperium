package cc.hyperium.gui.keybinds;

import cc.hyperium.Hyperium;
import cc.hyperium.gui.HyperiumGui;
import cc.hyperium.gui.hyperium.HyperiumMainGui;
import cc.hyperium.handlers.handlers.keybinds.HyperiumBind;
import cc.hyperium.utils.HyperiumFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GuiKeybinds extends HyperiumGui {

    private List<HyperiumBind> binds = new ArrayList<>();
    private List<KeybindEntry> keybindEntries = new ArrayList<>();
    private HyperiumFontRenderer sfr = new HyperiumFontRenderer("Arial", Font.PLAIN, 12);
    private GuiButton resetButton;
    private GuiButton backButton;
    private HyperiumMainGui prevGui;

    private int scrollOffset;
    private int buttonWidth = 70;
    private int buttonHeight = 20;
    private int numColumns = 2;

    private int topGui;
    private int leftGui;
    private int rightGui;
    private int bottomGui;

    private int initialGuiScale;

    public GuiKeybinds() {

        // Change the GUI scale to the intended one.
        Minecraft.getMinecraft().gameSettings.guiScale = 3;
    }

    @Override
    public void initGui() {
        super.initGui();
        detectAllConflicts();
    }

    @Override
    protected void pack() {
        binds.clear();
        keybindEntries.clear();

        scrollOffset = 0;

        // Measurements of the GUI.
        int fixedWidth = 485;
        int fixedHeight = 300;


        // Measurements of the screen on which the GUI was intended to be drawn.
        int intendedWidth = 640;
        int intendedHeight = 360;

        int currentWidth = width;
        int currentHeight = height;

        float widthScaleFactor = (float) currentWidth / intendedWidth;
        float heightScaleFactor = (float) currentHeight / intendedHeight;

        fixedHeight *= heightScaleFactor;
        fixedWidth *= widthScaleFactor;


        if (fixedWidth < 2 * (150 + buttonWidth)) {
            numColumns = 1;
        }

        int difference = width - fixedWidth;
        int calculatedGap = difference / 2;
        leftGui = calculatedGap;
        rightGui = width - calculatedGap;

        difference = height - fixedHeight;
        calculatedGap = difference / 2;
        topGui = calculatedGap;
        bottomGui = height - calculatedGap;

        prevGui = HyperiumMainGui.INSTANCE;

        // Get keybinds.
        binds = new ArrayList<>(Hyperium.INSTANCE.getHandlers().getKeybindHandler().getKeybinds().values());

        for (int i = 0; i < binds.size(); i++) {
            HyperiumBind linkedBind = binds.get(i);
            String label = linkedBind.getKeyDescription();
            KeybindButton btn = new KeybindButton(i, 0, 0, buttonWidth, buttonHeight, "default", linkedBind);
            KeybindEntry keybindEntry = new KeybindEntry(label, btn);
            keybindEntries.add(keybindEntry);
        }

        resetButton = new GuiButton(1337, rightGui - 100, bottomGui - 20, fontRendererObj.getStringWidth("Reset all binds.") + 5, buttonHeight, "Reset all binds.");
        backButton = new GuiButton(1338, rightGui - 150, bottomGui - 20, fontRendererObj.getStringWidth("Back") + 5, buttonHeight, "Back");
    }


    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        drawCenteredString(fontRendererObj, "Hyperium Keybinds", width / 2, topGui - 10, Color.WHITE.getRGB());
        drawRect(leftGui, topGui, rightGui, bottomGui, new Color(30, 30, 30, 100).getRGB());

        // Divide entries into columns.
        List<List<KeybindEntry>> entries = divideList(keybindEntries, numColumns);
        for (int column = 0; column < numColumns; column++) {
            List<KeybindEntry> subEntries = entries.get(column);

            // Render entries for this column.
            int index = 0;
            int startX = leftGui + (column * ((rightGui - leftGui) / numColumns)) + 10;
            int startY = topGui + 10;
            for (KeybindEntry entry : subEntries) {
                int spacing = (bottomGui - topGui) / subEntries.size();
                if (spacing < 30) {
                    spacing = 30;
                }
                int yPosition = startY + (spacing * index) + scrollOffset;
                if (!exceedsBoundaries(yPosition)) {
                    entry.renderBind(startX, yPosition, fontRendererObj, mc, mouseX, mouseY);
                } else {
                    if (entry.isVisible()) {
                        entry.setVisible(false);
                    }
                }
                index++;
            }
        }
        resetButton.drawButton(mc, mouseX, mouseY);
        backButton.drawButton(mc, mouseX, mouseY);
    }

    private boolean exceedsBoundaries(int yPosition) {
        int centre = yPosition + buttonHeight / 2;
        if (centre - (buttonHeight / 2) < topGui) {
            // Button has been scrolled up too far.
            return true;
        }

        // Button has been scrolled down too far.
        return centre + (buttonHeight / 2) > bottomGui;

    }

    private List<List<KeybindEntry>> divideList(List<KeybindEntry> inputList, int number) {
        List<List<KeybindEntry>> partitions = new ArrayList<>(number);
        for (int i = 0; i < number; i++) {
            partitions.add(new ArrayList<>());
        }

        int counter = 0;
        for (KeybindEntry entry : inputList) {
            if (counter >= number) {
                counter = 0;
            }

            partitions.get(counter).add(entry);
            counter++;
        }

        return partitions;
    }

    @Override
    public void show() {
        initialGuiScale = Minecraft.getMinecraft().gameSettings.guiScale;
        Minecraft.getMinecraft().gameSettings.guiScale = 2;
        super.show();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (mouseX > 10 && mouseY < height - 10) {
            if (resetButton.mousePressed(mc, mouseX, mouseY)) {
                resetAll();
                return;
            } else if (backButton.mousePressed(mc, mouseX, mouseY)) {
                openPreviousGui();
                return;
            }

            for (int i = 0; i < keybindEntries.size(); i++) {
                KeybindEntry entry = keybindEntries.get(i);
                if (!entry.isVisible()) {
                    continue;
                }
                KeybindButton button = entry.getKeybindButton();

                // Check if button is pressed.
                if (button.mousePressedDyanmic(this.mc, mouseX, mouseY)) {
                    // Make sure no other buttons are listening.
                    int bIndex = keybindEntries.indexOf(entry);
                    keybindEntries.remove(entry);

                    // Checks every other entry.
                    for (KeybindEntry keybindEntry : keybindEntries) {
                        KeybindButton entryButton = keybindEntry.getKeybindButton();
                        entryButton.setListening(false);
                    }

                    // Reinsert original entry back into list.
                    keybindEntries.add(bIndex, entry);
                    button.mouseButtonClicked(mouseButton);
                }
            }
        }
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int i = Mouse.getEventDWheel();
        if (i < 0) {
            // works out length of scrollable area
            int size = keybindEntries.size() / numColumns;
            int length = 10 - (int) (size * sfr.getHeight("s"));

            if (scrollOffset - length + 1 > -size && length <= size) {
                // regions it cant exceed
                scrollOffset -= 25;
            }
        } else if (i > 0 && scrollOffset < 0) {
            scrollOffset += 25;
        }
    }


    public void resetAll() {
        for (KeybindEntry entry : keybindEntries) {
            KeybindButton keybindButton = entry.getKeybindButton();
            keybindButton.resetBind();
        }
    }

    private void openPreviousGui() {
        prevGui.show();
    }

    @Override
    public void onGuiClosed() {
        // Reset back to the user's normal GUI scale.
        Minecraft.getMinecraft().gameSettings.guiScale = initialGuiScale;
        Hyperium.CONFIG.save();

        super.onGuiClosed();
        openPreviousGui();
    }

    public void detectAllConflicts() {
        for (KeybindEntry entry : keybindEntries) {
            KeybindButton keybindButton = entry.getKeybindButton();
            keybindButton.detectConflicts();
        }
    }

    private boolean areKeysListening() {
        for (KeybindEntry entry : keybindEntries) {
            KeybindButton btn = entry.getKeybindButton();
            if (btn.isListening()) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == Keyboard.KEY_ESCAPE) {
            if (areKeysListening()) {
                return;
            }
        }
        super.keyTyped(typedChar, keyCode);
    }
}
