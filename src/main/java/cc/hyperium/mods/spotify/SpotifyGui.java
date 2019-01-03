/*
 *     Copyright (C) 2018  Hyperium <https://hyperium.cc/>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.mods.spotify;

import cc.hyperium.gui.HyperiumGui;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;

import java.io.IOException;

import org.lwjgl.input.Mouse;

public class SpotifyGui extends HyperiumGui {

    private SpotifyControls controls = SpotifyControls.instance;

    private int lastMouseX;
    private int lastMouseY;

    private boolean dragging = false;

    @Override
    public void initGui() {
        this.buttonList.add(new GuiButton(0, 5, this.height - 25, 100, 20, "Reset Scale"));
        this.buttonList.add(new GuiButton(1, 5, this.height - 45, 100, 20, "Reset Position"));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();

        super.drawScreen(mouseX, mouseY, partialTicks);

        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

        drawCenteredString(this.fontRendererObj, "Pause and play has been temporary disabled", this.width / 2, this.height - 70, Color.WHITE.getRGB());
        drawCenteredString(this.fontRendererObj, "Use the keybind to toggle Spotify from in-game", this.width / 2, this.height - 60, Color.WHITE.getRGB());

        if (this.dragging) {
            int diffX = mouseX - lastMouseX;
            int diffY = mouseY - lastMouseY;

            double newX = this.controls.getX() + diffX;
            double newY = this.controls.getY() + diffY;

            newX = (int) HyperiumGui.clamp((float) newX, 0, (float) (sr.getScaledWidth() - this.controls.getWidthWithScale()) + 1);
            newY = (int) HyperiumGui.clamp((float) newY, 0, (float) (sr.getScaledHeight() - this.controls.getHeightWithScale()) + 1);

            this.controls.setX(newX);
            this.controls.setY(newY);

            lastMouseX = mouseX;
            lastMouseY = mouseY;
        }

        this.controls.renderControls();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        double x = this.controls.getX();
        double y = this.controls.getY();

        double scale = this.controls.getScale();

        if (mouseX > x && mouseX < x + this.controls.getWidthWithScale() && mouseY > y && mouseY < y + this.controls.getHeightWithScale() && mouseButton == 0) {
            this.dragging = true;
            lastMouseX = mouseX;
            lastMouseY = mouseY;
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);

        dragging = false;
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 0:
                this.controls.setScale(1);
                break;
            case 1:
                this.controls.setX(0);
                this.controls.setY(0);
        }
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();

        double scale = this.controls.getScale();

        int i = Mouse.getEventDWheel();

        if (i < 0) {
            this.controls.setScale(scale - 0.05);
        } else if (i > 0) {
            this.controls.setScale(scale + 0.05);
        }
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();

        this.controls.save();
    }

    @Override
    protected void pack() {

    }
}
