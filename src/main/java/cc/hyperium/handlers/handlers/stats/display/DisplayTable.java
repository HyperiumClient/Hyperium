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

package cc.hyperium.handlers.handlers.stats.display;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class DisplayTable extends StatsDisplayItem {
    private String[][] rows;
    private int[] rowSpacing;

    public DisplayTable(String[]... rows) {
        this.rows = rows;
        build();
    }

    public DisplayTable(List<String[]> strings) {
        rows = strings.toArray(new String[0][]);
        build();
    }

    private void build() {
        int columns = rows[0].length;
        rowSpacing = new int[columns];

        /*
       4 3 2
       3 2 1
         */
        Arrays.stream(rows).forEach(row -> {
            for (int i = 0; i < columns; i++) {
                rowSpacing[i] =
                    Math.max(rowSpacing[i], Minecraft.getMinecraft().fontRendererObj.getStringWidth(row[i]) + 15);
            }
        });
        Arrays.stream(rowSpacing).forEach(i -> width += i);
        height = 11 * rows.length;
    }

    @Override
    public void draw(int x, int y) {
        boolean first = true;
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 0);

        for (String[] row : rows) {
            FontRenderer fontRendererObj = Minecraft.getMinecraft().fontRendererObj;
            GlStateManager.pushMatrix();

            for (int i = 0; i < row.length; i++) {
                String tmp = row[i];
                fontRendererObj.drawString((first ? EnumChatFormatting.BOLD : "") + tmp, 0, 0, Color.WHITE.getRGB());
                GlStateManager.translate(rowSpacing[i], 0, 0);
            }

            GlStateManager.popMatrix();
            GlStateManager.translate(0, 11, 0);
            first = false;
        }

        GlStateManager.popMatrix();
    }
}
