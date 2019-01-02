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

package cc.hyperium.addons.customcrosshair.gui;

import cc.hyperium.addons.customcrosshair.gui.items.CCButton;
import cc.hyperium.addons.customcrosshair.gui.items.CCEditColourButton;
import cc.hyperium.addons.customcrosshair.gui.items.CCGuiItem;
import cc.hyperium.addons.customcrosshair.gui.items.CCHelpButton;
import cc.hyperium.addons.customcrosshair.gui.items.CCScrollbar;
import cc.hyperium.addons.customcrosshair.gui.items.CCSlider;
import cc.hyperium.addons.customcrosshair.gui.items.CCTickbox;
import cc.hyperium.addons.customcrosshair.CustomCrosshairAddon;
import cc.hyperium.addons.customcrosshair.utils.CustomCrosshairGraphics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import org.lwjgl.input.Mouse;

public class GuiCustomCrosshairEditCrosshair extends CustomCrosshairScreen {

    private CCTickbox tickbox_visibleDefault;
    private CCTickbox tickbox_visibleHiddenGui;
    private CCTickbox tickbox_visibleDebug;
    private CCTickbox tickbox_visibleSpectator;
    private CCTickbox tickbox_visibleThirdPerson;
    private CCTickbox tickbox_outline;
    private CCTickbox tickbox_dot;
    private CCSlider slider_crosshairType;
    private CCSlider slider_width;
    private CCSlider slider_height;
    private CCSlider slider_gap;
    private CCSlider slider_thickness;
    private CCTickbox tickbox_dynamicBow;
    private CCButton button_resetCrosshair;
    private CCScrollbar scrollbar;
    private int prevScrollbarPosition;
    private int itemOffset;
    private List<CCHelpButton> helpButtonList;

    public GuiCustomCrosshairEditCrosshair(CustomCrosshairAddon addon) {
        super(addon);

        this.itemOffset = 0;
        this.helpButtonList = new ArrayList<>();
    }

