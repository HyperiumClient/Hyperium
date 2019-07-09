package cc.hyperium.handlers.handlers.stats.display;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.Color;

public class DisplayLine extends StatsDisplayItem {
    private String value;
    private int color;
    private int scale;

    public DisplayLine(String value) {
        this(value, Color.WHITE.getRGB());
    }

    public DisplayLine(String value, int color) {
        this(value, color, 1);
    }

    public DisplayLine(String value, int color, int scale) {
        this.value = value;
        this.color = color;
        width = Minecraft.getMinecraft().fontRendererObj.getStringWidth(value) * scale;
        height = 10 * scale;
        this.scale = scale;
    }


    @Override
    public void draw(int x, int y) {
        GlStateManager.scale(scale, scale, scale);
        Minecraft.getMinecraft().fontRendererObj.drawString(value, x / scale, y / scale, color);
        GlStateManager.scale(1 / scale, 1 / scale, 1 / scale);
    }
}
