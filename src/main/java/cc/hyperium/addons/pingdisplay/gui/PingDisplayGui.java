package cc.hyperium.addons.pingdisplay.gui;

import cc.hyperium.Hyperium;
import cc.hyperium.addons.pingdisplay.PingDisplayAddon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

public class PingDisplayGui extends GuiScreen {

    private boolean isMouseDown;
    private int prevMouseX;
    private int prevMouseY;
    private GuiButton buttonPingDisplay;

    public void initGui() {
        this.buttonList.add(this.buttonPingDisplay = new GuiButton(0, super.width / 2 - 75, super.height / 2, 150, 20, "Show ping: " + (PingDisplayAddon.showPingDisplay ? "On" : "Off")));
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawDefaultBackground();
        PingDisplayAddon.render.drawPingDisplay(PingDisplayAddon.pingDisplayPosX, PingDisplayAddon.pingDisplayPosY, Minecraft.getMinecraft().getNetHandler().getPlayerInfo(Minecraft.getMinecraft().thePlayer.getGameProfile().getId()));
        GL11.glPushMatrix();
        GL11.glScalef(1.2f, 1.2f, 1.2f);
        this.drawCenteredString(super.fontRendererObj, "Ping Display", Math.round(super.width / 2 / 1.2f), Math.round(super.height / 2 / 1.2f) - 25, -1);
        GL11.glPopMatrix();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 0) {
            PingDisplayAddon.showPingDisplay = true;
            this.buttonPingDisplay.displayString = "Show ping: " + (PingDisplayAddon.showPingDisplay ? "On" : "Off");
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (PingDisplayAddon.showPingDisplay && PingDisplayAddon.pingDisplayPosX <= mouseX && mouseX <= PingDisplayAddon.pingDisplayPosX + 18 && PingDisplayAddon.pingDisplayPosY <= mouseY && mouseY <= PingDisplayAddon.pingDisplayPosY + 18) {
            this.isMouseDown = true;
            this.prevMouseX = mouseX;
            this.prevMouseY = mouseY;
        }
    }

    public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        if (this.isMouseDown) {
            PingDisplayAddon.pingDisplayPosX = ((PingDisplayAddon.pingDisplayPosX + mouseX - this.prevMouseX < 0) ? 0 : (PingDisplayAddon.pingDisplayPosX + mouseX - this.prevMouseX));
            PingDisplayAddon.pingDisplayPosY = ((PingDisplayAddon.pingDisplayPosY + mouseY - this.prevMouseY < 0) ? 0 : (PingDisplayAddon.pingDisplayPosY + mouseY - this.prevMouseY));
            if (PingDisplayAddon.pingDisplayPosX + 20 > super.width) {
                PingDisplayAddon.pingDisplayPosX = super.width - 20;
            }
            if (PingDisplayAddon.pingDisplayPosY + 20 > super.height) {
                PingDisplayAddon.pingDisplayPosY = super.height - 20;
            }
            this.prevMouseX = mouseX;
            this.prevMouseY = mouseY;
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        this.isMouseDown = false;
    }

    public void onGuiClosed() {
        Hyperium.CONFIG.save();
    }
}
