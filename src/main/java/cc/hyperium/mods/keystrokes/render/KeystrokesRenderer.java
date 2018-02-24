package cc.hyperium.mods.keystrokes.render;

import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.RenderEvent;
import cc.hyperium.mods.keystrokes.KeystrokesMod;
import cc.hyperium.mods.keystrokes.config.KeystrokesSettings;
import cc.hyperium.mods.keystrokes.keys.impl.CPSKey;
import cc.hyperium.mods.keystrokes.keys.impl.Key;
import cc.hyperium.mods.keystrokes.keys.impl.MouseButton;
import cc.hyperium.mods.keystrokes.keys.impl.SpaceKey;
import cc.hyperium.mods.keystrokes.screen.GuiScreenColor;
import cc.hyperium.mods.keystrokes.screen.GuiScreenKeystrokes;
import cc.hyperium.mods.keystrokes.utils.AntiReflection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

import java.io.IOException;

/**
 * The KeystrokesRenderer, taken and modified from the v3 source
 *
 * @author Fyu and boomboompower
 */
public class KeystrokesRenderer {

    @AntiReflection.HiddenField
    private final Minecraft mc = Minecraft.getMinecraft();

    @AntiReflection.HiddenField
    private final Key[] movementKeys = new Key[4];

    @AntiReflection.HiddenField
    private final CPSKey[] cpsKeys = new CPSKey[1];

    @AntiReflection.HiddenField
    private final SpaceKey[] spaceKey = new SpaceKey[1];

    @AntiReflection.HiddenField
    private final MouseButton[] mouseButtons = new MouseButton[2];

    public KeystrokesRenderer() {
        this.movementKeys[0] = new Key(this.mc.gameSettings.keyBindForward, 26, 2);
        this.movementKeys[1] = new Key(this.mc.gameSettings.keyBindBack, 26, 26);
        this.movementKeys[2] = new Key(this.mc.gameSettings.keyBindLeft, 2, 26);
        this.movementKeys[3] = new Key(this.mc.gameSettings.keyBindRight, 50, 26);

        this.cpsKeys[0] = new CPSKey(2, 92);

        this.spaceKey[0] = new SpaceKey(this.mc.gameSettings.keyBindJump, 2, 74);

        this.mouseButtons[0] = new MouseButton(0, 2, 50);
        this.mouseButtons[1] = new MouseButton(1, 38, 50);
    }

    @InvokeEvent
    public void onRenderTick(RenderEvent event) {
        if (this.mc.currentScreen != null) {
            if (this.mc.currentScreen instanceof GuiScreenKeystrokes || this.mc.currentScreen instanceof GuiScreenColor) {
                try {
                    this.mc.currentScreen.handleInput();
                } catch (IOException var3) {
                }
            }

        } else if (this.mc.inGameHasFocus && !this.mc.gameSettings.showDebugInfo) {
            this.renderKeystrokes();
        }
    }

    @AntiReflection.HiddenMethod
    public void renderKeystrokes() {
        KeystrokesSettings settings = KeystrokesMod.getSettings();
        if (settings.isEnabled()) {
            int x = settings.getX();
            int y = settings.getY();

            boolean showingMouseButtons = settings.isShowingMouseButtons();
            boolean showingSpacebar = settings.isShowingSpacebar();
            boolean showingCPS = settings.isShowingCPS();
            ScaledResolution res = new ScaledResolution(this.mc);

            int width = settings.getWidth();
            int height = settings.getHeight();

            if (x < 0) {
                settings.setX(0);
                x = settings.getX();
            } else if (x * settings.getScale() > res.getScaledWidth() - (width * settings.getScale())) {
                settings.setX((int) ((res.getScaledWidth() - (width * settings.getScale())) / settings.getScale()));
                x = settings.getX();
            }

            if (y < 0) {
                settings.setY(0);
                y = settings.getY();
            } else if (y * settings.getScale() > res.getScaledHeight() - (height * settings.getScale())) {
                settings.setY((int) ((res.getScaledHeight() - (height * settings.getScale())) / settings.getScale()));
                y = settings.getY();
            }

            if (settings.getScale() != 1D) {
                GlStateManager.pushMatrix();
                GlStateManager.scale(settings.getScale(), settings.getScale(), 1.0D);
            }

            this.drawMovementKeys(x, y);

            if (showingMouseButtons) {
                this.drawMouseButtons(x, y);
            }

            if (showingCPS) {
                this.drawCPSKeys(x, y);
            }

            if (showingSpacebar) {
                this.drawSpacebar(x, y);
            }

            if (settings.getScale() != 1D) {
                GlStateManager.popMatrix();
            }
        }
    }

    @AntiReflection.HiddenMethod
    private void drawMovementKeys(int x, int y) {
        for (Key key : this.movementKeys) {
            key.renderKey(x, y);
        }
    }

    @AntiReflection.HiddenMethod
    private void drawCPSKeys(int x, int y) {
        for (CPSKey key : this.cpsKeys) {
            key.renderKey(x, y);
        }
    }

    @AntiReflection.HiddenMethod
    private void drawSpacebar(int x, int y) {
        for (SpaceKey key : this.spaceKey) {
            key.renderKey(x, y);
        }
    }

    @AntiReflection.HiddenMethod
    private void drawMouseButtons(int x, int y) {
        for (MouseButton button : this.mouseButtons) {
            button.renderKey(x, y);
        }
    }
}