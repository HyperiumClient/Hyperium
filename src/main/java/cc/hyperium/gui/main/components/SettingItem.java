package cc.hyperium.gui.main.components;

import cc.hyperium.gui.HyperiumMainMenu;
import cc.hyperium.gui.main.HyperiumMainGui;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/*
 * Created by Cubxity on 29/05/2018
 */
public class SettingItem {
    private Runnable onClick;
    private List<SubItem> subItems = new ArrayList<>();
    private ResourceLocation icon;
    private String title;
    private String desc;
    private String hover;
    private int xIndex, yIndex;

    public SettingItem(Runnable onClick, ResourceLocation icon, String title, String desc, String hover, int xIndex, int yIndex) {
        this.onClick = onClick;
        this.icon = icon;
        this.title = title;
        this.desc = desc;
        this.hover = hover;
        this.xIndex = xIndex;
        this.yIndex = yIndex;
    }

    public List<SubItem> getSubItems() {
        return subItems;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setHover(String hover) {
        this.hover = hover;
    }

    public void handleMouseInput(int mouseX, int mouseY) {

    }

    public void render(int mouseX, int mouseY, int containerWidth, int containerHeight, int topX, int topY) {
        int w = containerWidth / 4;
        int h = containerHeight / 4;
        int blockX = topX + w * xIndex + w / 7;
        int blockY = topY + h * yIndex + h / 6;
        Gui.drawRect(blockX, blockY, blockX + w / 7 * 5, blockY + h / 6 * 4, new Color(0, 0, 0, 120).getRGB());
        HyperiumMainGui.getFr().drawString(title, blockX + 3, blockY + 3, 0xffffff);
        float s = 0.8f;
        GlStateManager.scale(s, s, s);
        HyperiumMainGui.getFr().drawString(desc, (blockX + 3) / s, (blockY + 15) / s, new Color(120, 120, 120).getRGB());
        GlStateManager.scale(1.25f, 1.25f, 1.25f);
    }

    public static class SubItem {
        private String label;
        private boolean toggle;

        public SubItem(String label) {
            this.label = label;
            toggle = false;
        }

        public boolean isToggled() {
            return toggle;
        }

        public void setToggle(boolean toggle) {
            this.toggle = toggle;
        }
    }
}
