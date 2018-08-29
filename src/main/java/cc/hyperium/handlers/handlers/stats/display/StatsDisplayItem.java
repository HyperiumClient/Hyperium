package cc.hyperium.handlers.handlers.stats.display;

public abstract class StatsDisplayItem {

    public int width;
    public int height;

    public abstract void draw(int x, int y);

}
