package cc.hyperium.mods.keystrokes.screen.impl;

import cc.hyperium.mods.keystrokes.config.KeystrokesSettings;
import cc.hyperium.mods.keystrokes.KeystrokesMod;
import cc.hyperium.mods.keystrokes.screen.GuiScreenKeystrokes;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.client.config.GuiSlider;

public class GuiSliderFadeTime extends GuiSlider {

    private final KeystrokesSettings settings;
    private final GuiScreenKeystrokes keystrokesGui;

    public GuiSliderFadeTime(int id, int xPos, int yPos, int width, int height, GuiScreenKeystrokes keystrokes) {
        super(id, xPos, yPos, width, height, "Fade Time: ", "%", 0, 30, KeystrokesMod.getSettings().getFadeTime() * 100.0D, false, true);

        this.settings = KeystrokesMod.getSettings();
        this.keystrokesGui = keystrokes;
    }

    @Override
    public void updateSlider() {
        super.updateSlider();
        this.settings.setFadeTime((float) (getValue() / 100.0D));

        this.keystrokesGui.setUpdated();

        if (getValue() < 10) {
            this.displayString = this.dispString + "Slow";
        } else if (getValue() > 20) {
            this.displayString = this.dispString + "Fast";
        } else {
            this.displayString = this.dispString + "Normal";
        }
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (getValue() > this.maxValue) {
            setValue(this.maxValue);
        } else if (getValue() < this.minValue) {
            setValue(this.minValue);
        }

        if (getValue() < 10) {
            this.displayString = this.dispString + "Slow";
        } else if (getValue() > 20) {
            this.displayString = this.dispString + "Fast";
        } else {
            this.displayString = this.dispString + "Normal";
        }
        super.drawButton(mc, mouseX, mouseY);
    }
}
