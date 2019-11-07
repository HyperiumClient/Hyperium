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

package cc.hyperium.mixinsimp.client.gui;

import cc.hyperium.Hyperium;
import cc.hyperium.gui.GuiAddonError;
import cc.hyperium.gui.GuiHyperiumScreenMainMenu;
import cc.hyperium.gui.GuiHyperiumScreenTos;
import cc.hyperium.internal.addons.AddonMinecraftBootstrap;
import net.minecraft.client.gui.GuiMainMenu;

public class HyperiumGuiMainMenu {

    private GuiMainMenu parent;

    public HyperiumGuiMainMenu(GuiMainMenu parent) {
        this.parent = parent;
    }

    public void initGui() {
        if (Hyperium.INSTANCE.isAcceptedTos()) {
            parent.drawDefaultBackground();
        }
    }

    public void drawScreen() {
        if (!Hyperium.INSTANCE.isAcceptedTos()) {
            Hyperium.LOGGER.info("Hasn't accepted! Redirecting them!");
            Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new GuiHyperiumScreenTos());
        } else Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(!AddonMinecraftBootstrap.getDependenciesLoopMap().isEmpty() ||
            !AddonMinecraftBootstrap.getMissingDependenciesMap().isEmpty() ? new GuiAddonError() : new GuiHyperiumScreenMainMenu());
    }

}
