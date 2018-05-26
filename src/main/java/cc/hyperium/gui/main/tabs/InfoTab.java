package cc.hyperium.gui.main.tabs;

import cc.hyperium.gui.GuiBlock;
import cc.hyperium.gui.main.components.AbstractTab;

/*
 * Created by Cubxity on 23/05/2018
 */
public class InfoTab extends AbstractTab {

    private GuiBlock block;
    private int y, w;

    public InfoTab(int y, int w) {
        block = new GuiBlock(0, w, y, y + w);
        this.y = y;
        this.w = w;
    }

    @Override
    public void drawTabIcon() {

    }

    @Override
    public GuiBlock getBlock() {
        return block;
    }

    @Override
    public void drawHighlight() {

    }

    @Override
    public void draw(int mouseX, int mouseY, int topX, int topY, int containerWidth, int containerHeight) {
        super.draw(mouseX, mouseY, topX, topY, containerWidth, containerHeight);

    }
}
