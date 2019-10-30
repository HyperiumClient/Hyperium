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

package cc.hyperium.mods.keystrokes.render;

import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.render.RenderHUDEvent;
import cc.hyperium.mods.keystrokes.KeystrokesMod;
import cc.hyperium.mods.keystrokes.keys.impl.*;
import cc.hyperium.mods.keystrokes.screen.GuiScreenColor;
import cc.hyperium.mods.keystrokes.screen.GuiScreenKeystrokes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The KeystrokesRenderer, taken and modified from the v3 source
 *
 * @author Fyu and boomboompower
 */
public class KeystrokesRenderer {

    private final Minecraft mc = Minecraft.getMinecraft();
    private final KeystrokesMod mod;
    private final Key[] movementKeys = new Key[4];
    private final CPSKey[] cpsKeys = new CPSKey[1];
    private final FPSKey[] fpsKeys = new FPSKey[1];
    private final SpaceKey[] spaceKey = new SpaceKey[1];
    private final MouseButton[] mouseButtons = new MouseButton[2];
    private final SpaceKey[] sneakKeys = new SpaceKey[1];
    private final List<CustomKeyWrapper> customKeys = new ArrayList<>();

    public KeystrokesRenderer(KeystrokesMod mod) {
        this.mod = mod;

        movementKeys[0] = new Key(mod, mc.gameSettings.keyBindForward, 26, 2);
        movementKeys[1] = new Key(mod, mc.gameSettings.keyBindBack, 26, 26);
        movementKeys[2] = new Key(mod, mc.gameSettings.keyBindLeft, 2, 26);
        movementKeys[3] = new Key(mod, mc.gameSettings.keyBindRight, 50, 26);

        cpsKeys[0] = new CPSKey(mod, 2, 110);

        fpsKeys[0] = new FPSKey(mod, 2, 110 + 18);

        spaceKey[0] = new SpaceKey(mod, mc.gameSettings.keyBindJump, 2, 92, "SPACE");

        mouseButtons[0] = new MouseButton(mod, 0, 2, 50);
        mouseButtons[1] = new MouseButton(mod, 1, 38, 50);

        sneakKeys[0] = new SpaceKey(mod, mc.gameSettings.keyBindSneak, 2, 74, "Sneak");
        customKeys.addAll(mod.getSettings().getConfigWrappers());
    }

    public MouseButton[] getMouseButtons() {
        return mouseButtons;
    }

    public CPSKey[] getCPSKeys() {
        return cpsKeys;
    }

    public List<CustomKeyWrapper> getCustomKeys() {
        return customKeys;
    }

    @InvokeEvent
    public void onRenderTick(RenderHUDEvent event) {
        if (mc.currentScreen != null) {
            if (mc.currentScreen instanceof GuiScreenKeystrokes || mc.currentScreen instanceof GuiScreenColor) {
                try {
                    mc.currentScreen.handleInput();
                } catch (IOException ignored) {
                }
            }
        } else if (mc.inGameHasFocus && !mc.gameSettings.showDebugInfo) {
            renderKeystrokes();
        }
    }

    public void renderKeystrokes() {
        if (mod.getSettings().isEnabled()) {
            int x = mod.getSettings().getRenderX();
            int y = mod.getSettings().getRenderY();

            boolean showingMouseButtons = mod.getSettings().isShowingMouseButtons();
            boolean showingSpacebar = mod.getSettings().isShowingSpacebar();
            boolean showingCPS = mod.getSettings().isShowingCPS();
            boolean showingCPSOnButtons = mod.getSettings().isShowingCPSOnButtons();
            boolean showingSneak = mod.getSettings().isShowingSneak();
            boolean showingFPS = mod.getSettings().isShowingFPS();
            boolean showingWASD = mod.getSettings().isShowingWASD();

            if (mod.getSettings().getScale() != 1.0) {
                GlStateManager.pushMatrix();
                GlStateManager.scale(mod.getSettings().getScale(), mod.getSettings().getScale(), 1.0);
            }

            if (showingMouseButtons) drawMouseButtons(x, y);
            if (showingCPS && !showingCPSOnButtons) drawCPSKeys(x, y);
            if (showingSneak) drawSneak(x, y);
            if (showingSpacebar) drawSpacebar(x, y);
            if (showingFPS) drawFPS(x, y);
            if (showingWASD) drawMovementKeys(x, y);

            y += 130;

            if (!mod.getSettings().isShowingMouseButtons()) y -= 24;
            if (!showingSneak) y -= 18;
            if (!showingSpacebar) y -= 18;
            if (!showingCPS || showingCPSOnButtons) y -= 18;
            if (!showingFPS) y -= 18;
            if (!showingWASD) y -= 18;

            for (CustomKeyWrapper key : customKeys) {
                int xOffset = (int) key.getxOffset();
                int yOffset = (int) key.getyOffset();
                key.getKey().renderKey(x + xOffset, y + yOffset);
            }

            if (mod.getSettings().getScale() != 1.0) GlStateManager.popMatrix();
        }
    }

    private void drawMovementKeys(int x, int y) {
        Arrays.stream(movementKeys).forEach(key -> key.renderKey(x, y));
    }

    private void drawCPSKeys(int x, int y) {
        Arrays.stream(cpsKeys).forEach(key -> key.renderKey(x, y));
    }

    private void drawSneak(int x, int y) {
        Arrays.stream(sneakKeys).forEach(key -> key.renderKey(x, y));
    }

    private void drawSpacebar(int x, int y) {
        Arrays.stream(spaceKey).forEach(key -> key.renderKey(x, y));
    }

    private void drawMouseButtons(int x, int y) {
        Arrays.stream(mouseButtons).forEach(button -> button.renderKey(x, y));
    }

    private void drawFPS(int x, int y) {
        Arrays.stream(fpsKeys).forEach(key -> key.renderKey(x, y));
    }
}
