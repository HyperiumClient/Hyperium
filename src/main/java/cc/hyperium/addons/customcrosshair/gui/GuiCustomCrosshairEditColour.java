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

import java.awt.*;
import java.io.IOException;

public class GuiCustomCrosshairEditColour extends CustomCrosshairScreen {

    private Color editColour;
    private CCSlider slider_red;
    private CCSlider slider_green;
    private CCSlider slider_blue;
    private CCSlider slider_opacity;
    private CCTickbox tickbox_rainbow;
    private CCSlider slider_rainbowspeed;
    private String title;

    public GuiCustomCrosshairEditColour(CustomCrosshairAddon addon, final Color colour) {
        this(addon, colour, "Edit Colour...");
    }

    public GuiCustomCrosshairEditColour(CustomCrosshairAddon addon, final Color colour, final String text) {
        super(addon);

        editColour = colour;
        title = text;

        this.addon = addon;
    }

    public void initGui() {
        itemList.clear();

        itemList.add(slider_red = new CCSlider(this, 1, "Red", 0, 0, 255, 10, 0, 255)
            .setCallback(() ->
                editColour = new Color(slider_red.getValue(), editColour.getGreen(),
                    editColour.getBlue(), editColour.getAlpha())));
        slider_red.setValue(editColour.getRed());
        slider_red.setBoxColour(new Color(255, 0, 0, 128));
        slider_red.getHelpText().add("Changes how red the colour is.");

        itemList.add(slider_green = new CCSlider(this, 2, "Green", 0, 0, 255, 10, 0, 255)
            .setCallback(() ->
                editColour = new Color(editColour.getRed(), slider_green.getValue(),
                    editColour.getBlue(), editColour.getAlpha())));

        slider_green.setValue(editColour.getGreen());
        slider_green.setBoxColour(new Color(0, 255, 0, 128));
        slider_green.getHelpText().add("Changes how green the colour is.");

        itemList.add(slider_blue = new CCSlider(this, 3, "Blue", 0, 0, 255, 10, 0, 255)
            .setCallback(() ->
                editColour = new Color(editColour.getRed(), editColour.getGreen(),
                    slider_blue.getValue(), editColour.getAlpha())));
        slider_blue.setValue(editColour.getBlue());
        slider_blue.setBoxColour(new Color(0, 0, 255, 128));
        slider_blue.getHelpText().add("Changes how blue the colour is.");

        itemList.add(
            slider_opacity = new CCSlider(this, 4, "Opacity", 0, 0, 255, 10, 0, 255)
                .setCallback(() -> editColour = new Color(editColour.getRed(),
                    editColour.getGreen(), editColour.getBlue(),
                    slider_opacity.getValue())));

        slider_opacity.setValue(editColour.getAlpha());
        slider_opacity.getHelpText().add("Changes the opacity of the colour.");
        itemList.add(tickbox_rainbow = new CCTickbox(this, 5, "Rainbow", 0, 0) {
            @Override
            public void mouseClicked(final int mouseX, final int mouseY) {
                super.mouseClicked(mouseX, mouseY);
                CustomCrosshairAddon.getCrosshairMod().getCrosshair().setRainbowCrosshair(tickbox_rainbow.getChecked());
            }
        });

        tickbox_rainbow.setChecked(addon.getCrosshair().getRainbowCrosshair());
        tickbox_rainbow.getHelpText().add("Make crosshair rainbow.");
        itemList.add(slider_rainbowspeed = new CCSlider(this, 6, "Rainbow Speed", 0, 0, 255, 10, 0, 5000)
            .setCallback(() -> CustomCrosshairAddon.getCrosshairMod().getCrosshair().setRainbowSpeed(slider_rainbowspeed.getValue())));
        slider_rainbowspeed.setValue(addon.getCrosshair().getRainbowSpeed());
        slider_rainbowspeed.getHelpText().add("Change speed of rainbow.");

        int y = 71;
        for (int i = 0; i < itemList.size(); ++i) {
            if (i > 0) {
                y += itemList.get(i - 1).getHeight() + 6;
            }

            itemList.get(i).setPosition(21, y);
        }
        y = 71;

        for (int size = itemList.size(), j = 0; j < size; ++j) {
            itemList.add(new CCHelpButton(this, itemList.get(j).getHelpText()));
            if (j > 0) {
                y += itemList.get(j - 1).getHeight() + 6;
            }

            itemList.get(size + j).setPosition(5, y);
        }

        itemList.add(new CCButton(this, 0, "<- Return", width - 56, 0, 55, 25) {
            @Override
            public void mouseClicked(final int mouseX, final int mouseY) {
                mc.displayGuiScreen(new GuiCustomCrosshairEditCrosshair(addon));
            }
        });
    }

    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        drawDefaultBackground();
        final String titleText = "Custom Crosshair Addon" + " v" + CustomCrosshairAddon.VERSION;

        CustomCrosshairGraphics.drawBorderedRectangle(0, 0, width - 1, 25, CustomCrosshairAddon.PRIMARY, CustomCrosshairAddon.SECONDARY);
        drawString(fontRendererObj, titleText, 5, 10, 16777215);
        drawString(fontRendererObj, title, 5, 32, 16777215);
        CustomCrosshairGraphics.drawBorderedRectangle(5, 44, 55, 64, editColour, new Color(255, 255, 255, 255));

        toolTip = null;

        for (final CCGuiItem item : itemList) {
            item.drawItem(mouseX, mouseY);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        for (final CCGuiItem item : itemList) {
            if (mouseX >= item.getPosX() && mouseX <= item.getPosX() + item.getWidth()
                && mouseY >= item.getPosY() && mouseY <= item.getPosY() + item.getHeight()) {
                item.mouseClicked(mouseX, mouseY);
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int mouseButton) {
        for (final CCGuiItem item : itemList) {
            item.mouseReleased(mouseX, mouseY);
        }
        super.mouseReleased(mouseX, mouseY, mouseButton);
    }

    @Override
    public void onGuiClosed() {
        String lowerTitle = title.toLowerCase();

        if (lowerTitle.contains("dot")) {
            addon.getCrosshair().setDotColour(editColour);
        } else if (lowerTitle.contains("outline")) {
            addon.getCrosshair().setOutlineColour(editColour);
        } else {
            addon.getCrosshair().setColour(editColour);
        }
    }
}

