package cc.hyperium.gui.main.components;

/*
 * Created by Cubxity on 27/08/2018
 */
public abstract class AbstractTabComponent {
    private AbstractTab tab;

    public AbstractTabComponent(AbstractTab tab) {
        this.tab = tab;
    }

    public int getHeight() {
        return 20;
    }

    abstract public void render(int x, int y, int width);
}
