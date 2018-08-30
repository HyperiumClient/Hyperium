package cc.hyperium.gui.hyperium;

import cc.hyperium.config.Settings;
import cc.hyperium.gui.HyperiumGui;
import cc.hyperium.gui.hyperium.components.AbstractTab;
import cc.hyperium.utils.HyperiumFontRenderer;

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

/*
 * Created by Cubxity on 27/08/2018
 */
public class HyperiumMainGui extends HyperiumGui {
    private static int tabIndex = 0; // save tab position

    private HyperiumFontRenderer font = new HyperiumFontRenderer(Settings.GUI_FONT, Font.PLAIN, 14);
    private HyperiumFontRenderer title = new HyperiumFontRenderer(Settings.GUI_FONT, Font.PLAIN, 20);
    private List<AbstractTab> tabs = new ArrayList<>();
    private AbstractTab currentTab;

    public HyperiumMainGui() {
        setTab(tabIndex);
    }

    @Override
    protected void pack() {

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int yg = height / 10;  // Y grid
        int xg = width / 10;   // X grid
        // Draw STuFf
        super.drawScreen(mouseX, mouseY, partialTicks);

        // Header
        drawRect(xg, yg, xg * 8, yg, 0x64000000);
        title.drawCenteredString(currentTab.getTitle(), width / 2, yg + (yg / 2 - 5), 0xffffff);

        // Body
        currentTab.render(xg, yg, xg * 8, yg * 8);
    }

    public HyperiumFontRenderer getFont() {
        return font;
    }

    public HyperiumFontRenderer getTitle() {
        return title;
    }

    public void setTab(int i) {
        tabIndex = i;
        currentTab = tabs.get(i);
    }
}
