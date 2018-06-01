package cc.hyperium.gui.main.components;

import cc.hyperium.gui.main.HyperiumMainGui;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;

/*
 * Created by Cubxity on 01/06/2018
 */
public abstract class OverlayComponent {
    String label;

    public void render(int mouseX, int mouseY, int overlayX, int overlayY, int w, int h) {
        if (mouseX >= overlayX && mouseX <= overlayX + w && mouseY >= overlayY && mouseY <= overlayY + h)
            Gui.drawRect(overlayX, overlayY, overlayX + w, overlayY + h, new Color(0, 0, 0, 40).getRGB());
        float s = 0.8f;
        GlStateManager.scale(s, s, s);
        HyperiumMainGui.getFr().drawString(label, overlayX / s + 4, (overlayY + (h - HyperiumMainGui.getFr().FONT_HEIGHT * s) / 2) / s, 0xffffff);
        GlStateManager.scale(1.5f, 1.5f, 1.5f);
    }

    public abstract void handleMouseInput(int mouseX, int mouseY, int overlayX, int overlayY, int w, int h);
}
