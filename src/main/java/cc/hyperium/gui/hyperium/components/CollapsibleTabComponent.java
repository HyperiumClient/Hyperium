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
    private String label;

    public CollapsibleTabComponent(AbstractTab tab, List<String> tags, String label) {
        super(tab, tags);
        this.label = label;
    }

    @Override
    public void render(int x, int y, int width) {
        super.render(x, y, width);
        tab.gui.getFont().drawString(label.toUpperCase(), x + 3, y + 4, 0xffffff);
    }

    @Override
    public int getHeight() {
        if (collapsed)
            return 20;
        else
            return 20 + childs.stream().flatMapToInt(c -> IntStream.of(c.getHeight())).sum();
    }

    public void addChild(AbstractTabComponent component) {
        childs.add(component);
        tags.addAll(component.tags);
    }

    @Override
    public void onClick(int x, int y) {
        if (y < 20) {
            collapsed = !collapsed;
            System.out.println("toggled");
        }
    }
}
