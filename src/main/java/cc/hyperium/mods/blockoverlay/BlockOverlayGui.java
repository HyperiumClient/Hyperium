package cc.hyperium.mods.blockoverlay;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;

public class BlockOverlayGui extends GuiScreen {
    private GuiButton buttonMode;
    private GuiButton buttonSettings;

    public BlockOverlayGui() {
        super();
    }

    public void initGui() {
        super.buttonList.add(this.buttonMode = new GuiButton(0, super.width / 2 - 100, super.height / 2 - 15, "Mode: " + BlockOverlay.mode.name));
        super.buttonList.add(this.buttonSettings = new GuiButton(1, super.width / 2 - 100, super.height / 2 + 15, "Settings"));
    }

    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        super.drawDefaultBackground();
        GlStateManager.pushMatrix();
        GlStateManager.scale(1.2f, 1.2f, 1.2f);
        super.drawCenteredString(super.fontRendererObj, "Block Overlay", Math.round((float) (super.width / 2) / 1.2f), Math.round((float) (super.height / 2) / 1.2f) - 50, -1);
        GlStateManager.popMatrix();
        this.buttonSettings.enabled = (!BlockOverlay.mode.equals(BlockOverlayMode.NONE) && !BlockOverlay.mode.equals(BlockOverlayMode.DEFAULT));
        this.buttonMode.drawButton(super.mc, mouseX, mouseY);
        this.buttonSettings.drawButton(super.mc, mouseX, mouseY);
    }

    public void actionPerformed(final GuiButton button) {
        switch (button.id) {
            case 0: {
                BlockOverlay.mode = BlockOverlayMode.getNextMode(BlockOverlay.mode);
                this.buttonMode.displayString = "Mode: " + BlockOverlay.mode.name;
                break;
            }
            case 1: {
                Minecraft.getMinecraft().displayGuiScreen(new BlockOverlaySettings());
                break;
            }
        }
    }

    public void onGuiClosed() {
        BlockOverlay.saveConfig();
    }
}
