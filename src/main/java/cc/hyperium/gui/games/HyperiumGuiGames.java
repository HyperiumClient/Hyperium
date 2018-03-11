/*
 *  Hypixel Community Client, Client optimized for Hypixel Network
 *     Copyright (C) 2018  Hyperium Dev Team
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.gui.games;

import cc.hyperium.gui.HyperiumGui;
import cc.hyperium.mods.sk1ercommon.ResolutionUtil;
import net.minecraft.client.gui.GuiButton;

public class HyperiumGuiGames extends HyperiumGui {

    @Override
    protected void pack() {
        reg("DYNO", new GuiButton(0, ResolutionUtil.current().getScaledWidth() / 2 - 100, 40, "Dyno"), guiButton -> {
          new DynoGame().show();
        }, guiButton -> {

        });
    }
}
