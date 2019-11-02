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
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/*
 * Created by Sk1er on today (It will be right for a little bit)
 */
public class SelectorComponent extends AbstractTabComponent {
    private final String label;
    private List<String> lines = new ArrayList<>();
    private Field field;
    private Object parentObj;
    private Supplier<String[]> values;

    public SelectorComponent(AbstractTab tab, List<String> tags, String label, Field field, Object parentObj, Supplier<String[]> values) {
        super(tab, tags);
        tag(label);
        this.label = label;
        this.field = field;
        this.parentObj = parentObj;
        this.values = values;
    }

    public String getCurrentValue() {
        try {
            String s = (String) field.get(parentObj);
            String[] strings = values.get();
            for (String s1 : strings) {
                if (s1.equalsIgnoreCase(s)) {
                    return s;
                }
            }

            //Not found
            if (strings.length == 0) return "Error 4";
            String string = strings[0];
            setValue(string);
            return string;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return "Error 1";
    }

    public int getCurrentIndex() {
        String[] strings = values.get();
        for (int i = 0; i < strings.length; i++) {
            if (strings[i].equalsIgnoreCase(getCurrentValue())) {
                return i;
            }
        }

        return 0;
    }

    @Override
    public void render(int x, int y, int width, int mouseX, int mouseY) {
        HyperiumFontRenderer font = tab.gui.getFont();
        lines.clear();
        lines = font.splitString(label, (int) (width - font.getWidth(getCurrentValue()))); //16 for icon, 3 for render offset and then some more

        GlStateManager.pushMatrix();
        if (hover) Gui.drawRect(x, y, x + width, y + 18 * lines.size(), 0xa0000000);
        GlStateManager.popMatrix();

        int line1 = 0;
        for (String line : lines) {
            font.drawString(line.replaceAll("_", " ").toUpperCase(), x + 3, y + 5 + 17 * line1, 0xffffff);
            line1++;
        }

        int farSide = x + width;
        String val = getCurrentValue();
        float statX = farSide - 5 - font.getWidth(val);
        font.drawString(val, statX, y + 5, Color.WHITE.getRGB());

    }

    @Override
    public int getHeight() {
        return 18 * lines.size();
    }

    public void setValue(String newvalue) {
        try {
            field.set(parentObj, newvalue);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(int x, int y) {
        if (y < 18 * lines.size()) {
            String currentValue = getCurrentValue();
            int currentIndex = getCurrentIndex();
            String[] strings = values.get();
            if (strings.length == 0) setValue("Error 2");
            currentIndex++;
            if (currentIndex > strings.length - 1) currentIndex = 0;
            String string = strings[currentIndex];
            setValue(string);
            if (!getCurrentValue().equalsIgnoreCase(currentValue)) stateChange(string);
        }
    }

    public String getLabel() {
        return label;
    }
}
