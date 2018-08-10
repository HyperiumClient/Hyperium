package cc.hyperium.addons.customcrosshair.gui;

import cc.hyperium.addons.customcrosshair.gui.items.CCButton;
import cc.hyperium.addons.customcrosshair.gui.items.CCGuiItem;
import cc.hyperium.addons.customcrosshair.gui.items.CCHelpButton;
import cc.hyperium.addons.customcrosshair.gui.items.CCSlider;
import cc.hyperium.addons.customcrosshair.CustomCrosshairAddon;
import cc.hyperium.addons.customcrosshair.utils.CustomCrosshairGraphics;
import java.awt.Color;

import java.io.IOException;

public class GuiCustomCrosshairEditColour extends CustomCrosshairScreen {

    private Color editColour;
    private CCSlider slider_red;
    private CCSlider slider_green;
    private CCSlider slider_blue;
    private CCSlider slider_opacity;
    private String title;

    public GuiCustomCrosshairEditColour(CustomCrosshairAddon addon, final Color colour) {
        this(addon, colour, "Edit Colour...");
    }

    public GuiCustomCrosshairEditColour(CustomCrosshairAddon addon, final Color colour, final String text) {
        super(addon);

        this.editColour = colour;
        this.title = text;

        this.addon = addon;
    }

    public void initGui() {
        this.itemList.clear();
        this.itemList.add(this.slider_red = new CCSlider(this, 1, "Red", 0, 0, 255, 10, 0, 255)
            .setCallback(() -> {
                this.editColour = new Color(this.slider_red.getValue(), this.editColour.getGreen(),
                    this.editColour.getBlue(), this.editColour.getAlpha());
            }));
        this.slider_red.setValue(this.editColour.getRed());
        this.slider_red.setBoxColour(new Color(255, 0, 0, 128));
        this.slider_red.getHelpText().add("Changes how red the colour is.");
        this.itemList.add(this.slider_green = new CCSlider(this, 2, "Green", 0, 0, 255, 10, 0, 255)
            .setCallback(() -> {
                this.editColour = new Color(this.editColour.getRed(), this.slider_green.getValue(),
                    this.editColour.getBlue(), this.editColour.getAlpha());
            }));
        this.slider_green.setValue(this.editColour.getGreen());
        this.slider_green.setBoxColour(new Color(0, 255, 0, 128));
        this.slider_green.getHelpText().add("Changes how green the colour is.");
        this.itemList.add(this.slider_blue = new CCSlider(this, 3, "Blue", 0, 0, 255, 10, 0, 255)
            .setCallback(() -> {
                this.editColour = new Color(this.editColour.getRed(), this.editColour.getGreen(),
                    this.slider_blue.getValue(), this.editColour.getAlpha());
            }));
        this.slider_blue.setValue(this.editColour.getBlue());
        this.slider_blue.setBoxColour(new Color(0, 0, 255, 128));
        this.slider_blue.getHelpText().add("Changes how blue the colour is.");
        this.itemList.add(
            this.slider_opacity = new CCSlider(this, 4, "Opacity", 0, 0, 255, 10, 0, 255)
                .setCallback(() -> {
                    this.editColour = new Color(this.editColour.getRed(),
                        this.editColour.getGreen(), this.editColour.getBlue(),
                        this.slider_opacity.getValue());
                }));
        this.slider_opacity.setValue(this.editColour.getAlpha());
        this.slider_opacity.getHelpText().add("Changes the opacity of the colour.");
        int y = 71;
        for (int i = 0; i < this.itemList.size(); ++i) {
            if (i > 0) {
                y += this.itemList.get(i - 1).getHeight() + 6;
            }
            this.itemList.get(i).setPosition(21, y);
        }
        y = 71;
        for (int size = this.itemList.size(), j = 0; j < size; ++j) {
            this.itemList.add(new CCHelpButton(this, this.itemList.get(j).getHelpText()));
            if (j > 0) {
                y += this.itemList.get(j - 1).getHeight() + 6;
            }
            this.itemList.get(size + j).setPosition(5, y);
        }
        this.itemList.add(new CCButton(this, 0, "<- Return", this.width - 56, 0, 55, 25) {
            @Override
            public void mouseClicked(final int mouseX, final int mouseY) {
                GuiCustomCrosshairEditColour.this.mc.displayGuiScreen(new GuiCustomCrosshairEditCrosshair(addon));
            }
        });
    }

    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        final String titleText = "Custom Crosshair Addon" + " v" + CustomCrosshairAddon.VERSION;

        CustomCrosshairGraphics.drawBorderedRectangle(0, 0, this.width - 1, 25, CustomCrosshairAddon.PRIMARY, CustomCrosshairAddon.SECONDARY);
        drawString(this.fontRendererObj, titleText, 5, 10, 16777215);
        drawString(this.fontRendererObj, this.title, 5, 32, 16777215);
        CustomCrosshairGraphics.drawBorderedRectangle(5, 44, 55, 64, this.editColour, new Color(255, 255, 255, 255));

        this.toolTip = null;

        for (final CCGuiItem item : this.itemList) {
            item.drawItem(mouseX, mouseY);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        for (final CCGuiItem item : this.itemList) {
            if (mouseX >= item.getPosX() && mouseX <= item.getPosX() + item.getWidth()
                && mouseY >= item.getPosY() && mouseY <= item.getPosY() + item.getHeight()) {
                item.mouseClicked(mouseX, mouseY);
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int mouseButton) {
        for (final CCGuiItem item : this.itemList) {
            item.mouseReleased(mouseX, mouseY);
        }
        super.mouseReleased(mouseX, mouseY, mouseButton);
    }

    @Override
    public void onGuiClosed() {
        String lowerTitle = this.title.toLowerCase();

        if (lowerTitle.contains("dot")) {
            this.addon.getCrosshair().setDotColour(this.editColour);
        } else if (lowerTitle.contains("outline")) {
            this.addon.getCrosshair().setOutlineColour(this.editColour);
        } else {
            this.addon.getCrosshair().setColour(this.editColour);
        }
    }
}

