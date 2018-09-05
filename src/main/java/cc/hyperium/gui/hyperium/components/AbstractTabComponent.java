package cc.hyperium.gui.hyperium.components;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

import java.util.List;

/*
 * Created by Cubxity on 27/08/2018
 */
public abstract class AbstractTabComponent {
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
        return 18;
    }

    public void render(int x, int y, int width, int mouseX, int mouseY) {
        GlStateManager.pushMatrix();
        if (hover)
            Gui.drawRect(x, y, x + width, y + 18,  0xa0000000);
        GlStateManager.popMatrix();
    }

    public boolean filter(String s) {
        boolean a = (s.equals(sc) ? fc : (fc = tags.stream().anyMatch(t -> t.contains(s))));
        sc = s;
        return a;
    }

    public void onClick(int x, int y) {

    }
}