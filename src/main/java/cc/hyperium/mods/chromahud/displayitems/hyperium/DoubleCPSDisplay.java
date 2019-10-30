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

package cc.hyperium.mods.chromahud.displayitems.hyperium;

import cc.hyperium.mods.chromahud.ElementRenderer;
import cc.hyperium.mods.chromahud.api.DisplayItem;
import cc.hyperium.utils.JsonHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import java.util.ArrayList;
import java.util.List;

public class DoubleCPSDisplay extends DisplayItem {

    private final FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;

    public DoubleCPSDisplay(JsonHolder data, int ordinal) {
        super(data, ordinal);
    }

    @Override
    public void draw(int x, double y, boolean config) {
        List<String> list = new ArrayList<>();
        int leftCps = ElementRenderer.getCPS();
        int rightCps = ElementRenderer.getRightCPS();
        list.add("CPS:");
        list.add("Left CPS: " + leftCps);
        list.add("Right CPS: " + rightCps);
        list.add("Total CPS: " + (leftCps + rightCps));
        height = fr.FONT_HEIGHT * list.size();
        width = list.stream().mapToInt(fr::getStringWidth).filter(line -> line >= 0).max().orElse(0);
        ElementRenderer.draw(x, y, list);
    }
}
