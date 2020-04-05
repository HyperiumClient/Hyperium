/*
 *       Copyright (C) 2018-present Hyperium <https://hyperium.cc/>
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU Lesser General Public License as published
 *       by the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU Lesser General Public License for more details.
 *
 *       You should have received a copy of the GNU Lesser General Public License
 *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cc.hyperium.mods.rodcolor.rodcolor;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiPageButtonList;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlider;
import net.minecraft.util.EnumChatFormatting;

import java.awt.*;
import java.util.function.IntConsumer;

import static cc.hyperium.config.Settings.*;

public class RodColorGui extends GuiScreen {

    private GuiSlider sliderRed = new GuiSlider(new SliderResponder(value -> ROD_COLOR_RED = value), 2, width / 2 - 70, height / 2 - 80, "Red", 0, 255, (int) ROD_COLOR_RED, SliderFormatHelper.HELPER);
    private GuiSlider sliderGreen = new GuiSlider(new SliderResponder(value -> ROD_COLOR_GREEN = value), 3, width / 2 - 70, height / 2 - 58, "Green", 0, 255, (int) ROD_COLOR_GREEN, SliderFormatHelper.HELPER);
    private GuiSlider sliderBlue = new GuiSlider(new SliderResponder(value -> ROD_COLOR_BLUE = value), 4, width / 2 - 70, height / 2 - 36, "Blue", 0, 255, (int) ROD_COLOR_BLUE, SliderFormatHelper.HELPER);
    private GuiSlider sliderAlpha = new GuiSlider(new SliderResponder(value -> ROD_COLOR_ALPHA = value), 5, width / 2 - 70, height / 2 - 14, "Alpha", 0, 255, (int) ROD_COLOR_ALPHA, SliderFormatHelper.HELPER);
    private GuiSlider sliderWidth = new GuiSlider(new SliderResponder(value -> ROD_COLOR_ROPE_WIDTH = value), 6, width / 2 - 70, height / 2 + 8, "Rope Width", 1, 30, (int) ROD_COLOR_ROPE_WIDTH, SliderFormatHelper.HELPER);
    private GuiButton buttonChroma = new GuiButton(7, width / 2 - 70, height / 2 + 30, 150, 20, "Chroma: " + (ROD_COLOR_CHROMA ? EnumChatFormatting.RED + "Disable" : EnumChatFormatting.GREEN + "Enable"));

    public void initGui() {
        buttonList.clear();
        sliderRed.yPosition = height / 2 - 80;
        sliderGreen.yPosition = height / 2 - 58;
        sliderBlue.yPosition = height / 2 - 36;
        sliderAlpha.yPosition = height / 2 - 14;
        sliderWidth.yPosition = height / 2 + 8;
        buttonChroma.yPosition = height / 2 + 30;
        super.buttonList.add(sliderRed);
        super.buttonList.add(sliderGreen);
        super.buttonList.add(sliderBlue);
        super.buttonList.add(sliderAlpha);
        super.buttonList.add(buttonChroma);
        super.buttonList.add(sliderWidth);
        buttonList.forEach(b -> b.xPosition = width / 2 - 70);
    }

    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        try {
            drawDefaultBackground();
        } catch (Exception ignored) { // literally it happens for no absolute reason...
        }
        int x = width / 2 - 70;
        int y = height / 2 + 15;
        long dif = (x * 10) - (y * 10);
        long color = System.currentTimeMillis() - dif;
        float ff = 2000.0F;
        int i = Color.HSBtoRGB((float) (color % (int) ff) / ff, 0.8F, 0.8F);
        if (ROD_COLOR_CHROMA)
            drawRect(x - 30, y + 44, x + 180, y + 54, i);
        else
            drawRect(x - 30, y + 44, x + 180, y + 54, new Color((int) ROD_COLOR_RED, (int) ROD_COLOR_GREEN, (int) ROD_COLOR_BLUE, (int) ROD_COLOR_ALPHA).getRGB());
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void actionPerformed(final GuiButton button) {
        switch (button.id) {
            case 2: {
                ROD_COLOR_RED = getSliderValue(sliderRed);
                break;
            }
            case 3: {
                ROD_COLOR_GREEN = getSliderValue(sliderGreen);
                break;
            }
            case 4: {
                ROD_COLOR_BLUE = getSliderValue(sliderBlue);
                break;
            }
            case 5: {
                ROD_COLOR_ALPHA = getSliderValue(sliderAlpha);
                break;
            }
            case 6: {
                ROD_COLOR_ROPE_WIDTH = getSliderValue(sliderWidth);
                break;
            }
            case 7: {
                boolean chroma = ROD_COLOR_CHROMA = !ROD_COLOR_CHROMA;
                button.displayString = "Chroma: " + (chroma ? EnumChatFormatting.RED + "Disable" : EnumChatFormatting.GREEN + "Enable");
            }
        }
    }

    public void mouseClickMove(final int mouseX, final int mouseY, final int clickedMouseButton, final long timeSinceLastClick) {
        ROD_COLOR_RED = getSliderValue(sliderRed);
        ROD_COLOR_GREEN = getSliderValue(sliderGreen);
        ROD_COLOR_BLUE = getSliderValue(sliderBlue);
        ROD_COLOR_ALPHA = getSliderValue(sliderAlpha);
        ROD_COLOR_ROPE_WIDTH = getSliderValue(sliderWidth);
    }

    private int getSliderValue(GuiSlider slider) {
        return (int) slider.func_175220_c(); // apparently this is the getSliderValue() method in 1.12.2
    }

    public static class SliderResponder implements GuiPageButtonList.GuiResponder {

        private IntConsumer setting;

        public SliderResponder(IntConsumer setting) {
            this.setting = setting;
        }

        /**
         * setEntryValue
         */
        @Override
        public void func_175321_a(int id, boolean b) {

        }

        /**
         * setEntryValue
         */
        @Override
        public void onTick(int id, float v) {
            setting.accept((int) v);
        }

        /**
         * setEntryValue
         */
        @Override
        public void func_175319_a(int id, String s) {

        }
    }

    public static class SliderFormatHelper implements GuiSlider.FormatHelper {

        public static final SliderFormatHelper HELPER = new SliderFormatHelper();

        @Override
        public String getText(int id, String name, float value) {
            return name + ": " + (int) value;
        }
    }
}
