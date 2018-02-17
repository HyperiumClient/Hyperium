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

package com.hcc.commands

import com.hcc.gui.ModConfigGui
import net.minecraft.client.Minecraft
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender
import java.util.*

class ConfigCommand : CommandBase() {
    override fun processCommand(sender: ICommandSender?, args: Array<out String>?) {
        Minecraft.getMinecraft().displayGuiScreen(ModConfigGui())
    }

    override fun getCommandName(): String {
        return "CONFIG"
    }

    override fun getCommandUsage(sender: ICommandSender?): String {
        return "/CONFIG"
    }

    override fun getCommandAliases(): MutableList<String> {
        return Arrays.asList("clientconfig", "hcc", "hypixelcommunityclient", "hccconfig")
    }

    override fun getRequiredPermissionLevel(): Int {
        return 0
    }

    override fun canCommandSenderUseCommand(sender: ICommandSender?): Boolean {
        return true
    }
}