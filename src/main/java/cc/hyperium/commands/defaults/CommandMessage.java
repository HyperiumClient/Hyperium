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
import cc.hyperium.handlers.handlers.hud.TabCompletionUtil;
import net.minecraft.client.Minecraft;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class CommandMessage implements BaseCommand {

    @Override
    public String getName() {
        return "msg";
    }

    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList("msg", "tell", "w", "message");
    }

    @Override
    public String getUsage() {
        return "/msg <name> <msg>";
    }

    @Override
    public void onExecute(String[] args) {
        Hyperium.INSTANCE.getHandlers().getCommandQueue().queue("/msg " + Arrays.stream(args).map(arg -> arg + " ").collect(Collectors.joining()));
    }

    @Override
    public List<String> onTabComplete(String[] args) {
        List<String> tabUsernames = TabCompletionUtil.getTabUsernames();
        addTabHypixel(tabUsernames);
        tabUsernames.remove(Minecraft.getMinecraft().getSession().getUsername());
        return TabCompletionUtil.getListOfStringsMatchingLastWord(args, tabUsernames);
    }

    @Override
    public boolean tabOnly() {
        return true;
    }

    static void addTabHypixel(List<String> tabUsernames) {
        if (Hyperium.INSTANCE.getHandlers().getHypixelDetector().isHypixel()) {
            try {
                for (String s : Hyperium.INSTANCE.getHandlers().getDataHandler().getFriendsForCurrentUser().get().getData().getKeys()) {
                    String name = Hyperium.INSTANCE.getHandlers().getDataHandler().getFriendsForCurrentUser().get().getData().optJSONObject(s).optString("name");
                    if (!name.isEmpty()) tabUsernames.add(name);
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
}
