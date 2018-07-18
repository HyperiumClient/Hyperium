package cc.hyperium.addons.customcrosshair.gui;

import cc.hyperium.addons.customcrosshair.gui.items.Button;
import cc.hyperium.addons.customcrosshair.gui.items.GuiItem;
import cc.hyperium.addons.customcrosshair.gui.items.HelpButton;
import cc.hyperium.addons.customcrosshair.gui.items.Slider;
import cc.hyperium.addons.customcrosshair.main.CustomCrosshairAddon;
import cc.hyperium.addons.customcrosshair.utils.GuiGraphics;
import cc.hyperium.addons.customcrosshair.utils.GuiTheme;
import cc.hyperium.addons.customcrosshair.utils.RGBA;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;

public class GuiEditColour extends ModGuiScreen
{
    private RGBA editColour;
    private Slider slider_red;
    private Slider slider_green;
    private Slider slider_blue;
    private Slider slider_opacity;
    private String title;

    public GuiEditColour(final RGBA colour) {
        this(colour, "Edit Colour...");
    }

    public GuiEditColour(final RGBA colour, final String text) {
        this.editColour = colour;
        this.title = text;
    }

    public void initGui() {
        final int[] screenSize = GuiGraphics.getScreenSize();
        this.itemList.clear();
        this.itemList.add(this.slider_red = new Slider(this, 1, "Red", 0, 0, 255, 10, 0, 255));
        this.slider_red.setValue(this.editColour.getRed());
        this.slider_red.setBoxColour(new RGBA(255, 0, 0, 128));
        this.slider_red.getHelpText().add("Changes how red the colour is.");
        this.itemList.add(this.slider_green = new Slider(this, 2, "Green", 0, 0, 255, 10, 0, 255));
        this.slider_green.setValue(this.editColour.getGreen());
        this.slider_green.setBoxColour(new RGBA(0, 255, 0, 128));
        this.slider_green.getHelpText().add("Changes how green the colour is.");
        this.itemList.add(this.slider_blue = new Slider(this, 3, "Blue", 0, 0, 255, 10, 0, 255));
        this.slider_blue.setValue(this.editColour.getBlue());
        this.slider_blue.setBoxColour(new RGBA(0, 0, 255, 128));
        this.slider_blue.getHelpText().add("Changes how blue the colour is.");
        this.itemList.add(this.slider_opacity = new Slider(this, 4, "Opacity", 0, 0, 255, 10, 0, 255));
        this.slider_opacity.setValue(this.editColour.getOpacity());
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
            this.itemList.add(new HelpButton(this, this.itemList.get(j).getHelpText()));
            if (j > 0) {
                y += this.itemList.get(j - 1).getHeight() + 6;
            }
            this.itemList.get(size + j).setPosition(5, y);
        }
        this.itemList.add(new Button(this, 0, "<- Return", screenSize[0] - 56, 0, 55, 25) {
            @Override
            public void mouseClicked(final int mouseX, final int mouseY) {
                GuiEditColour.this.mc.displayGuiScreen((GuiScreen)new GuiEditCrosshair());
            }
        });
    }

    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        final int[] screenSize = GuiGraphics.getScreenSize();
        this.drawDefaultBackground();
        final String titleText = "Custom Crosshair Addon" + " v" + CustomCrosshairAddon.VERSION;
        GuiGraphics.drawBorderedRectangle(0, 0, screenSize[0] - 1, 25, GuiTheme.PRIMARY, GuiTheme.SECONDARY);
        GuiGraphics.drawStringWithShadow(titleText, 5, 10, 16777215);
        GuiGraphics.drawStringWithShadow(this.title, 5, 32, 16777215);
        GuiGraphics.drawBorderedRectangle(5, 44, 55, 64, this.editColour, new RGBA(255, 255, 255, 255));
        this.toolTip = null;
        for (int i = 0; i < this.itemList.size(); ++i) {
            final GuiItem item = this.itemList.get(i);
            item.drawItem(mouseX, mouseY);
            switch (item.getActionID()) {
                case 1: {
                    this.editColour.setRed(this.slider_red.getValue());
                    break;
                }
                case 2: {
                    this.editColour.setGreen(this.slider_green.getValue());
                    break;
                }
                case 3: {
                    this.editColour.setBlue(this.slider_blue.getValue());
                    break;
                }
                case 4: {
                    this.editColour.setOpacity(this.slider_opacity.getValue());
                    break;
                }
            }
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        for (int i = 0; i < this.itemList.size(); ++i) {
            final GuiItem item = this.itemList.get(i);
            if (mouseX >= item.getPosX() && mouseX <= item.getPosX() + item.getWidth() && mouseY >= item.getPosY() && mouseY <= item.getPosY() + item.getHeight()) {
                item.mouseClicked(mouseX, mouseY);
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int mouseButton) {
        for (int i = 0; i < this.itemList.size(); ++i) {
            final GuiItem item = this.itemList.get(i);
            item.mouseReleased(mouseX, mouseY);
        }
        super.mouseReleased(mouseX, mouseY, mouseButton);
    }
}

