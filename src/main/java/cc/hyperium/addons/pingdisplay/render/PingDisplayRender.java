package cc.hyperium.addons.pingdisplay.render;

import cc.hyperium.addons.pingdisplay.PingDisplayAddon;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.RenderHUDEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class PingDisplayRender extends Gui {

    private ResourceLocation icons;

    public PingDisplayRender() {
        this.icons = new ResourceLocation("textures/gui/icons.png");
    }

    @InvokeEvent
    public void onRenderGameOverlay(RenderHUDEvent event) {
        if (Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().theWorld != null && Minecraft.getMinecraft().currentScreen == null) {
            this.drawPingDisplay(PingDisplayAddon.pingDisplayPosX, PingDisplayAddon.pingDisplayPosY, Minecraft.getMinecraft().getNetHandler().getPlayerInfo(Minecraft.getMinecraft().thePlayer.getGameProfile().getId()));
        }
    }

    public void drawPingDisplay(int x, int y, NetworkPlayerInfo networkPlayerInfo) {
        if (PingDisplayAddon.showPingDisplay) {
            Gui.drawRect(PingDisplayAddon.pingDisplayPosX, PingDisplayAddon.pingDisplayPosY, PingDisplayAddon.pingDisplayPosX + 21, PingDisplayAddon.pingDisplayPosY + 21, 1694498816);
            GlStateManager.pushMatrix();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            Minecraft.getMinecraft().getTextureManager().bindTexture(this.icons);
            int yOffset = 4;
            if (networkPlayerInfo != null) {
                if (networkPlayerInfo.getResponseTime() < 0) {
                    yOffset = 5;
                } else if (networkPlayerInfo.getResponseTime() < 150) {
                    yOffset = 0;
                } else if (networkPlayerInfo.getResponseTime() < 300) {
                    yOffset = 1;
                } else if (networkPlayerInfo.getResponseTime() < 600) {
                    yOffset = 2;
                } else if (networkPlayerInfo.getResponseTime() < 1000) {
                    yOffset = 3;
                }
            }
            super.drawTexturedModalRect(x + 6, y + 3, 0, 176 + yOffset * 8, 10, 8);
            GlStateManager.scale(0.6f, 0.6f, 0.6f);
            super.drawCenteredString(Minecraft.getMinecraft().fontRendererObj, (networkPlayerInfo == null) ? "0" : (networkPlayerInfo.getResponseTime() + " ms"), Math.round(PingDisplayAddon.pingDisplayPosX / 0.6f) + 17, Math.round(PingDisplayAddon.pingDisplayPosY / 0.6f) + 22, -1);
            GlStateManager.popMatrix();
        }
    }
}
