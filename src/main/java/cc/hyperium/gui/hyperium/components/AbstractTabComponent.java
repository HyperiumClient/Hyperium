package cc.hyperium.gui.hyperium.components;

import cc.hyperium.utils.SimpleAnimValue;
import net.minecraft.client.gui.Gui;

import java.awt.Color;
import java.util.List;

/*
 * Created by Cubxity on 27/08/2018
 */
public abstract class AbstractTabComponent {
    private SimpleAnimValue hoverAnim = new SimpleAnimValue(0L, 0f, 0f);

    protected List<String> tags;
    protected AbstractTab tab;
    private boolean fc = false; // search query cache
    private String sc = "";  // filter cache
    public boolean hover;

    /**
     * @param tab  the tab that this component will be added on
     * @param tags tags that are used for search function
     */
    AbstractTabComponent(AbstractTab tab, List<String> tags) {
        this.tab = tab;
        this.tags = tags;
    }

    public int getHeight() {
        return 20;
    }

    public void render(int x, int y, int width) {
        if (hover && hoverAnim.getValue() == 0f)
            hoverAnim = new SimpleAnimValue(1000L, hoverAnim.getValue(), 1f);
        else if (!hover && hoverAnim.getValue() == 1f)
            hoverAnim = new SimpleAnimValue(1000L, hoverAnim.getValue(), 0f);
        Gui.drawRect(x, y, x + width, y + 20, new Color(0, 0, 0, 10).getRGB()); //TODO: someone help
    }

    public boolean filter(String s) {
        boolean a = (s.equals(sc) ? fc : (fc = tags.stream().anyMatch(t -> t.contains(s))));
        sc = s;
        return a;
    }

    public void onClick(int x, int y) {

    }
}