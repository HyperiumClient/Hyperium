package cc.hyperium.gui.main.components;

import cc.hyperium.gui.main.HyperiumMainGui;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

/*
 * Created by Cubxity on 01/06/2018
 */
public abstract class OverlayComponent {
    String label;

    public void render(int mouseX, int mouseY, int overlayX, int overlayY, int w, int h) {
        if (mouseX >= overlayX && mouseX <= overlayX + w && mouseY >= overlayY && mouseY <= overlayY + h)
            Gui.drawRect(overlayX, overlayY, overlayX + w, overlayY + h, 0x1e000000);
        HyperiumMainGui.getFr().drawString(label, overlayX + 4, (overlayY + (h - HyperiumMainGui.getFr().FONT_HEIGHT) / 2), 0xffffff);
    }

    public abstract void handleMouseInput(int mouseX, int mouseY, int overlayX, int overlayY, int w, int h);
}
