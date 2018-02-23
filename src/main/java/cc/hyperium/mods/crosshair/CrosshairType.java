package cc.hyperium.mods.crosshair;

import java.awt.*;

public abstract class CrosshairType {

    public abstract void draw(boolean bloom,
                              Color colour,
                              int thickness,
                              int length,
                              int centerGap,
                              int dotSize,
                              int dotOpacity);
}
