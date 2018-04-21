package cc.hyperium.mods.blockoverlay;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class BlockOverlayColor extends GuiScreen
{
    private GuiButton buttonChroma;
    private GuiButton buttonBack;
    private GuiSlider sliderChroma;
    private GuiSlider sliderRed;
    private GuiSlider sliderGreen;
    private GuiSlider sliderBlue;
    private GuiSlider sliderAlpha;

    public BlockOverlayColor() {
        super();
    }

    public void initGui() {
        super.buttonList.add(this.buttonChroma = new GuiButton(0, super.width / 2 - 100, super.height / 2 - 90, "Chroma: " + (BlockOverlay.isChroma ? "On" : "Off")));
        super.buttonList.add(this.sliderChroma = new GuiSlider(1, super.width / 2 - 100, super.height / 2 - 60, "Speed: ", 1, 10, BlockOverlay.chromaSpeed));
        super.buttonList.add(this.sliderRed = new GuiSlider(2, super.width / 2 - 100, super.height / 2 - 60, "Red: ", 0.0f, 1.0f, BlockOverlay.red));
        super.buttonList.add(this.sliderGreen = new GuiSlider(3, super.width / 2 - 100, super.height / 2 - 30, "Green: ", 0.0f, 1.0f, BlockOverlay.green));
        super.buttonList.add(this.sliderBlue = new GuiSlider(4, super.width / 2 - 100, super.height / 2, "Blue: ", 0.0f, 1.0f, BlockOverlay.blue));
        super.buttonList.add(this.sliderAlpha = new GuiSlider(5, super.width / 2 - 100, super.height / 2 + 30, "Alpha: ", 0.0f, 1.0f, BlockOverlay.alpha));
        super.buttonList.add(this.buttonBack = new GuiButton(6, super.width / 2 - 100, super.height / 2 + 80, "Back"));
    }

    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        super.drawDefaultBackground();
        if (BlockOverlay.isChroma) {
            this.sliderRed.enabled = false;
            this.sliderGreen.enabled = false;
            this.sliderBlue.enabled = false;
            this.sliderAlpha.yPosition = super.height / 2 - 30;
            this.sliderChroma.drawButton(super.mc, mouseX, mouseY);
        }
        else {
            this.sliderRed.enabled = true;
            this.sliderGreen.enabled = true;
            this.sliderBlue.enabled = true;
            this.sliderAlpha.yPosition = super.height / 2 + 30;
            this.sliderRed.drawButton(super.mc, mouseX, mouseY);
            this.sliderGreen.drawButton(super.mc, mouseX, mouseY);
            this.sliderBlue.drawButton(super.mc, mouseX, mouseY);
        }
        this.buttonChroma.drawButton(super.mc, mouseX, mouseY);
        this.sliderAlpha.drawButton(super.mc, mouseX, mouseY);
        this.buttonBack.drawButton(super.mc, mouseX, mouseY);
    }

    public void actionPerformed(final GuiButton button) {
        switch (button.id) {
            case 0: {
                BlockOverlay.isChroma = !BlockOverlay.isChroma;
                this.buttonChroma.displayString = "Chroma: " + (BlockOverlay.isChroma ? "On" : "Off");
                break;
            }
            case 1: {
                BlockOverlay.chromaSpeed = Math.round(this.sliderChroma.getSliderValue() * 9.0f + 1.0f);
                break;
            }
            case 2: {
                BlockOverlay.red = this.sliderRed.getSliderValue();
                break;
            }
            case 3: {
                BlockOverlay.green = this.sliderGreen.getSliderValue();
                break;
            }
            case 4: {
                BlockOverlay.blue = this.sliderBlue.getSliderValue();
                break;
            }
            case 5: {
                BlockOverlay.alpha = this.sliderAlpha.getSliderValue();
                break;
            }
            case 6: {
                Minecraft.getMinecraft().displayGuiScreen(new BlockOverlaySettings());
                break;
            }
        }
    }

    public void mouseClickMove(final int mouseX, final int mouseY, final int clickedMouseButton, final long timeSinceLastClick) {
        BlockOverlay.chromaSpeed = Math.round(this.sliderChroma.getSliderValue() * 9.0f + 1.0f);
        BlockOverlay.red = this.sliderRed.getSliderValue();
        BlockOverlay.green = this.sliderGreen.getSliderValue();
        BlockOverlay.blue = this.sliderBlue.getSliderValue();
        BlockOverlay.alpha = this.sliderAlpha.getSliderValue();
    }

    public void onGuiClosed() {
        BlockOverlay.saveConfig();
    }
}
