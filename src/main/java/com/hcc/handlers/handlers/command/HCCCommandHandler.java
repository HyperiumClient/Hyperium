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
import com.hcc.utils.Utils;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HCCCommandHandler {
    public static List<BaseCommand> commands = new ArrayList<>();

    @InvokeEvent
    public void onChat(SendChatMessageEvent event){
        String chatLine = event.getMessage();

        //Cancel sending message through normal chat.
        event.setCancelled(true);
        Minecraft.getMinecraft().currentScreen = null;

        if(chatLine.startsWith("/")){
            System.out.println("[COMMAND] RECEIVED COMMAND FROM USER");

            String commandLine = chatLine.split("/")[1];
            String commandName;
            String[] args = null;

            // Check if arguments are provided.
            if(commandLine.contains(" ")){
                System.out.println("[COMMAND] MULTIPLE ARGUMENTS DETECTED");
                commandName = commandLine.split(" ")[0];
                args = removeElement(commandLine.split(" "),commandName);
                // Example: If command is "/print hello 2", commandName will equal "print" and args will equal ["hello","2"]
            } else{
                commandName = commandLine;
            }

            for(BaseCommand command: commands){
                if(commandName.equals(command.getName())){
                    if(syntaxCheck(command,args)){
                        System.out.println("[COMMAND] COMMAND EXISTENCE VERIFIED, EXECUTING COMMAND...");
                        //Execute the command.
                        command.onExecute(args);
                    }
                    else{
                        GeneralChatHandler.instance().sendMessage("Incorrect command syntax! Please use: /" + command.getUsage());
                    }
                }
            }
        }
    }
    public boolean syntaxCheck(BaseCommand command, String[] sentArgs){
        String usage = command.getUsage();
        int argCount = Utils.INSTANCE.countSubstrings(usage,"<");
        int sentArgsLength = 0;

        if(sentArgs != null){
            sentArgsLength = sentArgs.length;
        }

        if(sentArgsLength!= argCount){
            return false;
        }

        return true;
    }

    public void registerCommand(BaseCommand command){
        commands.add(command);
        System.out.println("[COMMAND] REGISTERED COMMAND " + command.getName() + ".");
    }

    public static String[] removeElement(String[] input, String remove){
        String[] newArray;
        List<String> list = new ArrayList<>(Arrays.asList(input));
        list.remove(remove);
        newArray = list.toArray(input);
        return newArray;
    }

}
