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
import cc.hyperium.commands.CommandException;
import cc.hyperium.handlers.handlers.chat.GeneralChatHandler;
import cc.hyperium.mods.timechanger.TimeChanger;
import cc.hyperium.mods.timechanger.TimeChanger.TimeType;
import cc.hyperium.utils.ChatColor;
import org.apache.commons.lang3.math.NumberUtils;

public class CommandTimeChangerFastTime implements BaseCommand {

    private final TimeChanger mod;

    public CommandTimeChangerFastTime(TimeChanger main) {
        mod = main;
    }

    @Override
    public String getName() {
        return "fasttime";
    }

    @Override
    public String getUsage() {
        return ChatColor.RED + "Usage: /fasttime <multiplier>";
    }

    @Override
    public void onExecute(String[] args) throws CommandException {
        assert args.length != 0;

        final double multiplier = NumberUtils.toDouble(args[0], -1.0);

        if (multiplier < 0.0) {
            GeneralChatHandler.instance().sendMessage(ChatColor.RED + "[TimeChanger] " + ChatColor.RED + "Invalid multiplier!", false);
            return;
        }

        mod.setTimeType(TimeType.FAST);
        mod.setFastTimeMultiplier(multiplier);
        GeneralChatHandler.instance().sendMessage(ChatColor.RED + "[TimeChanger] " + ChatColor.GREEN + "Time set to fast (" + multiplier + ").", false);
    }
}
