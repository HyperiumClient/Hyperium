/*
 * Hyperium Client, Free client with huds and popular mod
 *     Copyright (C) 2018  Hyperium Dev Team
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

package me.semx11.autotip.command;

import cc.hyperium.commands.BaseCommand;

import me.semx11.autotip.misc.TipTracker;
import cc.hyperium.utils.ChatColor;
import me.semx11.autotip.util.ClientMessage;
import me.semx11.autotip.util.TimeUtil;

import java.util.Collections;
import java.util.List;

public class TipHistoryCommand implements BaseCommand {
    
    @Override
    public String getName() {
        return "tiphistory";
    }
    
    @Override
    public String getUsage() {
        return "Usage: /tiphistory [page]";
    }
    
    @Override
    public List<String> getCommandAliases() {
        return Collections.singletonList("lasttip");
    }
    
    @Override
    public void onExecute(String[] args) {
        if (TipTracker.tipsSentHistory.size() > 0) {
            int page = 1;
            int pages = (int) Math.ceil((double) TipTracker.tipsSentHistory.size() / 7.0);
            
            if (args.length > 0) {
                try {
                    page = Integer.parseInt(args[0]);
                } catch (NumberFormatException ignored) {
                    page = -1;
                }
            }
            
            if (page < 1 || page > pages) {
                ClientMessage.send(ChatColor.RED + "Invalid page number.");
            } else {
                ClientMessage.separator();
                ClientMessage.send(ChatColor.GOLD + "Tip History " + ChatColor.GRAY
                    + "[Page " + page + " of " + pages + "]" + ChatColor.GOLD + ":");
                
                TipTracker.tipsSentHistory.entrySet().stream()
                    .skip((page - 1) * 7)
                    .limit(7)
                    .forEach(tip -> ClientMessage.send(tip.getValue() + ": " + ChatColor.GOLD
                        + TimeUtil.formatMillis(
                        System.currentTimeMillis() - tip.getKey()) + "."));
                
                ClientMessage.separator();
            }
        } else {
            ClientMessage.send(ChatColor.RED + "You haven't tipped anyone yet!");
        }
    }
}
