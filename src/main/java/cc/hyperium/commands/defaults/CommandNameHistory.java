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
import cc.hyperium.gui.NameHistoryGui;

import java.util.Arrays;
import java.util.List;

/**
 * Command to open the NameHistory Gui
 *
 * @author CoalOres
 */
public class CommandNameHistory implements BaseCommand {

    @Override
    public String getName() {
        return "namehistory";
    }

    @Override
    public String getUsage() {
        return "/namehistory <player>";
    }

    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList("nhistory", "names", "name");
    }

    @Override
    public void onExecute(String[] args) {
        switch (args.length) {
            case 0:
                Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new NameHistoryGui());
                break;

            case 1:
                Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new NameHistoryGui(args[0]));
                break;

            default:
                Hyperium.INSTANCE.getHandlers().getGeneralChatHandler().sendMessage("Usage: " + getUsage());
                break;
        }
    }
}
