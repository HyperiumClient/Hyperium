package cc.hyperium.mods.keystrokes.screen.impl;

import cc.hyperium.mods.keystrokes.KeystrokesMod;
import cc.hyperium.mods.keystrokes.config.KeystrokesSettings;
import cc.hyperium.mods.keystrokes.screen.GuiScreenKeystrokes;
import net.minecraftforge.fml.client.config.GuiSlider;

public class GuiSliderOpacity extends GuiSlider {

    private final KeystrokesSettings settings;
    private final GuiScreenKeystrokes keystrokesGui;

    public GuiSliderOpacity(KeystrokesMod mod, int id, int xPos, int yPos, int width, int height, GuiScreenKeystrokes keystrokes) {
        super(id, xPos, yPos, width, height, "Key Opacity: ", "", 0,
            255, mod.getSettings().getKeyBackgroundOpacity(), false, true);

        settings = mod.getSettings();
        keystrokesGui = keystrokes;
    }

    @Override
    public void updateSlider() {
        super.updateSlider();
        settings.setKeyBackgroundOpacity(getValueInt());
        keystrokesGui.setUpdated();
    }
}
