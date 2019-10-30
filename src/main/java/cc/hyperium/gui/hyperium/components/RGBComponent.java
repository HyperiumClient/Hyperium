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

import cc.hyperium.gui.hyperium.RGBFieldSet;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.Color;
import java.lang.reflect.Field;
import java.util.Collections;

/*
 * Created by Sk1er on today (It will be right for a little bit)
 */
public class RGBComponent extends AbstractTabComponent {

    private RGBFieldSet fieldSet;

    public RGBComponent(AbstractTab tab, RGBFieldSet fieldSet) {
        super(tab, Collections.emptyList());
        this.fieldSet = fieldSet;
    }

    @Override
    public void render(int x, int y, int width, int mouseX, int mouseY) {
        Color color = new Color(get(fieldSet.getRed()), get(fieldSet.getGreen()), get(fieldSet.getBlue()));
        int rgb = color.getRGB();
        GlStateManager.pushMatrix();
        Gui.drawRect(x, y, x + width - 5, y + 18, rgb);
        GlStateManager.popMatrix();
    }

    private int get(Field field) {
        try {
            return field.getInt(fieldSet.getParentObj());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return 255;
    }

    @Override
    public int getHeight() {
        return 18;
    }


    @Override
    public void onClick(int x, int y) {
    }
}
