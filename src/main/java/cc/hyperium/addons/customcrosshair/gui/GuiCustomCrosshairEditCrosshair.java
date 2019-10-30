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

package cc.hyperium.addons.customcrosshair.gui;

import cc.hyperium.addons.customcrosshair.CustomCrosshairAddon;
import cc.hyperium.addons.customcrosshair.gui.items.*;
import cc.hyperium.addons.customcrosshair.utils.CustomCrosshairGraphics;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

        itemOffset = 0;
        helpButtonList = new ArrayList<>();
    }

    public void initGui() {
        itemList.clear();
        CCTickbox tickbox_enabled = new CCTickbox(this, 0, "Enabled", 0, 0).setCallback((checked) -> {
            if (addon.getCrosshair() != null) {
                addon.getCrosshair().setEnabled(checked);
            }
        });

        itemList.add(tickbox_enabled);

        tickbox_enabled.setChecked(addon.getCrosshair().getEnabled());
        tickbox_enabled.getHelpText().add("Enables or disables the custom crosshair mod.");

        CCEditColourButton editColour_crosshair;
        itemList.add(
            editColour_crosshair = new CCEditColourButton(addon, this, 6, "Crosshair Colour",
                0, 0, 100, 20, addon.getCrosshair().getColour()));
        editColour_crosshair.getHelpText().add("Changes the main colour of the crosshair.");
        itemList.add(
            slider_crosshairType = new CCSlider(this, 5, "Crosshair Type", 0, 0, 120, 10, 0,
                4));
        slider_crosshairType.setValue(addon.getCrosshair().getCrosshairTypeID());
        slider_crosshairType.getHelpText().add("Changes the crosshair type.");
        slider_crosshairType.getHelpText().add("[0 = Cross]");
        slider_crosshairType.getHelpText().add("[1 = Circle]");
        slider_crosshairType.getHelpText().add("[2 = Square]");
        slider_crosshairType.getHelpText().add("[3 = Arrow]");
        slider_crosshairType.getHelpText().add("[4 = X]");
        itemList
            .add(tickbox_visibleDefault = new CCTickbox(this, 18, "Crosshair Visible", 0, 0) {
                @Override
                public void mouseClicked(final int mouseX, final int mouseY) {
                    super.mouseClicked(mouseX, mouseY);
                    CustomCrosshairAddon.getCrosshairMod().getCrosshair().setVisibleDefault(
                        tickbox_visibleDefault.getChecked());
                }
            });
        tickbox_visibleDefault.setChecked(addon.getCrosshair().getVisibleDefault());
        tickbox_visibleDefault.getHelpText().add("Shows or hides the crosshair.");
        itemList.add(
            tickbox_visibleHiddenGui = new CCTickbox(this, 19, "Visible in Hide Gui", 0, 0) {
                @Override
                public void mouseClicked(final int mouseX, final int mouseY) {
                    super.mouseClicked(mouseX, mouseY);
                    addon.getCrosshair().setVisibleHiddenGui(
                        tickbox_visibleHiddenGui.getChecked());
                }
            });
        tickbox_visibleHiddenGui.setChecked(addon.getCrosshair().getVisibleHiddenGui());
        tickbox_visibleHiddenGui.getHelpText()
            .add("Shows or hides the crosshair when the HUD (F1 Mode) is off.");
        itemList.add(
            tickbox_visibleDebug = new CCTickbox(this, 1, "Visible in debug Screen", 0, 0) {
                @Override
                public void mouseClicked(final int mouseX, final int mouseY) {
                    super.mouseClicked(mouseX, mouseY);
                    addon.getCrosshair().setVisibleDebug(
                        tickbox_visibleDebug.getChecked());
                }
            });
        tickbox_visibleDebug.setChecked(addon.getCrosshair().getVisibleDebug());
        tickbox_visibleDebug.getHelpText()
            .add("Shows or hides the crosshair in the debug Screen (F3 Mode).");
        itemList.add(
            tickbox_visibleSpectator = new CCTickbox(this, 2, "Visible in spectator mode", 0,
                0) {
                @Override
                public void mouseClicked(final int mouseX, final int mouseY) {
                    super.mouseClicked(mouseX, mouseY);
                    addon.getCrosshair().setVisibleSpectator(
                        tickbox_visibleSpectator.getChecked());
                }
            });
        tickbox_visibleSpectator.setChecked(addon.getCrosshair().getVisibleSpectator());
        tickbox_visibleSpectator.getHelpText()
            .add("Shows or hides the crosshair when in spectator mode.");
        itemList.add(
            tickbox_visibleThirdPerson = new CCTickbox(this, 2, "Visible in third person mode",
                0, 0) {
                @Override
                public void mouseClicked(final int mouseX, final int mouseY) {
                    super.mouseClicked(mouseX, mouseY);
                    addon.getCrosshair().setVisibleThirdPerson(
                        tickbox_visibleThirdPerson
                            .getChecked());
                }
            });
        tickbox_visibleThirdPerson
            .setChecked(addon.getCrosshair().getVisibleThirdPerson());
        tickbox_visibleThirdPerson.getHelpText()
            .add("Shows or hides the crosshair when in third person mode.");
        itemList.add(tickbox_outline = new CCTickbox(this, 3, "Outline", 0, 0) {
            @Override
            public void mouseClicked(final int mouseX, final int mouseY) {
                super.mouseClicked(mouseX, mouseY);
                addon.getCrosshair().setOutline(
                    tickbox_outline.getChecked());
            }
        });
        tickbox_outline.setChecked(addon.getCrosshair().getOutline());
        tickbox_outline.getHelpText().add("Draws a black outline around the crosshair.");
        CCEditColourButton editColour_outline;
        itemList.add(
            editColour_outline = new CCEditColourButton(addon, this, 7, "Outline Colour", 0, 0,
                100, 20, addon.getCrosshair().getOutlineColour()));
        editColour_outline.getHelpText().add("Changes the outline colour of the crosshair.");
        itemList.add(tickbox_dot = new CCTickbox(this, 4, "Dot", 0, 0) {
            @Override
            public void mouseClicked(final int mouseX, final int mouseY) {
                super.mouseClicked(mouseX, mouseY);
                addon.getCrosshair().setDot(
                    tickbox_dot.getChecked());
            }
        });
        tickbox_dot.setChecked(addon.getCrosshair().getDot());
        tickbox_dot.getHelpText().add("Draws a white dot in the centre of the Screen.");
        CCEditColourButton editColour_dot;
        itemList.add(
            editColour_dot = new CCEditColourButton(addon, this, 7, "Dot Colour", 0, 0, 100,
                20, addon.getCrosshair().getDotColour()));
        editColour_dot.getHelpText().add("Changes the dot colour of the crosshair.");
        itemList
            .add(slider_width = new CCSlider(this, 10, "Width", 0, 0, 150, 10, 1, 50));
        slider_width.setValue(addon.getCrosshair().getWidth());
        slider_width.getHelpText().add("Changes the horizontal width of the crosshair.");
        itemList
            .add(slider_height = new CCSlider(this, 11, "Height", 0, 0, 150, 10, 1, 50));
        slider_height.setValue(addon.getCrosshair().getHeight());
        slider_height.getHelpText().add("Changes the vertical height of the crosshair.");
        itemList.add(slider_gap = new CCSlider(this, 12, "Gap", 0, 0, 150, 10, 0, 50));
        slider_gap.setValue(addon.getCrosshair().getGap());
        slider_gap.getHelpText().add("Changes the gap/radius at the centre of the crosshair.");
        itemList
            .add(slider_thickness = new CCSlider(this, 13, "Thickness", 0, 0, 150, 10, 1, 10));
        slider_thickness.setValue(addon.getCrosshair().getThickness());
        slider_thickness.getHelpText().add("Changes the thickness of the crosshair.");
        itemList.add(
            tickbox_dynamicBow = new CCTickbox(this, 14, "Dynamic Crosshair (Bow)", 0, 0) {
                @Override
                public void mouseClicked(final int mouseX, final int mouseY) {
                    super.mouseClicked(mouseX, mouseY);
                    addon.getCrosshair().setDynamicBow(
                        tickbox_dynamicBow.getChecked());
                }
            });
        tickbox_dynamicBow.setChecked(addon.getCrosshair().getDynamicBow());
        tickbox_dynamicBow.getHelpText()
            .add("When using a bow, indicates the duration of the pull animation.");
        int y = 32;
        for (int i = 0; i < itemList.size(); ++i) {
            if (i > 0) {
                y += itemList.get(i - 1).getHeight() + 6;
            }
            itemList.get(i).setPosition(21, y - itemOffset);
        }
        y = 32;
        helpButtonList.clear();
        for (int i = 0; i < itemList.size(); ++i) {
            helpButtonList.add(new CCHelpButton(this, itemList.get(i).getHelpText()));
            if (i > 0) {
                y += itemList.get(i - 1).getHeight() + 6;
            }
            helpButtonList.get(i).setPosition(5, y - itemOffset);
        }
        button_resetCrosshair = new CCButton(this, 20, "Reset", width - 101, 0, 50, 25) {
            @Override
            public void mouseClicked(final int mouseX, final int mouseY) {
                super.mouseClicked(mouseX, mouseY);
                CustomCrosshairAddon.getCrosshairMod().resetCrosshair();
                initGui();
            }
        };
        scrollbar = new CCScrollbar(this, 100, width - 11, 25, 10, height - 26,
            y - 9);
    }

    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        drawDefaultBackground();
        toolTip = null;
        for (final CCGuiItem item : itemList) {
            item.drawItem(mouseX, mouseY);
            switch (item.getActionID()) {
                case 5: {
                    addon.getCrosshair()
                        .setCrosshairType(slider_crosshairType.getValue());
                    break;
                }
                case 10: {
                    addon.getCrosshair().setWidth(slider_width.getValue());
                    break;
                }
                case 11: {
                    addon.getCrosshair().setHeight(slider_height.getValue());
                    break;
                }
                case 12: {
                    addon.getCrosshair().setGap(slider_gap.getValue());
                    break;
                }
                case 13: {
                    addon.getCrosshair().setThickness(slider_thickness.getValue());
                    break;
                }
            }
        }
        for (CCHelpButton aHelpButtonList : helpButtonList) {
            aHelpButtonList.drawItem(mouseX, mouseY);
        }

        final String titleText = "Custom Crosshair Addon" + " v" + CustomCrosshairAddon.VERSION;
        CustomCrosshairGraphics.drawBorderedRectangle(0, 0, width - 1, 25, CustomCrosshairAddon.PRIMARY,
            CustomCrosshairAddon.SECONDARY);
        CustomCrosshairGraphics.drawStringWithShadow(titleText, 5, 10, 16777215);
        button_resetCrosshair.drawItem(mouseX, mouseY);
        scrollbar.drawItem(mouseX, mouseY);
        if (prevScrollbarPosition != scrollbar.getValue()) {
            itemOffset = scrollbar.getValue();
            int y = 32;
            for (int j = 0; j < itemList.size(); ++j) {
                final CCGuiItem item2 = itemList.get(j);
                if (item2 != button_resetCrosshair && !(item2 instanceof CCHelpButton)) {
                    if (j > 0) {
                        y += itemList.get(j - 1).getHeight() + 6;
                    }
                    item2.setPosition(21, y - itemOffset);
                }
            }
            y = 32;
            for (int j = 0; j < itemList.size(); ++j) {
                if (j > 0) {
                    y += itemList.get(j - 1).getHeight() + 6;
                }
                helpButtonList.get(j).setPosition(5, y - itemOffset);
            }
        }
        prevScrollbarPosition = scrollbar.getValue();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void updateScreen() {
        final int wheel = Mouse.getDWheel();
        final int increment = (int) Math.ceil(scrollbar.getMaxValue() / 10);
        if (wheel > 0) {
            scrollbar.setValue(scrollbar.getValue() - increment);
        }
        if (wheel < 0) {
            scrollbar.setValue(scrollbar.getValue() + increment);
        }
        super.updateScreen();
    }

    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton)
        throws IOException {
        for (final CCGuiItem item : itemList) {
            if (mouseX >= item.getPosX() && mouseX <= item.getPosX() + item.getWidth()
                && mouseY >= item.getPosY() && mouseY <= item.getPosY() + item.getHeight()) {
                item.mouseClicked(mouseX, mouseY);
            }
        }
        if (mouseX >= button_resetCrosshair.getPosX()
            && mouseX <= button_resetCrosshair.getPosX() + button_resetCrosshair
            .getWidth() && mouseY >= button_resetCrosshair.getPosY()
            && mouseY <= button_resetCrosshair.getPosY() + button_resetCrosshair
            .getHeight()) {
            button_resetCrosshair.mouseClicked(mouseX, mouseY);
        }
        scrollbar.mouseClicked(mouseX, mouseY);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int mouseButton) {
        for (final CCGuiItem item : itemList) {
            item.mouseReleased(mouseX, mouseY);
        }
        scrollbar.mouseReleased(mouseX, mouseY);
        super.mouseReleased(mouseX, mouseY, mouseButton);
    }

    public boolean doesGuiPauseGame() {
        return false;
    }

    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        switch (keyCode) {
            case 200: {
                scrollbar.setValue(scrollbar.getValue() - 5);
                break;
            }
            case 208: {
                scrollbar.setValue(scrollbar.getValue() + 5);
                break;
            }
        }
        super.keyTyped(typedChar, keyCode);
    }
}

