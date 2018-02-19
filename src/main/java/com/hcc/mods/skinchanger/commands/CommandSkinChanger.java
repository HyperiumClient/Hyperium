/*
 *     Copyright (C) 2017 boomboompower
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.hcc.mods.skinchanger.commands;

import com.hcc.commands.BaseCommand;
import com.hcc.commands.CommandException;
import com.hcc.mods.skinchanger.SkinChangerMod;
import com.hcc.mods.skinchanger.gui.SkinChangerGui;
import com.hcc.utils.ChatColor;

public class CommandSkinChanger implements BaseCommand {
    
    private SkinChangerMod mod;
    
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
    public void onExecute(String[] args) throws CommandException {
        new SkinChangerGui(this.mod).display();
    }
}
