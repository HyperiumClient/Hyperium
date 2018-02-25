package cc.hyperium.mods.keystrokes.screen.impl;

import cc.hyperium.mods.keystrokes.KeystrokesMod;
import cc.hyperium.mods.keystrokes.config.KeystrokesSettings;
import cc.hyperium.mods.keystrokes.screen.GuiScreenKeystrokes;
import net.minecraftforge.fml.client.config.GuiSlider;

public class GuiSliderScale extends GuiSlider {

    private final KeystrokesSettings settings;
    private final GuiScreenKeystrokes keystrokesGui;

    public GuiSliderScale(KeystrokesMod mod, int id, int xPos, int yPos, int width, int height, GuiScreenKeystrokes keystrokes) {
        super(id, xPos, yPos, width, height, "Scale: ", "%", 50, 150, mod.getSettings().getScale() * 100.0D, false, true);

        this.settings = mod.getSettings();
        this.keystrokesGui = keystrokes;
    }

    @Override
    public void updateSlider() {
        super.updateSlider();
        this.settings.setX(0);
        this.settings.setY(0);
        this.settings.setScale((float) (getValue() / 100.0D));
        this.keystrokesGui.setUpdated();
    }
}
