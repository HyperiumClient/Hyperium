package cc.hyperium.gui.main.components;

import cc.hyperium.gui.GuiBlock;
import cc.hyperium.gui.main.HyperiumMainGui;
import cc.hyperium.utils.HyperiumFontRenderer;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by Cubxity on 20/05/2018
 */
public abstract class AbstractTab {

    protected static HyperiumFontRenderer fr = HyperiumMainGui.getFr();

    protected List<SettingItem> items = new ArrayList<>();

    public abstract void drawTabIcon();

    public abstract GuiBlock getBlock();

    public abstract void drawHighlight();

    public void handleMouseInput() {
    }

    public void draw(int mouseX, int mouseY, int topX, int topY, int containerWidth, int containerHeight) {
        items.forEach(i -> i.render(mouseX, mouseY, containerWidth, containerHeight, topX, topY));
    }
}
