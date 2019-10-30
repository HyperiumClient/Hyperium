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

package cc.hyperium.mods.togglechat.commands;

import cc.hyperium.commands.BaseCommand;
import cc.hyperium.mods.togglechat.ToggleChatMod;
import cc.hyperium.mods.togglechat.gui.ToggleChatMainGui;
import cc.hyperium.utils.ChatColor;

import java.util.Collections;
import java.util.List;

/**
 * The BaseCommand implementation for ToggleChat's command with aliases
 *
 * @author boomboompower
 */
public class CommandToggleChat implements BaseCommand {

    /**
     * The "mod" instance
     */
    private final ToggleChatMod mod;

    /**
     * Default constructor
     */
    public CommandToggleChat(ToggleChatMod impl) {
        mod = impl;
    }

    @Override
    public String getName() {
        return "chattoggle";
    }

    @Override
    public String getUsage() {
        return ChatColor.RED + "Usage: /chattoggle";
    }

    @Override
    public List<String> getCommandAliases() {
        return Collections.singletonList("tc");
    }

    @Override
    public void onExecute(String[] args) {
        new ToggleChatMainGui(mod, 0).display();
    }
}
