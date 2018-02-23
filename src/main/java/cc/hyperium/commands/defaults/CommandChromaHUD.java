/*
 * Hyperium Client, Free client with huds and popular mod
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

package cc.hyperium.commands.defaults;

import cc.hyperium.Hyperium;
import cc.hyperium.commands.BaseCommand;
import cc.hyperium.commands.CommandException;
import cc.hyperium.mods.chromahud.gui.GeneralConfigGui;

/**
 * Created by mitchellkatz on 2/19/18. Designed for production use on Sk1er.club
 */
public class CommandChromaHUD implements BaseCommand {
    @Override
    public String getName() {
        return "chromahud";
    }

    @Override
    public String getUsage() {
        return "chromahud";
    }

    @Override
    public void onExecute(String[] args) throws CommandException {
        Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new GeneralConfigGui(Hyperium.INSTANCE.getModIntegration().getChromaHUD()));
    }
}
