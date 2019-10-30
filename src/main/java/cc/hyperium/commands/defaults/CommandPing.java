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

import cc.hyperium.commands.BaseCommand;
import cc.hyperium.handlers.handlers.hud.NetworkInfo;
import cc.hyperium.handlers.handlers.hud.TabCompletionUtil;
import net.minecraft.client.Minecraft;

import java.util.List;

public class CommandPing implements BaseCommand {
    @Override
    public String getName() {
        return "ping";
    }

    @Override
    public String getUsage() {
        return "/ping or /ping <name>";
    }

    @Override
    public void onExecute(String[] args) {
        String name = (args.length == 1) ? args[0] : Minecraft.getMinecraft().getSession().getUsername();
        NetworkInfo.getInstance().printPing(name);
    }

    @Override
    public List<String> onTabComplete(String[] args) {
        return TabCompletionUtil.getListOfStringsMatchingLastWord(args, TabCompletionUtil.getTabUsernames());
    }
}
