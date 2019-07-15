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
import cc.hyperium.handlers.handlers.chat.GeneralChatHandler;

public class CommandStats implements BaseCommand {

    @Override
    public String getName() {
        return "hstats";
    }

    @Override
    public String getUsage() {
        return "/hstats <player>";
    }

    @Override
    public void onExecute(String[] args) {
        if (args.length == 1) {
            Hyperium.INSTANCE.getHandlers().getStatsHandler().initStatsViewer(args[0]);
        } else {
            GeneralChatHandler.instance().sendMessage(getUsage());
        }
    }
}
