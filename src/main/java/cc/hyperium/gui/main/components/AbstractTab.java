package cc.hyperium.gui.main.components;

import cc.hyperium.gui.GuiBlock;
import cc.hyperium.gui.font.Fonts;
import cc.hyperium.utils.HyperiumFontRenderer;

/*
 * Created by Cubxity on 20/05/2018
 */
public abstract class AbstractTab {

    protected static HyperiumFontRenderer fr = Fonts.ARIAL.getTrueTypeFont();

    public abstract void drawTabIcon();

    public abstract GuiBlock getBlock();

    public abstract void drawHighlight();

    public void handleMouseInput(){}

    public void draw(int mouseX, int mouseY, int topX, int topY, int containerWidth, int containerHeight){}
}
