package cc.hyperium.gui.main;

import cc.hyperium.gui.HyperiumGui;
import cc.hyperium.gui.main.components.AbstractTab;
import cc.hyperium.gui.main.tabs.HomeTab;
import cc.hyperium.gui.main.tabs.ModsTab;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/*
 * Created by Cubxity on 20/05/2018
 */
public class HyperiumMainGui extends HyperiumGui {
    private static AbstractTab currentTab = null; // static so it is still the same tab

    private List<AbstractTab> tabs;

    @Override
    protected void pack() {
        int pw = width / 15;
        if (pw > 144)
            pw = 144; // icon res
        AbstractTab ht = new HomeTab(height / 2 - (pw * 2), pw);
        if (currentTab == null)
            currentTab = ht; // Home tab should be selected one by default
        tabs = Arrays.asList(
                ht,
                new ModsTab(height / 2 - pw, pw)
        );
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();

        // Draws side pane
        currentTab.drawHighlight();
        tabs.forEach(AbstractTab::drawTabIcon);

        int pw = width / 15;
        if (pw > 144)
            pw = 144; // icon res

        drawRect(pw * 2, pw, width - pw * 2, height - pw, new Color(0, 0, 0, 70).getRGB());

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        tabs.stream().filter(t -> t.getBlock().isMouseOver(mouseX, mouseY)).findFirst().ifPresent(
                t -> currentTab = t);
    }
}
