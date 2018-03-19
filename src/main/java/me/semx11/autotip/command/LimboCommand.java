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

package me.semx11.autotip.command;

import cc.hyperium.commands.BaseCommand;

import me.semx11.autotip.Autotip;
import cc.hyperium.utils.ChatColor;
import me.semx11.autotip.util.ClientMessage;

public class LimboCommand implements BaseCommand {
    
    public static boolean executed;
    
    @Override
    public String getName() {
        return "limbo";
    }
    
    @Override
    public String getUsage() {
        return "Usage: /limbo";
    }
    
    @Override
    public void onExecute(String[] args) {
        if (Autotip.onHypixel) {
            executed = true;
            Autotip.mc.thePlayer.sendChatMessage(ChatColor.RED.toString());
        } else {
            ClientMessage.send(ChatColor.RED + "You must be on Hypixel to use this command!");
        }
    }
}
