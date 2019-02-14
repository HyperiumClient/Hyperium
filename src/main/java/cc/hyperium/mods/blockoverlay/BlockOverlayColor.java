package cc.hyperium.mods.blockoverlay;

import cc.hyperium.Hyperium;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.config.GuiSlider;

public class BlockOverlayColor extends GuiScreen {
    private BlockOverlay mod;
    private GuiButton buttonChroma;
    private GuiButton buttonBack;
    private GuiSlider sliderChroma;
    private GuiSlider sliderRed;
    private GuiSlider sliderGreen;
    private GuiSlider sliderBlue;
    private GuiSlider sliderAlpha;

    public BlockOverlayColor(BlockOverlay mod) {
        this.mod = mod;
    }

    public void initGui() {
        super.buttonList.add(this.buttonChroma = new GuiButton(0, super.width / 2 - 50, super.height / 2 - 60, 100, 20, "Chroma: " + (this.mod.getSettings().isChroma() ? "On" : "Off")));
        super.buttonList.add(this.sliderChroma = new GuiSlider(1, super.width / 2 - 50, super.height / 2 - 35, 100, 20, "Speed: ", "", 1, 5, this.mod.getSettings().getChromaSpeed(), false, true));
        super.buttonList.add(this.sliderRed = new GuiSlider(2, super.width / 2 - 50, super.height / 2 - 35, 100, 20, "Red: ", "", 0, 255, this.mod.getSettings().getOverlayRed() * 255, false, true));
        super.buttonList.add(this.sliderGreen = new GuiSlider(3, super.width / 2 - 50, super.height / 2 - 10, 100, 20, "Green: ", "", 0, 255, this.mod.getSettings().getOverlayGreen() * 255, false, true));
        super.buttonList.add(this.sliderBlue = new GuiSlider(4, super.width / 2 - 50, super.height / 2 + 15, 100, 20, "Blue: ", "", 0, 255, this.mod.getSettings().getOverlayBlue() * 255, false, true));
        super.buttonList.add(this.sliderAlpha = new GuiSlider(5, super.width / 2 - 50, super.height / 2 + 40, 100, 20, "Alpha: ", "", 0, 255, this.mod.getSettings().getOverlayAlpha() * 255, false, true));
        super.buttonList.add(this.buttonBack = new GuiButton(6, super.width / 2 - 50, super.height / 2 + 65, 100, 20, "Back"));
    }

    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        super.drawDefaultBackground();
        if (this.mod.getSettings().isChroma()) {
            this.sliderRed.enabled = false;
            this.sliderGreen.enabled = false;
            this.sliderBlue.enabled = false;
            this.sliderAlpha.yPosition = super.height / 2 - 10;
            this.sliderChroma.enabled = true;
            this.sliderChroma.drawButton(super.mc, mouseX, mouseY);
        } else {
            this.sliderRed.enabled = true;
            this.sliderGreen.enabled = true;
            this.sliderBlue.enabled = true;
            this.sliderAlpha.yPosition = super.height / 2 + 40;
            this.sliderChroma.enabled = false;
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
            case 0:
                this.mod.getSettings().setChroma(!this.mod.getSettings().isChroma());
                this.buttonChroma.displayString = "Chroma: " + (this.mod.getSettings().isChroma() ? "On" : "Off");
                break;
            case 1:
                this.mod.getSettings().setChromaSpeed(this.sliderChroma.getValueInt());
                break;
            case 2:
                this.mod.getSettings().setOverlayRed(this.sliderRed.getValueInt() / 255.0f);
                break;
            case 3:
                this.mod.getSettings().setOverlayGreen(this.sliderGreen.getValueInt() / 255.0f);
                break;
            case 4:
                this.mod.getSettings().setOverlayBlue(this.sliderBlue.getValueInt() / 255.0f);
                break;
            case 5:
                this.mod.getSettings().setOverlayAlpha(this.sliderAlpha.getValueInt() / 255.0f);
                break;
            case 6:
                Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new BlockOverlayGui(this.mod));
                break;
        }
    }

    public void mouseClickMove(final int mouseX, final int mouseY, final int clickedMouseButton, final long timeSinceLastClick) {
        this.mod.getSettings().setChromaSpeed(this.sliderChroma.getValueInt());
        this.mod.getSettings().setOverlayRed(this.sliderRed.getValueInt() / 255.0f);
        this.mod.getSettings().setOverlayGreen(this.sliderGreen.getValueInt() / 255.0f);
        this.mod.getSettings().setOverlayBlue(this.sliderBlue.getValueInt() / 255.0f);
        this.mod.getSettings().setOverlayAlpha(this.sliderAlpha.getValueInt() / 255.0f);
    }

    public void onGuiClosed() {
        this.mod.getSettings().save();
    }
}
