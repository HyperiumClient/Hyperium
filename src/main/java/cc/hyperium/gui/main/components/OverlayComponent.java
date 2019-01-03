package cc.hyperium.gui.main.components;

import cc.hyperium.utils.HyperiumFontRenderer;
import net.minecraft.client.gui.Gui;

import java.awt.Color;
import java.awt.Font;

/*
 * Created by Cubxity on 01/06/2018
 */
public abstract class OverlayComponent {
    private static final HyperiumFontRenderer fr = new HyperiumFontRenderer("Arial", Font.PLAIN, 20);

    String label;
    boolean enabled;

    public OverlayComponent(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Renders this component
     *
     * @param mouseX   the current X position of the mouse
     * @param mouseY   the current Y position of the mouse
     * @param overlayX the current X of the overlay
     * @param overlayY the current Y of the overlay
     * @param w        the width of this item
     * @param h        the height of this item
     * @param overlayH the height of the overlay
     * @return if the item got rendered
     */
    public boolean render(int mouseX, int mouseY, int overlayX, int overlayY, int w, int h, int overlayH) {
        int textY = (overlayY + (h - fr.FONT_HEIGHT) / 2);
        if (textY < (overlayH / 4)) {
            return false;
        } else if ((textY + h) > (overlayH / 4 * 3)) {
            return false;
        }
        if (mouseX >= overlayX && mouseX <= overlayX + w && mouseY >= overlayY && mouseY <= overlayY + h) {
            Gui.drawRect(overlayX, overlayY, overlayX + w, overlayY + h, 0x1e000000);
        }
        if (enabled) {
            fr.drawString(label, overlayX + 4,
                (overlayY + (h - fr.FONT_HEIGHT) / 2), 0xffffff);
        } else {
            fr.drawString(label, overlayX + 4,
                (overlayY + (h - fr.FONT_HEIGHT) / 2), new Color(169, 169, 169).getRGB());
        }
        return true;
    }

    public abstract void mouseClicked(int mouseX, int mouseY, int overlayX, int overlayY, int w, int h);

    public void handleMouseInput(int mouseX, int mouseY, int overlayX, int overlayY, int w, int h) {

    }

    public String getLabel() {
        return this.label;
    }
}
