package cc.hyperium.gui;

import cc.hyperium.config.ConfigOpt;
import cc.hyperium.gui.main.HyperiumOverlay;
import cc.hyperium.gui.main.components.OverlayButton;
import cc.hyperium.gui.main.components.OverlayLabel;
import cc.hyperium.gui.main.components.OverlaySlider;
import cc.hyperium.mods.glintcolorizer.Colors;

import java.lang.reflect.Field;

public class ColourOptions extends HyperiumOverlay {

    @ConfigOpt
    public static int accent_r = 136;
    @ConfigOpt
    public static int accent_g = 255;
    @ConfigOpt
    public static int accent_b = 0;


    public static boolean toggle = true;

    public ColourOptions() {
        super("GUI Options", false);
        reload();
    }

    /*
     * TAKEN FROM GLINTCOLORIZERSETTINGS CLASS as OverlaySlider didn't work (and i wasnt bothered to do all of that code)
     */
    private void addSlider(String label, Field f, int max, int min, boolean updateColor) {
        f.setAccessible(true);
        try {
            this.getComponents().add(new OverlaySlider(label, min, max, ((Integer) f.get(null)).floatValue(), (i) -> {
                try {
                    f.set(null, i.intValue());
                    Colors.setonepoint8color(Colors.glintR, Colors.glintG, Colors.glintB);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }, true));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void addLabel(String text) {
        this.getComponents().add(new OverlayLabel(text, true, () -> {
        }));
    }

    private void reload() {
        try {
            addLabel("Accent Colour:");
            addSlider("Red", this.getClass().getField("accent_r"), 255, 0, true);
            addSlider("Green", this.getClass().getField("accent_g"), 255, 0, true);
            addSlider("Blue", this.getClass().getField("accent_b"), 255, 0, true);
            addToggle("Example: ", this.getClass().getField("toggle"), null, true, this);
            this.getComponents().add(new OverlayButton("Reset to default colours", () -> {
                try {
                    this.getClass().getField("accent_r").setInt(null, 136);
                    this.getClass().getField("accent_g").setInt(null, 255);
                    this.getClass().getField("accent_b").setInt(null, 0);
                } catch (IllegalAccessException | NoSuchFieldException e) {
                    e.printStackTrace();
                }
            }));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}

