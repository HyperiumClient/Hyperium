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

package cc.hyperium.commands.defaults;

import cc.hyperium.Hyperium;
import cc.hyperium.commands.BaseCommand;
import cc.hyperium.commands.CommandException;
import cc.hyperium.commands.CommandUsageException;
import cc.hyperium.gui.integrations.HypixelPrivateMessage;
import cc.hyperium.handlers.handlers.hud.TabCompletionUtil;
import net.minecraft.client.Minecraft;

import java.util.List;

public class CommandPrivateMessage implements BaseCommand {


    @Override
    public String getName() {
        return "pm";
    }

    @Override
    public String getUsage() {
        return "/pm <name>";
    }

    @Override
    public void onExecute(String[] args) throws CommandException {
        if (args.length != 1) {
            throw new CommandUsageException();
        } else {
            new HypixelPrivateMessage(Hyperium.INSTANCE.getHandlers().getPrivateMessageHandler().getChat(args[0])).show();
        }
    }

    @Override
    public List<String> onTabComplete(String[] args) {
        List<String> tabUsernames = TabCompletionUtil.getTabUsernames();
        CommandMessage.addTabHypixel(tabUsernames);
        tabUsernames.remove(Minecraft.getMinecraft().getSession().getUsername());
        return TabCompletionUtil.getListOfStringsMatchingLastWord(args, tabUsernames);
    }


}
