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

package cc.hyperium.mods.keystrokes.screen;

import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.TickEvent;
import cc.hyperium.mods.keystrokes.KeystrokesMod;
import cc.hyperium.mods.keystrokes.config.KeystrokesSettings;
import cc.hyperium.mods.keystrokes.screen.impl.GuiSliderFadeTime;
import cc.hyperium.mods.keystrokes.screen.impl.GuiSliderScale;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import java.awt.Color;
import java.io.IOException;

public class GuiScreenKeystrokes extends GuiScreen {

    //        - 99
    //        - 75
    //        - 51
    //        - 27
    //        - 3
    //        + 21
    //        + 45
    //        + 69

    private final KeystrokesMod mod;
    private final Minecraft mc;

    private GuiButton buttonEnabled;
    private GuiButton buttonShowMouseButtons;
    private GuiButton buttonShowSpacebar;
    private GuiButton buttonToggleChroma;
    private GuiButton buttonShowCPS;
    private GuiButton buttonShowCPSOnButton;
    private GuiButton buttonTextColor;
    private GuiButton buttonPressedColor;
    private GuiButton buttonRightClick;

    private boolean dragging = false;
    private boolean updated = false;
    private int lastMouseX;
    private int lastMouseY;

    public GuiScreenKeystrokes(KeystrokesMod mod) {
        this.mod = mod;
        this.mc = Minecraft.getMinecraft();
    }

