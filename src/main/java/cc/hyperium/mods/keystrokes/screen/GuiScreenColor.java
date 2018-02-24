package cc.hyperium.mods.keystrokes.screen;

import cc.hyperium.mods.keystrokes.utils.IScrollable;
import cc.hyperium.mods.keystrokes.KeystrokesMod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.config.GuiSlider;

import java.io.IOException;

public class GuiScreenColor extends GuiScreen {

    private IScrollable scrollable1; // Red
    private IScrollable scrollable2; // Green
    private IScrollable scrollable3; // Blue
    private IScrollable scrollable4; // Gamma

    private boolean updated = false;

    GuiScreenColor(IScrollable scrollable1, IScrollable scrollable2, IScrollable scrollable3) {
        this.scrollable1 = scrollable1;
        this.scrollable2 = scrollable2;
        this.scrollable3 = scrollable3;
        this.scrollable4 = null;
    }

    GuiScreenColor(IScrollable scrollable1, IScrollable scrollable2, IScrollable scrollable3, IScrollable scrollable4) {
        this.scrollable1 = scrollable1;
        this.scrollable2 = scrollable2;
        this.scrollable3 = scrollable3;
        this.scrollable4 = scrollable4;
    }

    @Override
    public void initGui() {
        this.buttonList.add(new GuiSlider(0, this.width / 2 - 100, this.height / 2 - 32, 200, 20, "Red: ", "", 0, 255, this.scrollable1.getAmount(), false, true) {
            @Override
            public void updateSlider() {
                super.updateSlider();
                scrollable1.onScroll(getValue(), getValueInt());
                updated = true;
            }
        });
        this.buttonList.add(new GuiSlider(1, this.width / 2 - 100, this.height / 2 - 10, 200, 20, "Green: ", "", 0, 255, this.scrollable2.getAmount(), false, true) {
            @Override
            public void updateSlider() {
                super.updateSlider();
                scrollable2.onScroll(getValue(), getValueInt());
                updated = true;
            }
        });
        this.buttonList.add(new GuiSlider(2, this.width / 2 - 100, this.height / 2 + 12, 200, 20, "Blue: ", "", 0, 255, this.scrollable3.getAmount(), false, true) {
            @Override
            public void updateSlider() {
                super.updateSlider();
                scrollable3.onScroll(getValue(), getValueInt());
                updated = true;
            }
        });
        if (this.scrollable4 != null) {
            this.buttonList.add(new GuiSlider(3, this.width / 2 - 100, this.height / 2 + 34, 200, 20, "Alpha: ", "", 0, 255, this.scrollable4.getAmount(), false, true) {
                @Override
                public void updateSlider() {
                    super.updateSlider();
                    scrollable4.onScroll(getValue(), getValueInt());
                    updated = true;
                }
            });
        }
        this.buttonList.add(new GuiButton(4, 5, this.height - 25, 100, 20, "Back"));
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 4:
                Minecraft.getMinecraft().displayGuiScreen(new GuiScreenKeystrokes());
                break;
            default:
                break;
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1) {
            Minecraft.getMinecraft().displayGuiScreen(new GuiScreenKeystrokes());
        } else {
            super.keyTyped(typedChar, keyCode);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        KeystrokesMod.getRenderer().renderKeystrokes();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void onGuiClosed() {
        if (this.updated) {
            KeystrokesMod.getSettings().save();
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
