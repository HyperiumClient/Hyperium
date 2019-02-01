package cc.hyperium.mods.keystrokes.render;

import cc.hyperium.mods.keystrokes.keys.impl.CustomKey;

public class CustomKeyWrapper {

    private CustomKey key;
    private double xOffset;
    private double yOffset;

    public CustomKeyWrapper(CustomKey key, int xOffset, int yOffset) {
        this.key = key;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    public CustomKey getKey() {
        return key;
    }

    public double getxOffset() {
        return xOffset;
    }

    public void setxOffset(double xOffset) {
        this.xOffset = xOffset;
    }

    public double getyOffset() {
        return yOffset;
    }

    public void setyOffset(double yOffset) {
        this.yOffset = yOffset;
    }
}
