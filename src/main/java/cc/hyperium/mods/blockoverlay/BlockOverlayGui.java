package cc.hyperium.mods.blockoverlay;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.client.config.GuiSlider;

public class BlockOverlayGui extends GuiScreen {
    private BlockOverlay mod;
    private GuiButton buttonMode;
    private GuiButton buttonColor;
    private GuiSlider sliderWidth;

    public BlockOverlayGui(BlockOverlay mod) {
        this.mod = mod;
    }

    public void initGui() {
        super.buttonList.add(this.buttonMode = new GuiButton(0, super.width / 2 - 50, super.height / 2 - 35, 100, 20, "Mode: " + this.mod.getSettings().getOverlayMode().getName()));
        super.buttonList.add(this.buttonColor = new GuiButton(1, super.width / 2 - 50, super.height / 2 - 10, 100, 20, "Color"));
        super.buttonList.add(this.sliderWidth = new GuiSlider(2, super.width / 2 - 50, super.height / 2 + 15, 100, 20, "Width: ", "", 0.0f, 5.0f, this.mod.getSettings().getLineWidth(), false, true));
    }

    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        super.drawDefaultBackground();
        GlStateManager.pushMatrix();
        GlStateManager.scale(1.2f, 1.2f, 0.0f);
        super.drawCenteredString(super.fontRendererObj, "Block Overlay", Math.round(super.width / 2 / 1.2f), Math.round(super.height / 2 / 1.2f) - 50, -1);
        GlStateManager.popMatrix();
        this.buttonMode.drawButton(super.mc, mouseX, mouseY);
        this.buttonColor.drawButton(super.mc, mouseX, mouseY);
        this.sliderWidth.drawButton(super.mc, mouseX, mouseY);
    }

    public void actionPerformed(final GuiButton button) {
        switch (button.id) {
            case 0:
                this.mod.getSettings().setOverlayMode(BlockOverlayMode.getNextMode(this.mod.getSettings().getOverlayMode()));
                this.buttonMode.displayString = "Mode: " + this.mod.getSettings().getOverlayMode().getName();
                break;
            case 1:
                BlockOverlay.mc.displayGuiScreen(new BlockOverlayColor(this.mod));
                break;
            case 2:
                this.mod.getSettings().setLineWidth((float) this.sliderWidth.getValue());
                break;
        }
    }

    public void mouseClickMove(final int mouseX, final int mouseY, final int clickedMouseButton, final long timeSinceLastClick) {
        this.mod.getSettings().setLineWidth((float) this.sliderWidth.getValue());
    }

    public void onGuiClosed() {
        this.mod.getSettings().save();
    }
}
