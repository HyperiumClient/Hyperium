package cc.hyperium.addons.customcrosshair.utils;

public class RGBA {
    private int RED;
    private int GREEN;
    private int BLUE;
    private int OPACITY;

    public RGBA(final int red, final int green, final int blue, final int opacity) {
        this.RED = red;
        this.GREEN = green;
        this.BLUE = blue;
        this.OPACITY = opacity;
    }

    public int getRed() {
        return this.RED;
    }

    public void setRed(final int red) {
        this.RED = red;
    }

    public int getGreen() {
        return this.GREEN;
    }

    public void setGreen(final int green) {
        this.GREEN = green;
    }

    public int getBlue() {
        return this.BLUE;
    }

    public void setBlue(final int blue) {
        this.BLUE = blue;
    }

    public int getOpacity() {
        return this.OPACITY;
    }

    public void setOpacity(final int opacity) {
        this.OPACITY = opacity;
    }
}
