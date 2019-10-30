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

package cc.hyperium.commands.defaults;

import cc.hyperium.Hyperium;
import cc.hyperium.commands.BaseCommand;
import cc.hyperium.commands.HyperiumCommandHandler;
import cc.hyperium.utils.ChatColor;

public class CommandDisableCommand implements BaseCommand {

    private HyperiumCommandHandler handler = Hyperium.INSTANCE.getHandlers().getHyperiumCommandHandler();

    @Override
    public String getName() {
        return "disablecommand";
    }

    @Override
    public String getUsage() {
        return "Ignores a command when executed in Hyperium";
    }

    @Override
    public void onExecute(String[] args) {
        if (args.length == 0) {
            Hyperium.INSTANCE.getHandlers().getGeneralChatHandler().sendMessage("Please input the command to disable");
            return;
        }

        String command = args[0];

        if (command.equalsIgnoreCase("disablecommand") || command.equalsIgnoreCase("hyperium")) {
            Hyperium.INSTANCE.getHandlers().getGeneralChatHandler().sendMessage("That command cannot be disabled!");
            return;
        }

        // True if the command is disabled, false if it is not
        boolean disabled = handler.addOrRemoveCommand(args[0]);

        ChatColor color = disabled ? ChatColor.RED : ChatColor.GREEN;

        Hyperium.INSTANCE.getHandlers().getGeneralChatHandler().sendMessage("Command '" + args[0] + "' is " + color +
            (disabled ? "now" : "no longer") + ChatColor.WHITE + " disabled!");

        handler.saveDisabledCommands();
    }
}
