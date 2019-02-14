package cc.hyperium.gui;

import cc.hyperium.Hyperium;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;

public class GuiIngameMultiplayer extends GuiMultiplayer {

    public GuiIngameMultiplayer(GuiScreen parentScreen) {
        super(parentScreen);
    }

    /*
           Ported by KodingKing
           Original mod by Canelex
    */

    public void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 1 || button.id == 4) {
            dc();
        }
        super.actionPerformed(button);
    }

    public void connectToSelected() {
        dc();
        super.connectToSelected();
    }

    private void dc() {
        if (Minecraft.getMinecraft().theWorld != null) {
            Minecraft.getMinecraft().theWorld.sendQuittingDisconnectingPacket();
            Minecraft.getMinecraft().loadWorld(null);
            mc.displayGuiScreen(null);
        }
    }

    /*
        End port
     */
}
