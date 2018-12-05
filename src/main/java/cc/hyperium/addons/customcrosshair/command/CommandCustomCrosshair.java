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

package cc.hyperium.addons.customcrosshair.command;

import cc.hyperium.Hyperium;
import cc.hyperium.addons.customcrosshair.gui.GuiCustomCrosshairEditCrosshair;
import cc.hyperium.addons.customcrosshair.CustomCrosshairAddon;
import cc.hyperium.commands.BaseCommand;

import java.util.Collections;
import java.util.List;

public class CommandCustomCrosshair implements BaseCommand {

    private CustomCrosshairAddon addon;

    public CommandCustomCrosshair(CustomCrosshairAddon addon) {
        this.addon = addon;
    }

    @Override
    public String getName() {
        return "customcrosshairaddon";
    }

    @Override
    public String getUsage() {
        return "/customcrosshairaddon";
    }

    @Override
    public void onExecute(String[] strings) {
        Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new GuiCustomCrosshairEditCrosshair(this.addon));
    }

    @Override
    public List<String> getCommandAliases() {
        return Collections.singletonList("cch");
    }
}
