package cc.hyperium.gui.main.components;

/*
 * Created by Conor on 04/06/2018
 */

import cc.hyperium.gui.HyperiumGui;
import cc.hyperium.gui.Icons;
import cc.hyperium.gui.main.HyperiumMainGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class WrappingSettingItem extends SettingItem {
    private Runnable onClick;
    private List<SubItem> subItems = new ArrayList<>();
    private ResourceLocation icon;
    private String title;
    private String desc;
    private String hover;
    private int xIndex, yIndex;

    public WrappingSettingItem(Runnable onClick, ResourceLocation icon, String title, String desc, String hover, int xIndex, int yIndex) {
        super(onClick, icon, title, desc, hover, xIndex, yIndex);
        this.onClick = onClick;
        this.icon = icon;
        this.title = title;
        this.desc = desc;
        this.hover = hover;
        this.xIndex = xIndex;
        this.yIndex = yIndex;
    }

    @Override
    public void render(int mouseX, int mouseY, int containerWidth, int containerHeight, int topX, int topY) {
        int w = containerWidth / 3;
        int h = containerHeight / 3;
        int blockX = topX + w * xIndex + w / 7;
        int blockY = topY + h * yIndex + h / 6;
        if (mouseX >= blockX && mouseX <= blockX + w / 7 * 5 && mouseY >= blockY && mouseY <= blockY + h / 6 * 4 && HyperiumMainGui.INSTANCE.getOverlay() == null) {
            System.out.println("In render: " + mouseX + " " + mouseY);
            HyperiumGui.drawChromaBox(blockX, blockY, blockX + w / 7 * 5, blockY + h / 6 * 4, 0.2f);
        } else {
            Gui.drawRect(blockX, blockY, blockX + w / 7 * 5, blockY + h / 6 * 4, new Color(0, 0, 0, 60).getRGB());
            GlStateManager.shadeModel(7424); // for opening from main menu
            GlStateManager.disableBlend();
            GlStateManager.enableAlpha();
            GlStateManager.enableTexture2D();
        }
        if (Mouse.isButtonDown(0)) {
            if (mouseX >= blockX && mouseX <= blockX + w / 7 * 5 && mouseY >= blockY && mouseY <= blockY + h / 6 * 4)
                onClick.run();
        }
        HyperiumMainGui.getFr().drawString(title, blockX + 3, blockY + 3, 0xffffff);
        float s = 0.8f;
        GlStateManager.scale(s, s, s);
            if (desc.length() > 20) {
                HyperiumMainGui.getFr().drawString(desc.substring(0, 20), (blockX + 3) / s, (blockY + 15) / s, new Color(160, 160, 160).getRGB());
                if(String.valueOf(desc.substring(20, 21).charAt(0)) == " ") {
                    HyperiumMainGui.getFr().drawString(desc.substring(20, desc.length()).replaceFirst(" ", ""), (blockX + 3) / s, (blockY + 23) / s, new Color(160, 160, 160).getRGB());
                } else {
                    HyperiumMainGui.getFr().drawString(desc.substring(20, desc.length()), (blockX + 3) / s, (blockY + 23) / s, new Color(160, 160, 160).getRGB());
                }
            } else {
                HyperiumMainGui.getFr().drawString(desc, (blockX + 3) / s, (blockY + 15) / s, new Color(160, 160, 160).getRGB());
            }
        GlStateManager.scale(1.25f, 1.25f, 1.25f);
        if (icon != null) {
            Minecraft.getMinecraft().getTextureManager().bindTexture(icon);
            Gui.drawScaledCustomSizeModalRect(blockX + w / 7 * 5 - 25, blockY, 0, 0, 144, 144, 25, 25, 144, 144);
        }

        Icons.INFO.bind();
        Gui.drawScaledCustomSizeModalRect(blockX + w / 7 * 5 - 10, blockY + h / 6 * 4 - 10, 0, 0, 144, 144, 10, 10, 144, 144);
        if (mouseX >= blockX + w / 7 * 5 - 10 && mouseX <= blockX + w / 7 * 5 && mouseY >= blockY + h / 7 * 5 - 10 && mouseY < blockY + h / 7 * 5 && HyperiumMainGui.INSTANCE.getOverlay() == null) {
            HyperiumGui.drawChromaBox(mouseX + 10, mouseY, (int) (mouseX + 10 + HyperiumMainGui.getFr().getWidth(hover) * s + 3), (int) (mouseY + HyperiumMainGui.getFr().FONT_HEIGHT * s + 3), 0.2f);
            GlStateManager.scale(s, s, s);
            HyperiumMainGui.getFr().drawString(hover, (mouseX + 10) / s + 1, mouseY / s + 1, 0xffffff);
            GlStateManager.scale(1.25f, 1.25f, 1.25f);
        }
    }
}
