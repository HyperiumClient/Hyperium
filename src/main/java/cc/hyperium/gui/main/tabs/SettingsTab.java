package cc.hyperium.gui.main.tabs;

import cc.hyperium.Hyperium;
import cc.hyperium.gui.GuiBlock;
import cc.hyperium.gui.main.components.AbstractTab;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

/*
 * Created by Cubxity on 20/05/2018
 */
public class SettingsTab extends AbstractTab {
    private static DynamicTexture ico;

    static {
        try {
            ico = new DynamicTexture(ImageIO.read(Hyperium.class.getResourceAsStream("/assets/minecraft/textures/material/settings.png")));
            ico.loadTexture(Minecraft.getMinecraft().getResourceManager());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private GuiBlock block;
    private int y, w;

    public SettingsTab(int y, int w) {
        block = new GuiBlock(0, w, y, y + w);
        this.y = y;
        this.w = w;
    }

    @Override
    public void drawTabIcon() {
//        GlStateManager.bindTexture(ico.getGlTextureId());
//        Gui.drawScaledCustomSizeModalRect(5, y + 5, 0, 0, 144, 144, w - 10, w - 10, 144, 144);
    }

    @Override
    public GuiBlock getBlock() {
        return block;
    }

    @Override
    public void drawHighlight() {
        Gui.drawRect(0, y, w, y + w, new Color(0, 0, 0, 20).getRGB());
    }
}
