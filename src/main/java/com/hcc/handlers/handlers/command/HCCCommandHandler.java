/*
 * Hypixel Community Client, Client optimized for Hypixel Network
 * Copyright (C) 2018  HCC Dev Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.hcc.handlers.handlers.command;

import com.hcc.event.InvokeEvent;
import com.hcc.event.SendChatMessageEvent;
import com.hcc.handlers.handlers.chat.GeneralChatHandler;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HCCCommandHandler {
    public static List<BaseCommand> commands = new ArrayList<>();

    @InvokeEvent
    public void onChat(SendChatMessageEvent event){
        String chatLine = event.getMessage();

        if(chatLine.startsWith("/") && chatLine.length() > 1){

            String commandLine = chatLine.split("/")[1];
            String commandName;
            String[] args = null;

            // Check if arguments are provided.
            if(commandLine.contains(" ")){
                String[] syntax = commandLine.split(" ");
                commandName = syntax[0];
                args = Arrays.copyOfRange(syntax, 1, syntax.length);
                // Example: If command is "/print hello 2", commandName will equal "print" and args will equal ["hello","2"]
            } else{
                commandName = commandLine;
            }

            for(BaseCommand command: commands){
                if(commandName.equals(command.getName())){
                    // Command is our command, cancel event.
                    event.setCancelled(true);
                    Minecraft.getMinecraft().displayGuiScreen(null);

                    try {
                        command.onExecute(args);
                    } catch (NullPointerException exception){
                        GeneralChatHandler.instance().sendMessage("Incorrect command syntax! Please use: /" + command.getUsage());
                    }
                }
            }
        }
    }

    public void registerCommand(BaseCommand command){
        commands.add(command);
        System.out.println("[COMMAND] Registered " + command.getName() + " command.");
    }

}
