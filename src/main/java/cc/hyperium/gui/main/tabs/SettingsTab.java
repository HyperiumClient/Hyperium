package cc.hyperium.gui.main.tabs;

import cc.hyperium.gui.GuiBlock;
import cc.hyperium.gui.main.components.AbstractTab;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

/*
 * Created by Cubxity on 20/05/2018
 */
public class SettingsTab extends AbstractTab {
    private static final ResourceLocation ico = new ResourceLocation("textures/material/settings.png");

    private GuiBlock block;
    private int y, w;

    public SettingsTab(int y, int w) {
        block = new GuiBlock(0, w, y, y + w);
        this.y = y;
        this.w = w;
    }

    @Override
    public void drawTabIcon() {
        Minecraft.getMinecraft().getTextureManager().bindTexture(ico);
        Gui.drawScaledCustomSizeModalRect(5, y + 5, 0, 0, 144, 144, w - 10, w - 10, 144, 144);
    }

    @Override
    public GuiBlock getBlock() {
        return block;
    }

    @Override
    public void drawHighlight() {
        Gui.drawRect(0, y, 3, y + w, 0xffffff);
    }

    @Override
    public void draw(int mouseX, int mouseY, int topX, int topY, int containerWidth, int containerHeight) {

    }
}
