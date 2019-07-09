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

package cc.hyperium.gui.playerrenderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;

public class GuiPlayerRenderer {

    public static final FakePlayerRendering PLAYER_RENDERER = new FakePlayerRendering(Minecraft.getMinecraft().getSession().getProfile());

    public static void renderPlayerWithRotation(int x, int y, float givenRotation) {
        GlStateManager.pushMatrix();
        PLAYER_RENDERER.renderPlayerModel(x + 80, y + 100, 55, givenRotation);
        GlStateManager.popMatrix();

    }

}