    @Override
    public void initGui() {
        this.buttonList.clear();

        KeystrokesSettings settings = this.mod.getSettings();

        this.buttonList.add(this.buttonEnabled = new GuiButton(0, this.width / 2 - 70, this.height / 2 - 102, 140, 20, "Keystrokes: " + (settings.isEnabled() ? "On" : "Off")));
        this.buttonList.add(this.buttonShowMouseButtons = new GuiButton(1, this.width / 2 - 70, this.height / 2 - 80, 140, 20, "Show mouse buttons: " + (settings.isShowingMouseButtons() ? "On" : "Off")));
        this.buttonList.add(this.buttonShowSpacebar = new GuiButton(2, this.width / 2 - 70, this.height / 2 - 58, 140, 20, "Show spacebar: " + (settings.isShowingSpacebar() ? "On" : "Off")));
        this.buttonList.add(this.buttonShowCPS = new GuiButton(3, this.width / 2 - 70, this.height / 2 - 36, 140, 20, "Show CPS counter: " + (settings.isShowingCPS() ? "On" : "Off")));
        this.buttonList.add(this.buttonShowCPSOnButton = new GuiButton(4, this.width / 2 - 70, this.height / 2 - 14, 140, 20, "Show CPS on buttons: " + (settings.isShowingCPSOnButtons() ? "On" : "Off")));
        this.buttonList.add(this.buttonToggleChroma = new GuiButton(5, this.width / 2 - 70, this.height / 2 + 8, 140, 20, "Chroma: " + (settings.isChroma() ? "On" : "Off")));
        this.buttonList.add(this.buttonTextColor = new GuiButton(6, this.width / 2 - 70, this.height / 2 + 30, 140, 20, "Edit text color"));
        this.buttonList.add(this.buttonPressedColor = new GuiButton(7, this.width / 2 - 70, this.height / 2 + 52, 140, 20, "Edit pressed text color"));
        this.buttonList.add(this.buttonRightClick = new GuiButton(10, this.width / 2 - 70, this.height / 2 + 74, 140, 20, "Click counter: " + (settings.isLeftClick() ? "Left" : "Right")));

        this.buttonList.add(new GuiSliderScale(this.mod, 8, this.width / 2 - 70, this.height / 2 + 96, 140, 20, this));
        this.buttonList.add(new GuiSliderFadeTime(this.mod, 9, this.width / 2 - 70, this.height / 2 + 118, 140, 20, this));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.mod.getRenderer().renderKeystrokes();

        drawCenteredString(this.mc.fontRendererObj, "KeystrokesMod v4.1", this.width / 2, 5, Color.WHITE.getRGB());
        drawCenteredString(this.mc.fontRendererObj, "Ported by boomboompower", this.width / 2, 16, Color.WHITE.getRGB());
        drawCenteredString(this.mc.fontRendererObj, "<3 from Sk1er", this.width / 2, 27, Color.WHITE.getRGB());

        this.buttonTextColor.enabled = !this.mod.getSettings().isChroma();
        this.buttonPressedColor.enabled = !this.mod.getSettings().isChroma();

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        KeystrokesSettings settings = this.mod.getSettings();
        switch (button.id) {
            case 0:
                settings.setEnabled(!settings.isEnabled());
                this.buttonEnabled.displayString = "Keystrokes: " + (settings.isEnabled() ? "On" : "Off");
                this.updated = true;
                break;
            case 1:
                settings.setShowingMouseButtons(!settings.isShowingMouseButtons());
                this.buttonShowMouseButtons.displayString = "Show mouse buttons: " + (settings.isShowingMouseButtons() ? "On" : "Off");
                this.updated = true;
                break;
            case 2:
                settings.setShowingSpacebar(!settings.isShowingSpacebar());
                this.buttonShowSpacebar.displayString = "Show spacebar: " + (settings.isShowingSpacebar() ? "On" : "Off");
                this.updated = true;
                break;
            case 3:
                settings.setShowingCPS(!settings.isShowingCPS());
                this.buttonShowCPS.displayString = "Show CPS counter: " + (settings.isShowingCPS() ? "On" : "Off");
                this.updated = true;
                break;
            case 4:
                settings.setShowingCPSOnButtons(!settings.isShowingCPSOnButtons());
                this.buttonShowCPSOnButton.displayString = "Show CPS on buttons: " + (settings.isShowingCPSOnButtons() ? "On" : "Off");
                this.updated = true;
                break;
            case 5:
                settings.setChroma(!settings.isChroma());
                this.buttonToggleChroma.displayString = "Chroma: " + (settings.isChroma() ? "On" : "Off");
                this.updated = true;
                break;
            case 6:
                Minecraft.getMinecraft().displayGuiScreen(new GuiScreenColor(this.mod,
                        new IScrollable() {
                            @Override
                            public double getAmount() {
                                return settings.getRed();
                            }

                            @Override
                            public void onScroll(double doubleAmount, int intAmount) {
                                settings.setRed(intAmount);
                                updated = true;
                            }
                        },
                        new IScrollable() {
                            @Override
                            public double getAmount() {
                                return settings.getGreen();
                            }

                            @Override
                            public void onScroll(double doubleAmount, int intAmount) {
                                settings.setGreen(intAmount);
                                updated = true;
                            }
                        },
                        new IScrollable() {
                            @Override
                            public double getAmount() {
                                return settings.getBlue();
                            }

                            @Override
                            public void onScroll(double doubleAmount, int intAmount) {
                                settings.setBlue(intAmount);
                                updated = true;
                            }
                        }
                ));
                break;
            case 7:
                Minecraft.getMinecraft().displayGuiScreen(new GuiScreenColor(this.mod,
                        new IScrollable() {
                            @Override
                            public double getAmount() {
                                return settings.getPressedRed();
                            }

                            @Override
                            public void onScroll(double doubleAmount, int intAmount) {
                                settings.setPressedRed(intAmount);
                                updated = true;
                            }
                        },
                        new IScrollable() {
                            @Override
                            public double getAmount() {
                                return settings.getPressedGreen();
                            }

                            @Override
                            public void onScroll(double doubleAmount, int intAmount) {
                                settings.setPressedGreen(intAmount);
                                updated = true;
                            }
                        },
                        new IScrollable() {
                            @Override
                            public double getAmount() {
                                return settings.getPressedBlue();
                            }

                            @Override
                            public void onScroll(double doubleAmount, int intAmount) {
                                settings.setPressedBlue(intAmount);
                                updated = true;
                            }
                        }
                ));
                break;
            case 10:
                settings.setLeftClick(!settings.isLeftClick());
                this.buttonRightClick.displayString = "Click counter: " + (settings.isLeftClick() ? "Left" : "Right");
                this.updated = true;
            default:
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
            KeystrokesSettings settings = this.mod.getSettings();
            if (!settings.isEnabled()) {
                return;
            }
            int startX = (int) ((settings.getX() - 4) * settings.getScale());
            int startY = (int) ((settings.getY() - 4) * settings.getScale());
            int endX = (int) (startX + ((settings.getWidth() * settings.getScale())));
            int endY = (int) (startY + (settings.getHeight() * settings.getScale()));
            if (mouseX >= startX && mouseX <= endX && mouseY >= startY && mouseY <= endY) {
                this.dragging = true;
                this.lastMouseX = mouseX;
                this.lastMouseY = mouseY;
            }
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int action) {
        super.mouseReleased(mouseX, mouseY, action);
        this.dragging = false;
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int lastButtonClicked, long timeSinceMouseClick) {
        if (this.dragging) {
            KeystrokesSettings settings = this.mod.getSettings();
            settings.setX((int) (settings.getX() + (mouseX - this.lastMouseX) / settings.getScale()));
            settings.setY((int) (settings.getY() + (mouseY - this.lastMouseY) / settings.getScale()));
            this.lastMouseX = mouseX;
            this.lastMouseY = mouseY;
            this.updated = true;
        }
    }

    @Override
    public void onGuiClosed() {
        if (this.updated) {
            this.mod.getSettings().save();
        }
    }

    public void display() {
        EventBus.INSTANCE.register(this);
    }

    @InvokeEvent
    public void tick(TickEvent event) {
        EventBus.INSTANCE.unregister(this);
        Minecraft.getMinecraft().displayGuiScreen(this);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public void setUpdated() {
        this.updated = true;
    }

    public KeystrokesMod getMod() {
        return this.mod;
    }
}
