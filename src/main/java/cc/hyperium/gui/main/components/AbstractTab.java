package cc.hyperium.gui.main.components;

import cc.hyperium.gui.GuiBlock;

/*
 * Created by Cubxity on 20/05/2018
 */
public abstract class AbstractTab {

    public abstract void drawTabIcon();

    public abstract GuiBlock getBlock();

    public abstract void drawHighlight();
}