    public void initGui() {
        this.itemList.clear();
        CCTickbox tickbox_enabled = new CCTickbox(this, 0, "Enabled", 0, 0).setCallback((checked) -> {
            if (addon.getCrosshair() != null) {
                addon.getCrosshair().setEnabled(checked);
            }
        });

        this.itemList.add(tickbox_enabled);

        tickbox_enabled.setChecked(this.addon.getCrosshair().getEnabled());
        tickbox_enabled.getHelpText().add("Enables or disables the custom crosshair mod.");

        CCEditColourButton editColour_crosshair;
        this.itemList.add(
            editColour_crosshair = new CCEditColourButton(addon, this, 6, "Crosshair Colour",
                0, 0, 100, 20, this.addon.getCrosshair().getColour()));
        editColour_crosshair.getHelpText().add("Changes the main colour of the crosshair.");
        this.itemList.add(
            this.slider_crosshairType = new CCSlider(this, 5, "Crosshair Type", 0, 0, 120, 10, 0,
                4));
        this.slider_crosshairType.setValue(this.addon.getCrosshair().getCrosshairTypeID());
        this.slider_crosshairType.getHelpText().add("Changes the crosshair type.");
        this.slider_crosshairType.getHelpText().add("[0 = Cross]");
        this.slider_crosshairType.getHelpText().add("[1 = Circle]");
        this.slider_crosshairType.getHelpText().add("[2 = Square]");
        this.slider_crosshairType.getHelpText().add("[3 = Arrow]");
        this.slider_crosshairType.getHelpText().add("[4 = X]");
        this.itemList
            .add(this.tickbox_visibleDefault = new CCTickbox(this, 18, "Crosshair Visible", 0, 0) {
                @Override
                public void mouseClicked(final int mouseX, final int mouseY) {
                    super.mouseClicked(mouseX, mouseY);
                    CustomCrosshairAddon.getCrosshairMod().getCrosshair().setVisibleDefault(
                        GuiCustomCrosshairEditCrosshair.this.tickbox_visibleDefault.getChecked());
                }
            });
        this.tickbox_visibleDefault.setChecked(this.addon.getCrosshair().getVisibleDefault());
        this.tickbox_visibleDefault.getHelpText().add("Shows or hides the crosshair.");
        this.itemList.add(
            this.tickbox_visibleHiddenGui = new CCTickbox(this, 19, "Visible in Hide Gui", 0, 0) {
                @Override
                public void mouseClicked(final int mouseX, final int mouseY) {
                    super.mouseClicked(mouseX, mouseY);
                    GuiCustomCrosshairEditCrosshair.this.addon.getCrosshair().setVisibleHiddenGui(
                        GuiCustomCrosshairEditCrosshair.this.tickbox_visibleHiddenGui.getChecked());
                }
            });
        this.tickbox_visibleHiddenGui.setChecked(this.addon.getCrosshair().getVisibleHiddenGui());
        this.tickbox_visibleHiddenGui.getHelpText()
            .add("Shows or hides the crosshair when the HUD (F1 Mode) is off.");
        this.itemList.add(
            this.tickbox_visibleDebug = new CCTickbox(this, 1, "Visible in debug screen", 0, 0) {
                @Override
                public void mouseClicked(final int mouseX, final int mouseY) {
                    super.mouseClicked(mouseX, mouseY);
                    GuiCustomCrosshairEditCrosshair.this.addon.getCrosshair().setVisibleDebug(
                        GuiCustomCrosshairEditCrosshair.this.tickbox_visibleDebug.getChecked());
                }
            });
        this.tickbox_visibleDebug.setChecked(this.addon.getCrosshair().getVisibleDebug());
        this.tickbox_visibleDebug.getHelpText()
            .add("Shows or hides the crosshair in the debug screen (F3 Mode).");
        this.itemList.add(
            this.tickbox_visibleSpectator = new CCTickbox(this, 2, "Visible in spectator mode", 0,
                0) {
                @Override
                public void mouseClicked(final int mouseX, final int mouseY) {
                    super.mouseClicked(mouseX, mouseY);
                    GuiCustomCrosshairEditCrosshair.this.addon.getCrosshair().setVisibleSpectator(
                        GuiCustomCrosshairEditCrosshair.this.tickbox_visibleSpectator.getChecked());
                }
            });
        this.tickbox_visibleSpectator.setChecked(this.addon.getCrosshair().getVisibleSpectator());
        this.tickbox_visibleSpectator.getHelpText()
            .add("Shows or hides the crosshair when in spectator mode.");
        this.itemList.add(
            this.tickbox_visibleThirdPerson = new CCTickbox(this, 2, "Visible in third person mode",
                0, 0) {
                @Override
                public void mouseClicked(final int mouseX, final int mouseY) {
                    super.mouseClicked(mouseX, mouseY);
                    GuiCustomCrosshairEditCrosshair.this.addon.getCrosshair().setVisibleThirdPerson(
                        GuiCustomCrosshairEditCrosshair.this.tickbox_visibleThirdPerson
                            .getChecked());
                }
            });
        this.tickbox_visibleThirdPerson
            .setChecked(this.addon.getCrosshair().getVisibleThirdPerson());
        this.tickbox_visibleThirdPerson.getHelpText()
            .add("Shows or hides the crosshair when in third person mode.");
        this.itemList.add(this.tickbox_outline = new CCTickbox(this, 3, "Outline", 0, 0) {
            @Override
            public void mouseClicked(final int mouseX, final int mouseY) {
                super.mouseClicked(mouseX, mouseY);
                GuiCustomCrosshairEditCrosshair.this.addon.getCrosshair().setOutline(
                    GuiCustomCrosshairEditCrosshair.this.tickbox_outline.getChecked());
            }
        });
        this.tickbox_outline.setChecked(this.addon.getCrosshair().getOutline());
        this.tickbox_outline.getHelpText().add("Draws a black outline around the crosshair.");
        CCEditColourButton editColour_outline;
        this.itemList.add(
            editColour_outline = new CCEditColourButton(addon, this, 7, "Outline Colour", 0, 0,
                100, 20, this.addon.getCrosshair().getOutlineColour()));
        editColour_outline.getHelpText().add("Changes the outline colour of the crosshair.");
        this.itemList.add(this.tickbox_dot = new CCTickbox(this, 4, "Dot", 0, 0) {
            @Override
            public void mouseClicked(final int mouseX, final int mouseY) {
                super.mouseClicked(mouseX, mouseY);
                GuiCustomCrosshairEditCrosshair.this.addon.getCrosshair().setDot(
                    GuiCustomCrosshairEditCrosshair.this.tickbox_dot.getChecked());
            }
        });
        this.tickbox_dot.setChecked(this.addon.getCrosshair().getDot());
        this.tickbox_dot.getHelpText().add("Draws a white dot in the centre of the screen.");
        CCEditColourButton editColour_dot;
        this.itemList.add(
            editColour_dot = new CCEditColourButton(addon, this, 7, "Dot Colour", 0, 0, 100,
                20, this.addon.getCrosshair().getDotColour()));
        editColour_dot.getHelpText().add("Changes the dot colour of the crosshair.");
        this.itemList
            .add(this.slider_width = new CCSlider(this, 10, "Width", 0, 0, 150, 10, 1, 50));
        this.slider_width.setValue(this.addon.getCrosshair().getWidth());
        this.slider_width.getHelpText().add("Changes the horizontal width of the crosshair.");
        this.itemList
            .add(this.slider_height = new CCSlider(this, 11, "Height", 0, 0, 150, 10, 1, 50));
        this.slider_height.setValue(this.addon.getCrosshair().getHeight());
        this.slider_height.getHelpText().add("Changes the vertical height of the crosshair.");
        this.itemList.add(this.slider_gap = new CCSlider(this, 12, "Gap", 0, 0, 150, 10, 0, 50));
        this.slider_gap.setValue(this.addon.getCrosshair().getGap());
        this.slider_gap.getHelpText().add("Changes the gap/radius at the centre of the crosshair.");
        this.itemList
            .add(this.slider_thickness = new CCSlider(this, 13, "Thickness", 0, 0, 150, 10, 1, 10));
        this.slider_thickness.setValue(this.addon.getCrosshair().getThickness());
        this.slider_thickness.getHelpText().add("Changes the thickness of the crosshair.");
        this.itemList.add(
            this.tickbox_dynamicBow = new CCTickbox(this, 14, "Dynamic Crosshair (Bow)", 0, 0) {
                @Override
                public void mouseClicked(final int mouseX, final int mouseY) {
                    super.mouseClicked(mouseX, mouseY);
                    GuiCustomCrosshairEditCrosshair.this.addon.getCrosshair().setDynamicBow(
                        GuiCustomCrosshairEditCrosshair.this.tickbox_dynamicBow.getChecked());
                }
            });
        this.tickbox_dynamicBow.setChecked(this.addon.getCrosshair().getDynamicBow());
        this.tickbox_dynamicBow.getHelpText()
            .add("When using a bow, indicates the duration of the pull animation.");
        int y = 32;
        for (int i = 0; i < this.itemList.size(); ++i) {
            if (i > 0) {
                y += this.itemList.get(i - 1).getHeight() + 6;
            }
            this.itemList.get(i).setPosition(21, y - this.itemOffset);
        }
        y = 32;
        this.helpButtonList.clear();
        for (int i = 0; i < this.itemList.size(); ++i) {
            this.helpButtonList.add(new CCHelpButton(this, this.itemList.get(i).getHelpText()));
            if (i > 0) {
                y += this.itemList.get(i - 1).getHeight() + 6;
            }
            this.helpButtonList.get(i).setPosition(5, y - this.itemOffset);
        }
        this.button_resetCrosshair = new CCButton(this, 20, "Reset", this.width - 101, 0, 50, 25) {
            @Override
            public void mouseClicked(final int mouseX, final int mouseY) {
                super.mouseClicked(mouseX, mouseY);
                CustomCrosshairAddon.getCrosshairMod().resetCrosshair();
                GuiCustomCrosshairEditCrosshair.this.initGui();
            }
        };
        this.scrollbar = new CCScrollbar(this, 100, this.width - 11, 25, 10, this.height - 26,
            y - 9);
    }

    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        this.toolTip = null;
        for (final CCGuiItem item : this.itemList) {
            item.drawItem(mouseX, mouseY);
            switch (item.getActionID()) {
                case 5: {
                    this.addon.getCrosshair()
                        .setCrosshairType(this.slider_crosshairType.getValue());
                    break;
                }
                case 10: {
                    this.addon.getCrosshair().setWidth(this.slider_width.getValue());
                    break;
                }
                case 11: {
                    this.addon.getCrosshair().setHeight(this.slider_height.getValue());
                    break;
                }
                case 12: {
                    this.addon.getCrosshair().setGap(this.slider_gap.getValue());
                    break;
                }
                case 13: {
                    this.addon.getCrosshair().setThickness(this.slider_thickness.getValue());
                    break;
                }
            }
        }
        for (CCHelpButton aHelpButtonList : this.helpButtonList) {
            aHelpButtonList.drawItem(mouseX, mouseY);
        }

