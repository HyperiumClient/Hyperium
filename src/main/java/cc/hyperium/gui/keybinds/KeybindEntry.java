package cc.hyperium.gui.keybinds;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

public class KeybindEntry {
    private String label;
    private KeybindButton keybindButton;
    private boolean visible = true;

    public KeybindEntry(String label, KeybindButton keybindButton) {
        this.label = label;
        this.keybindButton = keybindButton;
    }

    public void renderBind(int x, int y, FontRenderer fontRenderer, Minecraft mc, int mouseX, int mouseY) {
        if (!visible) {
            visible = true;
        }
        fontRenderer.drawString(label, x, y, Color.WHITE.getRGB());
        keybindButton.drawDynamicButton(mc, mouseX, mouseY, x + 150, y);
    }

    public String getLabel() {
        return label;
    }

    public KeybindButton getKeybindButton() {
        return keybindButton;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }
}
