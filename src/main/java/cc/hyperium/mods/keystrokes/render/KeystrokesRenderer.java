/*
 *     Copyright (C) 2018  Hyperium <https://hyperium.cc/>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.mods.keystrokes.render;

import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.RenderHUDEvent;
import cc.hyperium.mods.keystrokes.KeystrokesMod;
import cc.hyperium.mods.keystrokes.keys.impl.CPSKey;
import cc.hyperium.mods.keystrokes.keys.impl.Key;
import cc.hyperium.mods.keystrokes.keys.impl.MouseButton;
import cc.hyperium.mods.keystrokes.keys.impl.SpaceKey;
import cc.hyperium.mods.keystrokes.screen.GuiScreenColor;
import cc.hyperium.mods.keystrokes.screen.GuiScreenKeystrokes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

import java.io.IOException;
import java.util.ArrayList;
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
    private final SpaceKey[] spaceKey = new SpaceKey[1];
    private final MouseButton[] mouseButtons = new MouseButton[2];
    private final SpaceKey[] sneakKeys = new SpaceKey[1];
    private final List<CustomKeyWrapper> customKeys = new ArrayList<>();

    public KeystrokesRenderer(KeystrokesMod mod) {
        this.mod = mod;

        this.movementKeys[0] = new Key(mod, this.mc.gameSettings.keyBindForward, 26, 2);
        this.movementKeys[1] = new Key(mod, this.mc.gameSettings.keyBindBack, 26, 26);
        this.movementKeys[2] = new Key(mod, this.mc.gameSettings.keyBindLeft, 2, 26);
        this.movementKeys[3] = new Key(mod, this.mc.gameSettings.keyBindRight, 50, 26);

        this.cpsKeys[0] = new CPSKey(mod, 2, 110);

        this.spaceKey[0] = new SpaceKey(mod, this.mc.gameSettings.keyBindJump, 2, 92, "SPACE");

        this.mouseButtons[0] = new MouseButton(mod, 0, 2, 50);
        this.mouseButtons[1] = new MouseButton(mod, 1, 38, 50);

        this.sneakKeys[0] = new SpaceKey(mod, mc.gameSettings.keyBindSneak, 2, 74, "Sneak");
        this.customKeys.addAll(mod.getSettings().getConfigWrappers());
    }

    public MouseButton[] getMouseButtons() {
        return mouseButtons;
    }

    public CPSKey[] getCPSKeys() {
        return this.cpsKeys;
    }

    public List<CustomKeyWrapper> getCustomKeys() {
        return customKeys;
    }

    @InvokeEvent
    public void onRenderTick(RenderHUDEvent event) {
        if (this.mc.currentScreen != null) {
            if (this.mc.currentScreen instanceof GuiScreenKeystrokes || this.mc.currentScreen instanceof GuiScreenColor) {
                try {
                    this.mc.currentScreen.handleInput();
                } catch (IOException ignored) {
                }
            }

        } else if (this.mc.inGameHasFocus && !this.mc.gameSettings.showDebugInfo) {
            this.renderKeystrokes();
        }
    }

    public void renderKeystrokes() {
        if (this.mod.getSettings().isEnabled()) {
            int x = this.mod.getSettings().getX();
            int y = this.mod.getSettings().getY();

            boolean showingMouseButtons = this.mod.getSettings().isShowingMouseButtons();
            boolean showingSpacebar = this.mod.getSettings().isShowingSpacebar();
            boolean showingCPS = this.mod.getSettings().isShowingCPS();
            boolean showingCPSOnButtons = this.mod.getSettings().isShowingCPSOnButtons();
            boolean showingSneak = mod.getSettings().isShowingSneak();
            ScaledResolution res = new ScaledResolution(this.mc);

            int width = this.mod.getSettings().getWidth();
            int height = this.mod.getSettings().getHeight();

            if (x < 0) {
                this.mod.getSettings().setX(0);
                x = this.mod.getSettings().getX();

            } else if (x * this.mod.getSettings().getScale() > res.getScaledWidth() - width * this.mod.getSettings().getScale()) {
                this.mod.getSettings().setX((int) ((res.getScaledWidth() - width * this.mod.getSettings().getScale()) / this.mod.getSettings().getScale()));
                x = this.mod.getSettings().getX();
            }

            if (y < 0) {
                this.mod.getSettings().setY(0);
                y = this.mod.getSettings().getY();

            } else if (y * this.mod.getSettings().getScale() > res.getScaledHeight() - height * this.mod.getSettings().getScale()) {
                this.mod.getSettings().setY((int) ((res.getScaledHeight() - height * this.mod.getSettings().getScale()) / this.mod.getSettings().getScale()));
                y = this.mod.getSettings().getY();
            }

            if (this.mod.getSettings().getScale() != 1.0) {
                GlStateManager.pushMatrix();
                GlStateManager.scale(this.mod.getSettings().getScale(), this.mod.getSettings().getScale(), 1.0);
            }

            this.drawMovementKeys(x, y);

            if (showingMouseButtons) {
                drawMouseButtons(x, y);
            }

            if (showingCPS && !showingCPSOnButtons) {
                drawCPSKeys(x, y);
            }

            if (showingSneak) {
                drawSneak(x, y);
            }

            if (showingSpacebar) {
                drawSpacebar(x, y);
            }

            y += 130;
            if (!mod.getSettings().isShowingMouseButtons()) {
                y -= 24;
            }

            if (!showingSneak) {
                y -= 18;
            }

            if (!showingSpacebar) {
                y -= 18;
            }

            if (!showingCPS || showingCPSOnButtons) {
                y = -18;
            }

            for (CustomKeyWrapper key : customKeys) {
                int xOffset = (int) key.getxOffset();
                int yOffset = (int) key.getyOffset();
                key.getKey().renderKey(x + xOffset, y + yOffset);
            }

            if (this.mod.getSettings().getScale() != 1.0) {
                GlStateManager.popMatrix();
            }
        }
    }

    private void drawMovementKeys(int x, int y) {
        for (Key key : this.movementKeys) {
            key.renderKey(x, y);
        }
    }

    private void drawCPSKeys(int x, int y) {
        for (CPSKey key : this.cpsKeys) {
            key.renderKey(x, y);
        }
    }

    private void drawSneak(int x, int y) {
        for (SpaceKey key : sneakKeys) {
            key.renderKey(x, y);
        }
    }

    private void drawSpacebar(int x, int y) {
        for (SpaceKey key : this.spaceKey) {
            key.renderKey(x, y);
        }
    }

    private void drawMouseButtons(int x, int y) {
        for (MouseButton button : this.mouseButtons) {
            button.renderKey(x, y);
        }
    }
}
