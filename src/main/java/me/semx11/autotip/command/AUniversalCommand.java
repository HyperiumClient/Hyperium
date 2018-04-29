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

import me.semx11.autotip.Autotip;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;

import javax.annotation.Nullable;
import java.util.List;

public abstract class AUniversalCommand extends CommandBase {

    // Minecraft 1.8 | 1.8.8 | 1.8.9
    // func_71515_b
    public void processCommand(ICommandSender sender, String[] args) {
        Autotip.THREAD_POOL.submit(() -> onCommand(sender, args));
    }

    // Minecraft 1.9 | 1.9.4 | 1.10 | 1.10.2 | 1.11 | 1.11.2
    // func_184881_a
    public void func_184881_a(MinecraftServer server, ICommandSender sender, String[] args) {
        Autotip.THREAD_POOL.submit(() -> onCommand(sender, args));
    }

    // Minecraft 1.8 | 1.8.8 | 1.8.9
    // func_180525_a
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args,
                                                BlockPos pos) {
        return onTabComplete(sender, args);
    }

    // Minecraft 1.9 | 1.9.4 | 1.10 | 1.10.2 | 1.11 | 1.11.2
    // Does not seem to work?
    // func_184883_a
    public List<String> func_184883_a(MinecraftServer server, ICommandSender sender, String[] args,
                                      @Nullable BlockPos pos) {
        return onTabComplete(sender, args);
    }

    public abstract void onCommand(ICommandSender sender, String[] args);

    public abstract List<String> onTabComplete(ICommandSender sender, String[] args);

}