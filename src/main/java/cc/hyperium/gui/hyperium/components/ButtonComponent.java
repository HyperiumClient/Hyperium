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

package cc.hyperium.gui.hyperium.components;

import cc.hyperium.utils.HyperiumFontRenderer;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

/*
 * Created by Sk1er on today (It will be right for a little bit)
 */
public class ButtonComponent extends AbstractTabComponent {
    private String label;
    private List<String> lines = new ArrayList<>();
    private Runnable callback;

    public ButtonComponent(AbstractTab tab, List<String> tags, String label, Runnable callback) {
        super(tab, tags);
        tag(label);
        this.label = label;
        this.callback = callback;
    }

    @Override
    public void render(int x, int y, int width, int mouseX, int mouseY) {
        HyperiumFontRenderer font = tab.gui.getFont();
        lines.clear();
        lines = font.splitString(label, (width + 25) / 2); //16 for icon, 3 for render offset and then some more

        GlStateManager.pushMatrix();
        if (hover) Gui.drawRect(x, y, x + width, y + 18 * lines.size(), 0xa0000000);
        GlStateManager.popMatrix();

        int line1 = 0;
        for (String line : lines) {
            font.drawString(line.toUpperCase(), x + 3, y + 5 + 17 * line1, 0xffffff);
            line1++;
        }
    }

    @Override
    public int getHeight() {
        return 18 * lines.size();
    }


    @Override
    public void onClick(int x, int y) {
        if (y < 18 * lines.size()) callback.run();
    }

    public String getLabel() {
        return label;
    }
}
