package cc.hyperium.mods.glintcolorizer;

import cc.hyperium.config.ConfigOpt;

import java.awt.Color;

public class Colors {
    @ConfigOpt
    public static boolean chroma;
    @ConfigOpt
    public static int chromaSpeed = 1;
    @ConfigOpt
    public static int glintR = 255;
    @ConfigOpt
    public static int glintG = 255;
    @ConfigOpt
    public static int glintB = 255;
    private static float[] onepoint8glintcolorF = Color.RGBtoHSB((int) Colors.glintR, (int) Colors.glintG, (int) Colors.glintB, null);
    public static int onepoint8glintcolorI = Color.HSBtoRGB(Colors.onepoint8glintcolorF[0], Colors.onepoint8glintcolorF[1], Colors.onepoint8glintcolorF[2]);

    public static void setonepoint8color(int r, int g, int b) {
        Colors.glintR = r;
        Colors.glintG = g;
        Colors.glintB = b;
        Colors.onepoint8glintcolorF = Color.RGBtoHSB((int) Colors.glintR, (int) Colors.glintG, (int) Colors.glintB, null);
        Colors.onepoint8glintcolorI = Color.HSBtoRGB(Colors.onepoint8glintcolorF[0], Colors.onepoint8glintcolorF[1], Colors.onepoint8glintcolorF[2]);
    }

    public void setChroma(boolean bool) {
        if (!(Colors.chroma = bool)) {
            Colors.onepoint8glintcolorF = Color.RGBtoHSB((int) Colors.glintR, (int) Colors.glintG, (int) Colors.glintB, null);
            Colors.onepoint8glintcolorI = Color.HSBtoRGB(Colors.onepoint8glintcolorF[0], Colors.onepoint8glintcolorF[1], Colors.onepoint8glintcolorF[2]);
        }
    }
}
