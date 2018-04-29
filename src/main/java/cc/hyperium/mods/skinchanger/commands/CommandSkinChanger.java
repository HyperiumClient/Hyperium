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

package cc.hyperium.mods.skinchanger.commands;

import cc.hyperium.commands.BaseCommand;
import cc.hyperium.commands.CommandException;
import cc.hyperium.mods.skinchanger.SkinChangerMod;
import cc.hyperium.mods.skinchanger.gui.SkinChangerGui;
import cc.hyperium.utils.ChatColor;

public class CommandSkinChanger implements BaseCommand {

    private final SkinChangerMod mod;

    public CommandSkinChanger(SkinChangerMod theMod) {
        this.mod = theMod;
    }

    @Override
    public String getName() {
        return "skinchanger";
    }

    @Override
    public String getUsage() {
        return ChatColor.RED + "Usage: /" + getName();
    }

    @Override
    public void onExecute(String[] args) {
        new SkinChangerGui(this.mod).display();
    }
}
