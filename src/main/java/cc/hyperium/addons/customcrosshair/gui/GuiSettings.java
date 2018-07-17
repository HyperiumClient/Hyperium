package cc.hyperium.addons.customcrosshair.gui;

import cc.hyperium.addons.customcrosshair.gui.items.Button;
import cc.hyperium.addons.customcrosshair.gui.items.GuiItem;
import cc.hyperium.addons.customcrosshair.gui.items.HelpButton;
import cc.hyperium.addons.customcrosshair.main.CrosshairVersion;
import cc.hyperium.addons.customcrosshair.main.CustomCrosshairAddon;
import cc.hyperium.addons.customcrosshair.utils.GuiGraphics;
import cc.hyperium.addons.customcrosshair.utils.GuiTheme;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class GuiSettings extends ModGuiScreen {
    private Button button_editKeyBind;
    private boolean editingKey;
    private CrosshairVersion requestedLatestVersion;

    public void initGui() {
        this.editingKey = false;
        this.itemList.clear();
        this.itemList.add(this.button_editKeyBind = new Button(this, 1, "Open GUI key: " + CustomCrosshairAddon.getCrosshairMod().getGuiKeyBind(), 0, 0, 110, 25) {
            @Override
            public void mouseClicked(final int mouseX, final int mouseY) {
                GuiSettings.this.editingKey = true;
                GuiSettings.this.button_editKeyBind.setDisplayText("Press a button...");
            }
        });
        this.button_editKeyBind.getHelpText().add("Change the key to open the customize crosshair menu.");
        int y = 32;
        for (int i = 0; i < this.itemList.size(); ++i) {
            if (i > 0) {
                y += this.itemList.get(i - 1).getHeight() + 6;
            }
            this.itemList.get(i).setPosition(21, y);
        }
        y = 32;
        for (int size = this.itemList.size(), j = 0; j < size; ++j) {
            this.itemList.add(new HelpButton(this, this.itemList.get(j).getHelpText()));
            if (j > 0) {
                y += this.itemList.get(j - 1).getHeight() + 6;
            }
            this.itemList.get(size + j).setPosition(5, y);
        }
        final int[] screenSize = GuiGraphics.getScreenSize();
        this.itemList.add(new Button(this, 0, "<- Return", screenSize[0] - 56, 0, 55, 25) {
            @Override
            public void mouseClicked(final int mouseX, final int mouseY) {
                GuiSettings.this.mc.displayGuiScreen(new GuiEditCrosshair());
            }
        });
        this.requestedLatestVersion = CustomCrosshairAddon.getCrosshairMod().sendLatestVersionGetRequest();
    }

    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        final int[] screenSize = GuiGraphics.getScreenSize();
        this.drawDefaultBackground();
        final String titleText = "Custom Crosshair Addon" + " v" + CustomCrosshairAddon.VERSION;
        GuiGraphics.drawBorderedRectangle(0, 0, screenSize[0] - 1, 25, GuiTheme.PRIMARY, GuiTheme.SECONDARY);
        GuiGraphics.drawStringWithShadow(titleText, 5, 10, 16777215);
        if (this.requestedLatestVersion != null && !this.requestedLatestVersion.version.equals(CustomCrosshairAddon.VERSION)) {
            final String message = "Detected new latest version: v" + this.requestedLatestVersion.version + ".";
            GuiGraphics.drawStringWithShadow(message, screenSize[0] - GuiGraphics.getStringWidth(message) - 5, screenSize[1] - 41, 16757760);
        }
        String message = "Custom Crosshair Addon" + " v" + CustomCrosshairAddon.VERSION;
        GuiGraphics.drawStringWithShadow(message, screenSize[0] - GuiGraphics.getStringWidth(message) - 5, screenSize[1] - 28, 16777215);
        message = "Ported by Amplifiable";
        GuiGraphics.drawStringWithShadow(message, screenSize[0] - GuiGraphics.getStringWidth(message) - 5, screenSize[1] - 15, 16777215);
        this.toolTip = null;
        for (final GuiItem item : this.itemList) {
            item.drawItem(mouseX, mouseY);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        for (final GuiItem item : this.itemList) {
            if (mouseX >= item.getPosX() && mouseX <= item.getPosX() + item.getWidth() && mouseY >= item.getPosY() && mouseY <= item.getPosY() + item.getHeight()) {
                item.mouseClicked(mouseX, mouseY);
            }
        }
    }

    protected void keyTyped(final char character, final int keyCode) throws IOException {
        if (keyCode != 1 && this.editingKey) {
            CustomCrosshairAddon.getCrosshairMod().setGuiKeyBind(Keyboard.getKeyName(keyCode));
            this.editingKey = false;
            this.button_editKeyBind.setDisplayText("Open GUI key: " + CustomCrosshairAddon.getCrosshairMod().getGuiKeyBind());
        }
        super.keyTyped(character, keyCode);
    }
}
