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

package cc.hyperium.mods.keystrokes.screen;

import cc.hyperium.Hyperium;
import cc.hyperium.gui.GuiBlock;
import cc.hyperium.mods.keystrokes.KeystrokesMod;
import cc.hyperium.mods.keystrokes.config.KeystrokesSettings;
import cc.hyperium.mods.keystrokes.keys.impl.CustomKey;
import cc.hyperium.mods.keystrokes.render.CustomKeyWrapper;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

public class GuiScreenEditKeys extends GuiScreen {

    private KeystrokesMod mod;
    private CustomKeyWrapper selected;
    private CustomKeyWrapper currentlyDragging;
    private int lastMouseX;
    private int lastMouseY;
    private boolean updated;
    private GuiButton changeKey;
    private GuiButton changeType;
    private GuiButton delete;
    private boolean listeningForNewKey;
    private int previousOpacity;

    GuiScreenEditKeys(KeystrokesMod mod) {
        selected = null;
        currentlyDragging = null;
        listeningForNewKey = false;
        this.mod = mod;
        previousOpacity = mod.getSettings().getKeyBackgroundOpacity();
    }

    @Override
    public void initGui() {
        buttonList.add(new GuiButton(1, width / 2 - 100, height / 2 - 44, "Add key"));
        buttonList.add(changeKey = new GuiButton(2, width / 2 - 100, height / 2 - 22, "Change key"));
        buttonList.add(changeType = new GuiButton(3, width / 2 - 100, height / 2, "Change type"));
        buttonList.add(delete = new GuiButton(4, width / 2 - 100, height / 2 + 22, "Delete"));
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 1:
                CustomKeyWrapper key = new CustomKeyWrapper(new CustomKey(mod, 30, 1), 10, 10);
                selected = key;
                mod.getRenderer().getCustomKeys().add(key);
                break;
            case 2:
                listeningForNewKey = true;
                changeKey.displayString = "Listening...";
                break;
            case 3:
                CustomKey theKey = selected.getKey();
                theKey.setType(theKey.getType() + 1);
                if (theKey.getType() > 2) {
                    theKey.setType(0);
                    break;
                }
                break;
            case 4:
                mod.getRenderer().getCustomKeys().remove(selected);
                selected = null;
                break;
        }
    }

    @Override
    public void handleInput() throws IOException {
        if (listeningForNewKey) {
            if (Mouse.next()) {
                int button = Mouse.getEventButton();
                if (Mouse.isButtonDown(button)) {
                    selected.getKey().setKey(button - 100);
                    listeningForNewKey = false;
                    changeKey.displayString = "Change key";
                    return;
                }
            }

            if (Keyboard.next()) {
                int key = Keyboard.getEventKey();
                if (Keyboard.isKeyDown(key)) {
                    selected.getKey().setKey(key);
                    listeningForNewKey = false;
                    changeKey.displayString = "Change key";
                    return;
                }
            }
        }

        super.handleInput();
    }

    @Override
    public void onGuiClosed() {
        if (updated) mod.getSettings().save();
        Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new GuiScreenKeystrokes(mod));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawDefaultBackground();
        delete.enabled = (selected != null);
        changeType.enabled = (selected != null);
        changeKey.enabled = (selected != null);

        if (selected != null) {
            GuiBlock hitbox = selected.getKey().getHitbox().multiply(mod.getSettings().getScale());
            drawRect(hitbox.getLeft(), hitbox.getTop(), hitbox.getRight(), hitbox.getBottom(), Color.WHITE.getRGB());
            mod.getSettings().setKeyBackgroundOpacity(120);
        }

        mod.getRenderer().renderKeystrokes();
        super.drawScreen(mouseX, mouseY, partialTicks);

        if (selected != null && Mouse.isButtonDown(0) && currentlyDragging == null &&
            selected.getKey().getHitbox().multiply(mod.getSettings().getScale()).isMouseOver(mouseX, mouseY)) {
            currentlyDragging = selected;
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        boolean hovered = false;
        for (GuiButton button : buttonList) {
            if (button.isMouseOver()) {
                hovered = true;
            }
        }

        if (!hovered) selected = null;

        getKeys().stream().filter(wrapper -> wrapper.getKey().getHitbox().multiply(mod.getSettings().getScale()).isMouseOver(mouseX, mouseY)).forEach(wrapper -> {
            selected = wrapper;
            lastMouseX = mouseX;
            lastMouseY = mouseY;
        });
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        if (currentlyDragging != null) {
            KeystrokesSettings settings = mod.getSettings();
            currentlyDragging.setxOffset(currentlyDragging.getxOffset() + (mouseX - lastMouseX) / settings.getScale());
            currentlyDragging.setyOffset(currentlyDragging.getyOffset() + (mouseY - lastMouseY) / settings.getScale());
            lastMouseX = mouseX;
            lastMouseY = mouseY;
            updated = true;
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        currentlyDragging = null;
        mod.getSettings().setKeyBackgroundOpacity(previousOpacity);
    }

    public List<CustomKeyWrapper> getKeys() {
        return mod.getRenderer().getCustomKeys();
    }
}
