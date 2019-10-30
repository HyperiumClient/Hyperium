package cc.hyperium.mods.keystrokes.screen;

import cc.hyperium.mods.keystrokes.KeystrokesMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.config.GuiSlider;

import java.io.IOException;

public class GuiScreenBackgroundColor extends GuiScreen implements IScreen {

    private final KeystrokesMod mod;
    private final IScrollable red;
    private final IScrollable green;
    private final IScrollable blue;
    private boolean updated;

    GuiScreenBackgroundColor(KeystrokesMod mod, IScrollable red, IScrollable green, IScrollable blue) {
        this.mod = mod;
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    @Override
    public void initGui() {
        buttonList.add(new GuiSlider(0, width / 2 - 80, calculateHeight(3), 150, 20, "Red: ", "",
            0, 255, red.getAmount(), false, true) {
            @Override
            public void updateSlider() {
                super.updateSlider();
                red.onScroll(getValue(), getValueInt());
                updated = true;
            }
        });

        buttonList.add(new GuiSlider(1, width / 2 - 80, calculateHeight(4), 150, 20, "Green: ", "",
            0, 255, green.getAmount(), false, true) {
            @Override
            public void updateSlider() {
                super.updateSlider();
                green.onScroll(getValue(), getValueInt());
                updated = true;
            }
        });

        buttonList.add(new GuiSlider(2, width / 2 - 80, calculateHeight(5), 150, 20, "Blue: ", "",
            0, 255, blue.getAmount(), false, true) {
            @Override
            public void updateSlider() {
                super.updateSlider();
                blue.onScroll(getValue(), getValueInt());
                updated = true;
            }
        });

        buttonList.add(new GuiButton(3, 5, height - 25, 100, 20, "Back"));
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 3) Minecraft.getMinecraft().displayGuiScreen(new GuiScreenKeystrokes(mod));
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1) Minecraft.getMinecraft().displayGuiScreen(new GuiScreenKeystrokes(mod));
        else super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        mod.getRenderer().renderKeystrokes();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void onGuiClosed() {
        if (updated) mod.getSettings().save();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
