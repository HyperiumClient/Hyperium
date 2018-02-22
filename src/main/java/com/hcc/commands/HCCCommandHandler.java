/*
 * Hypixel Community Client, Client optimized for Hypixel Network
 * Copyright (C) 2018 HCC Dev Team
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

package com.hcc.commands;

import com.hcc.event.InvokeEvent;
import com.hcc.event.SendChatMessageEvent;
import com.hcc.handlers.handlers.chat.GeneralChatHandler;
import com.hcc.utils.ChatColor;

import java.util.Map.Entry;
import net.minecraft.client.Minecraft;

import java.util.*;

/**
 * This is our custom client-side command implementation, it handles most of the
 * command logic and other firing methods. Commands should be register by doing
 * <i>HCC.INSTANCE.getHandlers().getHCCCommandHandler().registerCommand({@link BaseCommand})</i>
 */
public class HCCCommandHandler {

    private Map<String, BaseCommand> commands = new HashMap<>();

    private GeneralChatHandler chatHandler;
    private Minecraft mc;

    public HCCCommandHandler() {
        this.mc = Minecraft.getMinecraft();
        this.chatHandler = GeneralChatHandler.instance();
    }

    @InvokeEvent
    public void onChat(SendChatMessageEvent event) {
        String chatLine = event.getMessage();

        if (chatLine.startsWith("/") && chatLine.length() > 1) {
            String commandLine = chatLine.split("/")[1];
            String commandName;
            String[] args = new String[] {};

            // Check if arguments are provided.
            if (commandLine.contains(" ")) {
                String[] syntax = commandLine.split(" ");
                commandName = syntax[0];
                args = Arrays.copyOfRange(syntax, 1, syntax.length);
                // Example: If command is "/print hello 2", commandName will equal "print" and args will equal ["hello","2"]
            } else {
                commandName = commandLine;
            }

            // Loop through our commands, if the identifier matches the expected command, active the base
            for (Map.Entry<String, BaseCommand> entry : this.commands.entrySet()) {

                // Check if the expected command matches the command identifier for this entry
                if (commandName.equals(entry.getKey())) {

                    // It matched, we'll grab the command instance
                    BaseCommand command = entry.getValue();

                    // It is one of our commands, we'll cancel the event so it isn't
                    // sent to the server, and we'll close the currently opened gui
                    event.setCancelled(true);
                    this.mc.displayGuiScreen(null);

                    try {
                        command.onExecute(args);
                    } catch (CommandUsageException usageEx) {
                        // Throw a UsageException to trigger
                        this.chatHandler.sendMessage(ChatColor.RED + command.getUsage(), false);
                    } catch (CommandException knownEx) {
                        if (knownEx.getMessage() != null) {
                            this.chatHandler.sendMessage(ChatColor.RED + knownEx.getMessage(), false);
                        }
                    } catch (Exception exception) {
                        exception.printStackTrace();
                        this.chatHandler.sendMessage(ChatColor.RED + "An internal error occured whilst performing this command", false);
                    }
                }
            }
        }
    }

    /**
     * Registers the command to this CommandHandler instance.
     *      also registers any aliases if applicable
     *
     * @param command The command to register
     */
    public void registerCommand(BaseCommand command) {
        this.commands.put(command.getName(), command);

        if (command.getCommandAliases() != null && !command.getCommandAliases().isEmpty()) {
            for (String alias : command.getCommandAliases()) {
                this.commands.put(alias, command);
            }
        }
    }
    
    /**
     * Removes a register command & all aliases
     *
     * @param command the command to unregister
     */
    public void removeCommand(BaseCommand command) {
        for (Entry<String, BaseCommand> entry : this.commands.entrySet()) {
            if (entry.getValue().equals(command)) {
                this.commands.remove(entry.getKey());
            }
        }
    }
}
