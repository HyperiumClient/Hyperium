package cc.hyperium.gui.hyperium.components;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/*
 * Created by Cubxity on 27/08/2018
 */
public class CollapsibleTabComponent extends AbstractTabComponent {
    private List<AbstractTabComponent> childs = new ArrayList<>();
    private boolean collapsed = true;
    private String title;

    public CollapsibleTabComponent(AbstractTab tab, String title) {
        super(tab);
        this.title = title;
    }

    @Override
    public void render(int x, int y, int width) {
        tab.gui.getFont().drawString(title, x + 3, y + 3, 0xffffff);
    }

    @Override
    public int getHeight() {
        if (collapsed)
            return 20;
        else
            return 20 + childs.stream().flatMapToInt(c -> IntStream.of(c.getHeight())).sum();
    }

    public void setCollapsed(boolean collapsed) {
        this.collapsed = collapsed;
    }

    public List<AbstractTabComponent> getChilds() {
        return childs;
    }


}
