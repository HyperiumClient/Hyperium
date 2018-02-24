package cc.hyperium.mods.keystrokes.utils;

/**
 * Better way to control scrolling (Using Anonymous classes)
 */
public interface IScrollable {

    public double getAmount();

    public void onScroll(double doubleAmount, int intAmount);
}
