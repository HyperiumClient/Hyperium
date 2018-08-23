package cc.hyperium.handlers.handlers.stats.display;

import net.minecraft.client.Minecraft;

public class DisplayLine extends StatsDisplayItem {
    private String value;
    private int color;


    public DisplayLine(String value, int color) {
        this.value = value;
        this.color = color;
        width = Minecraft.getMinecraft().fontRendererObj.getStringWidth(value);
        height = 10;
    }

    @Override
    public void draw(int x, int y) {
        Minecraft.getMinecraft().fontRendererObj.drawString(value, x, y, color);
    }
}
