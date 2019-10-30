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

package cc.hyperium.mods.chromahud.displayitems.hyperium;

import cc.hyperium.mods.chromahud.api.DisplayItem;
import cc.hyperium.utils.JsonHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import org.lwjgl.opengl.GL11;

/*
 * Created by Cubxity on 22/04/2018
 */
public class PlayerDisplay extends DisplayItem {

    public PlayerDisplay(JsonHolder data, int ordinal) {
        super(data, ordinal);
        width = 51;
        height = 100;
    }


    @Override
    public void draw(int x, double y, boolean config) {
        GlStateManager.pushMatrix();
        GlStateManager.color(1, 1, 1);

        GlStateManager.translate(x, y, 0);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enableAlpha();

        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.enableAlpha();
        GlStateManager.enableDepth();

        GlStateManager.rotate(30, 0, 1.0F, 0);
        GuiInventory.drawEntityOnScreen(0, 100, 50, 0, 0, Minecraft.getMinecraft().thePlayer);
        GlStateManager.depthFunc(GL11.GL_LEQUAL);
        GlStateManager.resetColor();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableDepth();
        GlStateManager.popMatrix();
    }
}
