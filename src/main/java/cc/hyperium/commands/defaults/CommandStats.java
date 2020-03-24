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
import cc.hyperium.handlers.handlers.hud.TabCompletionUtil;

import java.util.ArrayList;
import java.util.List;

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
  public List<String> onTabComplete(String[] args) {
    List<String> tabUsernames = TabCompletionUtil.getTabUsernames();
    List<String> complete = new ArrayList<>();

    try {
      for (String s : Hyperium.INSTANCE.getHandlers().getDataHandler().getFriendsForCurrentUser()
          .get().getData().getKeys()) {
        String name = Hyperium.INSTANCE.getHandlers().getDataHandler().getFriendsForCurrentUser()
            .get().getData().optJSONObject(s).optString("name");
        if (!name.isEmpty()) {
          tabUsernames.add(name);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    complete.addAll(tabUsernames);
    return TabCompletionUtil.getListOfStringsMatchingLastWord(args, complete);
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
