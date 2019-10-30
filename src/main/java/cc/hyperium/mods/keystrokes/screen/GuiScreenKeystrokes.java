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

import cc.hyperium.mods.keystrokes.KeystrokesMod;
import cc.hyperium.mods.keystrokes.config.KeystrokesSettings;
import cc.hyperium.mods.keystrokes.screen.impl.GuiSliderFadeTime;
import cc.hyperium.mods.keystrokes.screen.impl.GuiSliderOpacity;
import cc.hyperium.mods.keystrokes.screen.impl.GuiSliderScale;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;

public class GuiScreenKeystrokes extends GuiScreen implements IScreen {

    private final KeystrokesMod mod;
    private final Minecraft mc;

    private GuiButton buttonEnabled;
    private GuiButton buttonShowMouseButtons;
    private GuiButton buttonShowSpacebar;
    private GuiButton buttonToggleChroma;
    private GuiButton buttonShowCPS;
    private GuiButton buttonShowCPSOnButton;
    private GuiButton buttonColors;
    private GuiButton buttonRightClick;
    private GuiButton buttonSneak;
    private GuiButton buttonFPS;
    private GuiButton buttonKeyBackground;
    private GuiButton buttonShowWASD;
    private GuiButton buttonLiteralKeys;
    private GuiButton buttonArrowKeys;

    private boolean dragging = false;
    private boolean updated = false;
    private int lastMouseX;
    private int lastMouseY;

    public GuiScreenKeystrokes(KeystrokesMod mod) {
        this.mod = mod;
        mc = Minecraft.getMinecraft();
    }

