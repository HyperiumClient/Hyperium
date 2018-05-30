package cc.hyperium.mods.blockoverlay;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.config.GuiSlider;

public class BlockOverlaySettings extends GuiScreen {
    private GuiButton buttonColor;
    private GuiButton buttonRender;
    private GuiButton buttonBack;
    private GuiSlider sliderWidth;

    public BlockOverlaySettings() {
        super();
    }

    public void initGui() {
        super.buttonList.add(this.sliderWidth = new GuiSlider(0, super.width / 2 - 100, super.height / 2 - 30, 200, 20, "Line width: ", "", 2.0f, 5.0f, BlockOverlay.lineWidth, true, true));
        super.buttonList.add(this.buttonColor = new GuiButton(1, super.width / 2 - 100, super.height / 2, "Color"));
        super.buttonList.add(this.buttonRender = new GuiButton(2, super.width / 2 - 100, super.height / 2 + 30, "Always render: " + (BlockOverlay.alwaysRender ? "On" : "Off")));
        super.buttonList.add(this.buttonBack = new GuiButton(3, super.width / 2 - 100, super.height / 2 + 80, "Back"));
    }

    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        super.drawDefaultBackground();
        this.sliderWidth.drawButton(super.mc, mouseX, mouseY);
        this.buttonColor.drawButton(super.mc, mouseX, mouseY);
        this.buttonRender.drawButton(super.mc, mouseX, mouseY);
        this.buttonBack.drawButton(super.mc, mouseX, mouseY);
    }

    public void actionPerformed(final GuiButton button) {
        switch (button.id) {
            case 0: {
                BlockOverlay.lineWidth = ((float) this.sliderWidth.getValue()) * 3.0f + 2.0f;
                break;
            }
            case 1: {
                Minecraft.getMinecraft().displayGuiScreen(new BlockOverlayColor());
                break;
            }
            case 2: {
                BlockOverlay.alwaysRender = !BlockOverlay.alwaysRender;
                this.buttonRender.displayString = "Always render: " + (BlockOverlay.alwaysRender ? "On" : "Off");
                break;
            }
            case 3: {
                Minecraft.getMinecraft().displayGuiScreen(new BlockOverlayGui());
                break;
            }
        }
    }

    public void mouseClickMove(final int mouseX, final int mouseY, final int clickedMouseButton, final long timeSinceLastClick) {
        BlockOverlay.lineWidth = ((float) this.sliderWidth.getValue()) * 3.0f + 2.0f;
    }

    public void onGuiClosed() {
        BlockOverlay.saveConfig();
    }
}