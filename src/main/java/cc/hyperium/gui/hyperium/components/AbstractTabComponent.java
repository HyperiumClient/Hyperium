package cc.hyperium.gui.hyperium.components;

/*
 * Created by Cubxity on 27/08/2018
 */
public abstract class AbstractTabComponent {
    protected AbstractTab tab;

    AbstractTabComponent(AbstractTab tab) {
        this.tab = tab;
    }

    public int getHeight() {
        return 20;
    }

    abstract public void render(int x, int y, int width);
}