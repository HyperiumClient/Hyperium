/*
 *       Copyright (C) 2018-present Hyperium <https://hyperium.cc/>
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU Lesser General Public License as published
 *       by the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU Lesser General Public License for more details.
 *
 *       You should have received a copy of the GNU Lesser General Public License
 *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.gui.main.components;

import cc.hyperium.utils.HyperiumFontRenderer;
import net.minecraft.client.gui.Gui;

import java.awt.*;

/*
 * Created by Cubxity on 01/06/2018
 */
@Deprecated // Soon to be removed, please refrain from using.
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
        if (textY < (overlayH / 4) || (textY + h) > (overlayH / 4 * 3)) return false;

        if (mouseX >= overlayX && mouseX <= overlayX + w && mouseY >= overlayY && mouseY <= overlayY + h) {
            Gui.drawRect(overlayX, overlayY, overlayX + w, overlayY + h, 0x1e000000);
        }

        fr.drawString(label, overlayX + 4, (overlayY + (h - fr.FONT_HEIGHT) / 2f), enabled ? -1 : new Color(169, 169, 169).getRGB());
        return true;
    }

    public abstract void mouseClicked(int mouseX, int mouseY, int overlayX, int overlayY, int w, int h);

    public void handleMouseInput(int mouseX, int mouseY, int overlayX, int overlayY, int w, int h) {

    }

    public String getLabel() {
        return label;
    }
}
