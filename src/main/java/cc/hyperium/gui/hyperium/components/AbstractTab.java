package cc.hyperium.gui.hyperium.components;

import cc.hyperium.gui.hyperium.HyperiumMainGui;

/*
 * Created by Cubxity on 27/08/2018
 */
public abstract class AbstractTab {
    protected HyperiumMainGui gui;
    protected String title;

    public AbstractTab(HyperiumMainGui gui, String title) {
        this.gui = gui;
        this.title = title;
    }

    public abstract void render(int x, int y, int width, int height);

    public String getTitle() {
        return title;
    }
}
