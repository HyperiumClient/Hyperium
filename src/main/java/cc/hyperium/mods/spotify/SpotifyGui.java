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
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import java.io.IOException;

public class SpotifyGui extends HyperiumGui {
    private int lastMouseX, lastMouseY;
    private boolean dragging = false;

    @Override
    protected void pack() {

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        drawDefaultBackground();

        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        SpotifyControls controls = SpotifyControls.instance;

        if (this.dragging) {
            int diffX = mouseX - lastMouseX, diffY = mouseY - lastMouseY;
            int newX = controls.getX() + diffX, newY = controls.getY() + diffY;
            newX = (int) clamp(newX, 0, sr.getScaledWidth() - 200);
            newY = (int) clamp(newY, 0, sr.getScaledHeight() - 50);

            controls.setX(newX);
            controls.setY(newY);

            lastMouseX = mouseX;
            lastMouseY = mouseY;
        }

        SpotifyControls.instance.renderControls();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        int x = SpotifyControls.instance.getX(), y = SpotifyControls.instance.getY();

        if (mouseX > x && mouseX < x + 200 && mouseY > y && mouseY < y + 50 && mouseButton == 0) {
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
    public void onGuiClosed() {
        super.onGuiClosed();

        SpotifyControls.instance.save();
    }
}