    @Override
    public void initGui() {
        buttonList.clear();

        KeystrokesSettings settings = mod.getSettings();

        buttonList.add(buttonEnabled = new GuiButton(0, width / 2 - 155, calculateHeight(-1), 150, 20,
            "Keystrokes: " + (settings.isEnabled() ? "On" : "Off")));
        buttonList.add(buttonShowMouseButtons = new GuiButton(1, width / 2 + 5, calculateHeight(-1), 150, 20,
            "Show mouse buttons: " + (settings.isShowingMouseButtons() ? "On" : "Off")));

        buttonList.add(buttonShowSpacebar = new GuiButton(2, width / 2 - 155, calculateHeight(0), 150, 20,
            "Show spacebar: " + (settings.isShowingSpacebar() ? "On" : "Off")));
        buttonList.add(buttonShowCPS = new GuiButton(3, width / 2 + 5, calculateHeight(0), 150, 20,
            "Show CPS counter: " + (settings.isShowingCPS() ? "On" : "Off")));

        buttonList.add(buttonShowCPSOnButton = new GuiButton(4, width / 2 - 155, calculateHeight(1), 150, 20,
            "Show CPS on buttons: " + (settings.isShowingCPSOnButtons() ? "On" : "Off")));
        buttonList.add(buttonToggleChroma = new GuiButton(5, width / 2 + 5, calculateHeight(1), 150, 20,
            "Chroma: " + (settings.isChroma() ? "On" : "Off")));

        buttonList.add(buttonRightClick = new GuiButton(6, width / 2 - 155, calculateHeight(2), 150, 20,
            "Click Counter: " + (settings.isLeftClick() ? "Left" : "Right")));
        buttonList.add(buttonSneak = new GuiButton(7, width / 2 + 5, calculateHeight(2), 150, 20,
            "Show sneak: " + (settings.isShowingSneak() ? "On" : "Off")));

        buttonList.add(buttonFPS = new GuiButton(8, width / 2 - 155, calculateHeight(3), 150, 20,
            "Show FPS: " + (settings.isShowingFPS() ? "On" : "Off")));
        buttonList.add(buttonKeyBackground = new GuiButton(9, width / 2 + 5, calculateHeight(3), 150, 20,
            "Key background: " + (settings.isKeyBackgroundEnabled() ? "On" : "Off")));

        buttonList.add(buttonColors = new GuiButton(10, width / 2 - 155, calculateHeight(4), 150, 20,
            "Edit key colors"));
        buttonList.add(new GuiButton(11, width / 2 + 5, calculateHeight(4), 150, 20,
            "Edit key background colors"));

        buttonList.add(new GuiSliderScale(mod, 12, width / 2 - 155, calculateHeight(5), 150, 20, this));
        buttonList.add(new GuiSliderFadeTime(mod, 13, width / 2 + 5, calculateHeight(5), 150, 20, this));

        buttonList.add(buttonShowWASD = new GuiButton(14, width / 2 - 155, calculateHeight(6), 150, 20,
            "Show WASD: " + (settings.isShowingWASD() ? "On" : "Off")));
        buttonList.add(new GuiSliderOpacity(mod, 15, width / 2 + 5, calculateHeight(6), 150, 20, this));

        buttonList.add(new GuiButton(16, width / 2 - 155, calculateHeight(7), 150, 20, "Edit Custom Keys"));
        buttonList.add(buttonLiteralKeys = new GuiButton(17, width / 2 + 5, calculateHeight(7), 150, 20,
            "Literal Keys: " + (settings.isUsingLiteralKeys() ? "On" : "Off")));

        buttonList.add(buttonArrowKeys = new GuiButton(18, width / 2 - 155, calculateHeight(8), 150, 20,
            "Arrow Keys: " + (settings.isUsingArrowKeys() ? "On" : "Off")));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        mod.getRenderer().renderKeystrokes();

        drawCenteredString(mc.fontRendererObj, "Keystrokes v" + mod.getVersion() + " - Created by Sk1er LLC", width / 2, 5, 16777215);

        buttonColors.enabled = !mod.getSettings().isChroma();
        buttonRightClick.enabled = !mod.getSettings().isShowingCPSOnButtons();

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        KeystrokesSettings settings = mod.getSettings();
        switch (button.id) {
            case 0:
                settings.setEnabled(!settings.isEnabled());
                buttonEnabled.displayString = "Keystrokes: " + (settings.isEnabled() ? "On" : "Off");
                updated = true;
                break;

            case 1:
                settings.setShowingMouseButtons(!settings.isShowingMouseButtons());
                buttonShowMouseButtons.displayString = "Show mouse buttons: " + (settings.isShowingMouseButtons() ? "On" : "Off");
                updated = true;
                break;

            case 2:
                settings.setShowingSpacebar(!settings.isShowingSpacebar());
                buttonShowSpacebar.displayString = "Show spacebar: " + (settings.isShowingSpacebar() ? "On" : "Off");
                updated = true;
                break;

            case 3:
                settings.setShowingCPS(!settings.isShowingCPS());
                buttonShowCPS.displayString = "Show CPS counter: " + (settings.isShowingCPS() ? "On" : "Off");
                updated = true;
                break;

            case 4:
                settings.setShowingCPSOnButtons(!settings.isShowingCPSOnButtons());
                buttonShowCPSOnButton.displayString = "Show CPS on buttons: " + (settings.isShowingCPSOnButtons() ? "On" : "Off");
                updated = true;
                break;

            case 5:
                settings.setChroma(!settings.isChroma());
                buttonToggleChroma.displayString = "Chroma: " + (settings.isChroma() ? "On" : "Off");
                updated = true;
                break;

            case 6:
                settings.setLeftClick(!settings.isLeftClick());
                buttonRightClick.displayString = "Click Counter: " + (settings.isLeftClick() ? "Left" : "Right");
                updated = true;
                break;

            case 7:
                settings.setShowingSneak(!settings.isShowingSneak());
                buttonSneak.displayString = "Show sneak: " + (settings.isShowingSneak() ? "On" : "Off");
                updated = true;
                break;

            case 8:
                settings.setShowingFPS(!settings.isShowingFPS());
                buttonFPS.displayString = "Show FPS: " + (settings.isShowingFPS() ? "On" : "Off");
                updated = true;
                break;

            case 9:
                settings.setKeyBackgroundEnabled(!settings.isKeyBackgroundEnabled());
                buttonKeyBackground.displayString = "Key background: " + (settings.isKeyBackgroundEnabled() ? "On" : "Off");
                updated = true;
                break;

            case 10:
                mc.displayGuiScreen(new GuiScreenColor(mod, new IScrollable() {
                    @Override
                    public double getAmount() {
                        return settings.getRed();
                    }

                    @Override
                    public void onScroll(double doubleAmount, int intAmount) {
                        settings.setRed(intAmount);
                        updated = true;
                    }
                }, new IScrollable() {
                    @Override
                    public double getAmount() {
                        return settings.getGreen();
                    }

                    @Override
                    public void onScroll(double doubleAmount, int intAmount) {
                        settings.setGreen(intAmount);
                        updated = true;
                    }
                }, new IScrollable() {
                    @Override
                    public double getAmount() {
                        return settings.getBlue();
                    }

                    @Override
                    public void onScroll(double doubleAmount, int intAmount) {
                        settings.setBlue(intAmount);
                        updated = true;
                    }
                }, new IScrollable() {
                    @Override
                    public double getAmount() {
                        return settings.getPressedRed();
                    }

                    @Override
                    public void onScroll(double doubleAmount, int intAmount) {
                        settings.setPressedRed(intAmount);
                        updated = true;
                    }
                }, new IScrollable() {
                    @Override
                    public double getAmount() {
                        return settings.getPressedGreen();
                    }

                    @Override
                    public void onScroll(double doubleAmount, int intAmount) {
                        settings.setPressedGreen(intAmount);
                        updated = true;
                    }
                }, new IScrollable() {
                    @Override
                    public double getAmount() {
                        return settings.getPressedBlue();
                    }

                    @Override
                    public void onScroll(double doubleAmount, int intAmount) {
                        settings.setPressedBlue(intAmount);
                        updated = true;
                    }
                }));
                break;
            case 11:
                mc.displayGuiScreen(new GuiScreenBackgroundColor(mod, new IScrollable() {
                    @Override
                    public double getAmount() {
                        return settings.getKeyBackgroundRed();
                    }

                    @Override
                    public void onScroll(double doubleAmount, int intAmount) {
                        settings.setKeyBackgroundRed(intAmount);
                        updated = true;
                    }
                }, new IScrollable() {
                    @Override
                    public double getAmount() {
                        return settings.getKeyBackgroundGreen();
                    }

                    @Override
                    public void onScroll(double doubleAmount, int intAmount) {
                        settings.setKeyBackgroundGreen(intAmount);
                        updated = true;
                    }
                }, new IScrollable() {
                    @Override
                    public double getAmount() {
                        return settings.getKeyBackgroundBlue();
                    }

                    @Override
                    public void onScroll(double doubleAmount, int intAmount) {
                        settings.setKeyBackgroundBlue(intAmount);
                        updated = true;
                    }
                }));
                break;

            case 14:
                settings.setShowingWASD(!settings.isShowingWASD());
                buttonShowWASD.displayString = "Show WASD: " + (settings.isShowingWASD() ? "On" : "Off");
                updated = true;
                break;

            case 16:
                mc.displayGuiScreen(new GuiScreenEditKeys(mod));
                break;
            case 17:
                settings.setUsingLiteralKeys(!settings.isUsingLiteralKeys());
                buttonLiteralKeys.displayString = "Literal Keys: " + (settings.isUsingLiteralKeys() ? "On" : "Off");
                updated = true;
                break;

            case 18:
                settings.setUsingArrowKeys(!settings.isUsingArrowKeys());
                buttonArrowKeys.displayString = "Arrow Keys: " + (settings.isUsingArrowKeys() ? "On" : "Off");
                updated = true;
                break;
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int button) {
        try {
            super.mouseClicked(mouseX, mouseY, button);
        } catch (IOException ignored) {
        }

        if (button == 0) {
            KeystrokesSettings settings = mod.getSettings();

            if (!settings.isEnabled()) return;

            int x = settings.getRenderX();
            int y = settings.getRenderY();
            int startX = (int) ((x - 4) * settings.getScale());
            int startY = (int) ((y - 4) * settings.getScale());
            int endX = (int) (startX + ((settings.getWidth() * settings.getScale())));
            int endY = (int) (startY + (settings.getHeight() * settings.getScale()));
            if (mouseX >= startX && mouseX <= endX && mouseY >= startY && mouseY <= endY) {
                dragging = true;
                lastMouseX = mouseX;
                lastMouseY = mouseY;
            }
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int action) {
        super.mouseReleased(mouseX, mouseY, action);
        dragging = false;
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int lastButtonClicked, long timeSinceMouseClick) {
        if (dragging) {
            KeystrokesSettings settings = mod.getSettings();
            settings.setX((int) (settings.getRenderX() + (mouseX - lastMouseX) / settings.getScale()));
            settings.setY((int) (settings.getRenderY() + (mouseY - lastMouseY) / settings.getScale()));
            lastMouseX = mouseX;
            lastMouseY = mouseY;
            updated = true;
        }
    }

    @Override
    public void onGuiClosed() {
        if (updated) mod.getSettings().save();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public void setUpdated() {
        updated = true;
    }

    public KeystrokesMod getMod() {
        return mod;
    }
}
