package cc.hyperium.gui.main.components;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by Cubxity on 27/08/2018
 */
public class TabCollapsible extends AbstractTabComponent {
    private boolean collapsed = true;
    private List<AbstractTabComponent> childs = new ArrayList<>();

    public TabCollapsible(AbstractTab tab) {
        super(tab);
    }

    @Override
    public int getHeight() {
        return 20;
    }

    public List<AbstractTabComponent> getChilds() {
        return childs;
    }

    public void setCollapsed(boolean collapsed) {
        this.collapsed = collapsed;
    }

    @Override
    public void render(int x, int y, int width) {

    }
}
