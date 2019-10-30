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

package cc.hyperium.mods.blockoverlay;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.client.config.GuiSlider;

public class BlockOverlayGui extends GuiScreen {
    private BlockOverlay mod;
    private GuiButton buttonMode;
    private GuiButton buttonColor;
    private GuiSlider sliderWidth;

    public BlockOverlayGui(BlockOverlay mod) {
        this.mod = mod;
    }

    public void initGui() {
        super.buttonList.add(buttonMode = new GuiButton(0, super.width / 2 - 50, super.height / 2 - 35, 100, 20,
            "Mode: " + mod.getSettings().getOverlayMode().getName()));
        super.buttonList.add(buttonColor = new GuiButton(1, super.width / 2 - 50, super.height / 2 - 10, 100, 20,
            "Color"));
        super.buttonList.add(sliderWidth = new GuiSlider(2, super.width / 2 - 50, super.height / 2 + 15, 100, 20,
            "Width: ", "", 0.0f, 5.0f, mod.getSettings().getLineWidth(), false, true));
    }

    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        super.drawDefaultBackground();
        GlStateManager.pushMatrix();
        GlStateManager.scale(1.2f, 1.2f, 0.0f);
        super.drawCenteredString(super.fontRendererObj, "Block Overlay", Math.round(super.width / 2F / 1.2f), Math.round(super.height / 2F / 1.2f) - 50, -1);
        GlStateManager.popMatrix();
        buttonMode.drawButton(super.mc, mouseX, mouseY);
        buttonColor.drawButton(super.mc, mouseX, mouseY);
        sliderWidth.drawButton(super.mc, mouseX, mouseY);
    }

    public void actionPerformed(final GuiButton button) {
        switch (button.id) {
            case 0:
                mod.getSettings().setOverlayMode(BlockOverlayMode.getNextMode(mod.getSettings().getOverlayMode()));
                buttonMode.displayString = "Mode: " + mod.getSettings().getOverlayMode().getName();
                break;
            case 1:
                BlockOverlay.mc.displayGuiScreen(new BlockOverlayColor(mod));
                break;
            case 2:
                mod.getSettings().setLineWidth((float) sliderWidth.getValue());
                break;
        }
    }

    public void mouseClickMove(final int mouseX, final int mouseY, final int clickedMouseButton, final long timeSinceLastClick) {
        mod.getSettings().setLineWidth((float) sliderWidth.getValue());
    }

    public void onGuiClosed() {
        mod.getSettings().save();
    }
}
