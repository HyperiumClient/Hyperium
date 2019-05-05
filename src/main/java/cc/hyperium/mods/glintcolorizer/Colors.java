package cc.hyperium.mods.glintcolorizer;

import cc.hyperium.config.Category;
import cc.hyperium.config.ConfigOpt;
import cc.hyperium.config.SliderSetting;
import cc.hyperium.config.ToggleSetting;

import java.awt.Color;

public class Colors {
    @ConfigOpt
    @ToggleSetting(category = Category.GLINTCOLORIZER, mods = true, name = "Chroma")
    public static boolean chroma;
    @ConfigOpt
    @SliderSetting(name = "Chroma speed", mods = true, category = Category.GLINTCOLORIZER, min = 1, max = 10, isInt = true)
    public static int chromaSpeed = 1;
    @ConfigOpt
    @SliderSetting(name = "Red", mods = true, category = Category.GLINTCOLORIZER, min = 0, max = 255, isInt = true)
    public static int glintR = 255;
    @ConfigOpt
    @SliderSetting(name = "Green", mods = true, category = Category.GLINTCOLORIZER, min = 0, max = 255, isInt = true)
    public static int glintG = 255;
    @ConfigOpt
    @SliderSetting(name = "Blue", mods = true, category = Category.GLINTCOLORIZER, min = 0, max = 255, isInt = true)
    public static int glintB = 255;
    @ConfigOpt
    @ToggleSetting(category = Category.GLINTCOLORIZER, mods = true, name = "Enabled")
    public static boolean enabled = false;
    private static float[] onepoint8glintcolorF = Color.RGBtoHSB(Colors.glintR, Colors.glintG, Colors.glintB, null);
    public static int onepoint8glintcolorI = Color.HSBtoRGB(Colors.onepoint8glintcolorF[0], Colors.onepoint8glintcolorF[1], Colors.onepoint8glintcolorF[2]);

    public static void setonepoint8color(int r, int g, int b) {
        Colors.glintR = r;
        Colors.glintG = g;
        Colors.glintB = b;
        Colors.onepoint8glintcolorF = Color.RGBtoHSB(Colors.glintR, Colors.glintG, Colors.glintB, null);
        Colors.onepoint8glintcolorI = Color.HSBtoRGB(Colors.onepoint8glintcolorF[0], Colors.onepoint8glintcolorF[1], Colors.onepoint8glintcolorF[2]);
    }

    public void setChroma(boolean bool) {
        if (!(Colors.chroma = bool)) {
            Colors.onepoint8glintcolorF = Color.RGBtoHSB(Colors.glintR, Colors.glintG, Colors.glintB, null);
            Colors.onepoint8glintcolorI = Color.HSBtoRGB(Colors.onepoint8glintcolorF[0], Colors.onepoint8glintcolorF[1], Colors.onepoint8glintcolorF[2]);
        }
    }
}
