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
import me.semx11.autotip.Autotip;
import me.semx11.autotip.util.ChatColor;
import me.semx11.autotip.util.ClientMessage;
import net.minecraft.command.ICommandSender;

import java.util.Collections;
import java.util.List;

public class LimboCommand implements BaseCommand {

    public static boolean executed;

    public String getCommandName() {
        return "limbo";
    }

    public int getRequiredPermissionLevel() {
        return 0;
    }

    public String getCommandUsage(ICommandSender sender) {
        return "limbo";
    }

    public void onCommand(ICommandSender sender, String[] args) {
        if (Autotip.onHypixel) {
            executed = true;
            Autotip.mc.thePlayer.sendChatMessage(ChatColor.RED.toString());
        } else {
            ClientMessage.send(ChatColor.RED + "You must be on Hypixel to use this command!");
        }
    }

    public List<String> onTabComplete(ICommandSender sender, String[] args) {
        return Collections.emptyList();
    }

    @Override
    public String getName() {
        return getCommandName();
    }

    @Override
    public String getUsage() {
        return getCommandUsage(null);
    }

    @Override
    public void onExecute(String[] args) {
        onCommand(null, args);
    }
}
