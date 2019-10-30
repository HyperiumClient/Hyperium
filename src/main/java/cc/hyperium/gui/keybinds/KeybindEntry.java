/*
 *       Copyright (C) 2018-present Hyperium <https://hyperium.cc/>
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU Lesser General Public License as published
 *       by the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU Lesser General Public License for more details.
 *
 *       You should have received a copy of the GNU Lesser General Public License
 *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
        if (!visible) visible = true;
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
