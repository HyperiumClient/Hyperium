/*
 *     Hypixel Community Client, Client optimized for Hypixel Network
 *     Copyright (C) 2018  HCC Dev Team
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.hcc.mods.levelhead.commands;

import com.hcc.handlers.handlers.HypixelDetector;
import com.hcc.handlers.handlers.chat.GeneralChatHandler;
import com.hcc.commands.BaseCommand;
import com.hcc.mods.levelhead.Levelhead;
import com.hcc.mods.levelhead.guis.LevelHeadGui;
import com.hcc.utils.ChatColor;
import com.hcc.mods.sk1ercommon.Sk1erMod;

/**
 * Created by Mitchell Katz on 5/8/2017.
 */
public class LevelHeadCommand implements BaseCommand {

    @Override
    public String getName() {
        return "levelhead";
    }

    @Override
    public String getUsage() {
        return "/" + getName();
    }

    @Override
    public void onExecute(String[] args) {
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("limit")) {
                GeneralChatHandler.instance().sendMessage(ChatColor.RED + "Count: " + Levelhead.getInstance().count);
                GeneralChatHandler.instance().sendMessage(ChatColor.RED + "Wait: " + Levelhead.getInstance().wait);
                GeneralChatHandler.instance().sendMessage(ChatColor.RED + "Hypixel: " + HypixelDetector.getInstance().isHypixel());
                GeneralChatHandler.instance().sendMessage(ChatColor.RED + "Remote Status: " + Sk1erMod.getInstance().isEnabled());
                GeneralChatHandler.instance().sendMessage(ChatColor.RED + "Local Stats: " + HypixelDetector.getInstance().isHypixel());
                GeneralChatHandler.instance().sendMessage(ChatColor.RED + "Header State: " + Levelhead.getInstance().getHeaderConfig());
                GeneralChatHandler.instance().sendMessage(ChatColor.RED + "Footer State: " + Levelhead.getInstance().getFooterConfig());
                GeneralChatHandler.instance().sendMessage(ChatColor.RED + "Callback: " + Sk1erMod.getInstance().getResponse());
                return;
            } else if (args[0].equalsIgnoreCase("dumpcache")) {
                Levelhead.getInstance().levelCache.clear();

                GeneralChatHandler.instance().sendMessage("Stringcache entries: " + Levelhead.getInstance().levelCache.size());
                return;
            }
        }
        new LevelHeadGui().display();
    }
}
