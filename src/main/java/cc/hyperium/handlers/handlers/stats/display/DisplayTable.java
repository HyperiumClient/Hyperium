package cc.hyperium.handlers.handlers.stats.display;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;

import java.awt.Color;
import java.util.List;

public class DisplayTable extends StatsDisplayItem {
    private String[][] rows;
    private int[] rowSpacing;

    public DisplayTable(String[]... rows) {
        this.rows = rows;
        build();
    }

    public DisplayTable(List<String[]> strings) {
        String[][] data = new String[strings.size()][];
        for (int i = 0; i < strings.size(); i++) {
            data[i] = strings.get(i);
        }
        this.rows = data;
        build();
    }

    private void build() {
        int columns = rows[0].length;
        rowSpacing = new int[columns];

        /*
       4 3 2
       3 2 1
         */
        for (String[] row : rows) {
            for (int i = 0; i < columns; i++) {
                rowSpacing[i] = Math.max(rowSpacing[i], Minecraft.getMinecraft().fontRendererObj.getStringWidth(row[i]) + 15);
            }
        }
        for (int i : rowSpacing) {
            width += i;
        }
        this.height = 11 * rows.length;
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
