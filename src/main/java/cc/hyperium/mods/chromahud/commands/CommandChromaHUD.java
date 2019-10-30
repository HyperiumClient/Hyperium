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

package cc.hyperium.mods.chromahud.commands;

import cc.hyperium.commands.BaseCommand;
import cc.hyperium.mods.chromahud.ChromaHUD;
import cc.hyperium.mods.chromahud.gui.GeneralConfigGui;

/**
 * @author Sk1er
 */
public class CommandChromaHUD implements BaseCommand {

    private final ChromaHUD mod;

    public CommandChromaHUD(ChromaHUD modIn) {
        mod = modIn;
    }

    @Override
    public String getName() {
        return "chromahud";
    }

    @Override
    public String getUsage() {
        return "chromahud";
    }

    @Override
    public void onExecute(String[] args) {
        new GeneralConfigGui(mod).display();
    }
}
