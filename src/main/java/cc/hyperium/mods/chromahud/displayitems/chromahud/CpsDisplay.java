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

package cc.hyperium.mods.chromahud.displayitems.chromahud;

import cc.hyperium.mods.chromahud.ElementRenderer;
import cc.hyperium.mods.chromahud.api.DisplayItem;
import cc.hyperium.utils.JsonHolder;
import net.minecraft.client.Minecraft;
import cc.hyperium.config.Settings;
import cc.hyperium.config.ConfigOpt;


/**
 * @author Sk1er
 */
public class CpsDisplay extends DisplayItem {

    public CpsDisplay(JsonHolder data, int ordinal) {
        super(data, ordinal);
        this.height = 10;
    }


    @Override
    public void draw(int starX, double startY, boolean isConfig) {
        if (!Settings.CHROMAHUD_SQUAREBRACE_PREFIX_OPTION) {
            ElementRenderer.draw(starX, startY, "CPS: " + ElementRenderer.getCPS());
        } else {
            ElementRenderer.draw(starX, startY, "[CPS] " + ElementRenderer.getCPS());
        }
        if (isConfig) {
            if (!Settings.CHROMAHUD_SQUAREBRACE_PREFIX_OPTION) {
                this.width = Minecraft.getMinecraft().fontRendererObj.getStringWidth("CPS: " + ElementRenderer.getCPS());
            } else {
                this.width = Minecraft.getMinecraft().fontRendererObj.getStringWidth("[CPS] " + ElementRenderer.getCPS());
            }
        } else
            this.width = 0;
    }


}
