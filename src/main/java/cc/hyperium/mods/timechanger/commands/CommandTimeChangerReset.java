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

package cc.hyperium.mods.timechanger.commands;

import cc.hyperium.commands.BaseCommand;
import cc.hyperium.handlers.handlers.chat.GeneralChatHandler;
import cc.hyperium.mods.timechanger.TimeChanger;
import cc.hyperium.mods.timechanger.TimeChanger.TimeType;
import cc.hyperium.utils.ChatColor;

public class CommandTimeChangerReset implements BaseCommand {

    private final TimeChanger mod;

    public CommandTimeChangerReset(TimeChanger main) {
        mod = main;
    }

    @Override
    public String getName() {
        return "resettime";
    }

    @Override
    public String getUsage() {
        return ChatColor.RED + "Usage: /resettime";
    }

    @Override
    public void onExecute(String[] args) {
        mod.setTimeType(TimeType.VANILLA);
        GeneralChatHandler.instance().sendMessage(ChatColor.RED + "[TimeChanger] " + ChatColor.GREEN + "Now using vanilla time.", false);
    }
}