        final String titleText = "Custom Crosshair Addon" + " v" + CustomCrosshairAddon.VERSION;
        CustomCrosshairGraphics.drawBorderedRectangle(0, 0, this.width - 1, 25, CustomCrosshairAddon.PRIMARY,
            CustomCrosshairAddon.SECONDARY);
        CustomCrosshairGraphics.drawStringWithShadow(titleText, 5, 10, 16777215);
        this.button_resetCrosshair.drawItem(mouseX, mouseY);
        this.scrollbar.drawItem(mouseX, mouseY);
        if (this.prevScrollbarPosition != this.scrollbar.getValue()) {
            this.itemOffset = this.scrollbar.getValue();
            int y = 32;
            for (int j = 0; j < this.itemList.size(); ++j) {
                final CCGuiItem item2 = this.itemList.get(j);
                if (item2 != this.button_resetCrosshair && !(item2 instanceof CCHelpButton)) {
                    if (j > 0) {
                        y += this.itemList.get(j - 1).getHeight() + 6;
                    }
                    item2.setPosition(21, y - this.itemOffset);
                }
            }
            y = 32;
            for (int j = 0; j < this.itemList.size(); ++j) {
                if (j > 0) {
                    y += this.itemList.get(j - 1).getHeight() + 6;
                }
                this.helpButtonList.get(j).setPosition(5, y - this.itemOffset);
            }
        }
        this.prevScrollbarPosition = this.scrollbar.getValue();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void updateScreen() {
        final int wheel = Mouse.getDWheel();
        final int increment = (int) Math.ceil(this.scrollbar.getMaxValue() / 10);
        if (wheel > 0) {
            this.scrollbar.setValue(this.scrollbar.getValue() - increment);
        }
        if (wheel < 0) {
            this.scrollbar.setValue(this.scrollbar.getValue() + increment);
        }
        super.updateScreen();
    }

    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton)
        throws IOException {
        for (final CCGuiItem item : this.itemList) {
            if (mouseX >= item.getPosX() && mouseX <= item.getPosX() + item.getWidth()
                && mouseY >= item.getPosY() && mouseY <= item.getPosY() + item.getHeight()) {
                item.mouseClicked(mouseX, mouseY);
            }
        }
        if (mouseX >= this.button_resetCrosshair.getPosX()
            && mouseX <= this.button_resetCrosshair.getPosX() + this.button_resetCrosshair
            .getWidth() && mouseY >= this.button_resetCrosshair.getPosY()
            && mouseY <= this.button_resetCrosshair.getPosY() + this.button_resetCrosshair
            .getHeight()) {
            this.button_resetCrosshair.mouseClicked(mouseX, mouseY);
        }
        this.scrollbar.mouseClicked(mouseX, mouseY);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int mouseButton) {
        for (final CCGuiItem item : this.itemList) {
            item.mouseReleased(mouseX, mouseY);
        }
        this.scrollbar.mouseReleased(mouseX, mouseY);
        super.mouseReleased(mouseX, mouseY, mouseButton);
    }

    public boolean doesGuiPauseGame() {
        return false;
    }

    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        switch (keyCode) {
            case 200: {
                this.scrollbar.setValue(this.scrollbar.getValue() - 5);
                break;
            }
            case 208: {
                this.scrollbar.setValue(this.scrollbar.getValue() + 5);
                break;
            }
        }
        super.keyTyped(typedChar, keyCode);
    }
}

