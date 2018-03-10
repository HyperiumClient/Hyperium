/*
 *  Hypixel Community Client, Client optimized for Hypixel Network
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

package cc.hyperium.commands;

import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.SendChatMessageEvent;
import cc.hyperium.handlers.handlers.chat.GeneralChatHandler;
import cc.hyperium.utils.ChatColor;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.command.CommandBase;

import java.util.*;
import java.util.Map.Entry;

/**
 * This is our custom client-side command implementation, it handles most of the
 * command logic and other firing methods. Commands should be register by doing
 * <i>Hyperium.INSTANCE.getHandlers().getHyperiumCommandHandler().registerCommand({@link BaseCommand})</i>
 */
public class HyperiumCommandHandler {

    private Map<String, BaseCommand> commands = new HashMap<>();

    private GeneralChatHandler chatHandler;
    private Minecraft mc;

    private String[] latestAutoComplete;

    public HyperiumCommandHandler() {
        this.mc = Minecraft.getMinecraft();
        this.chatHandler = GeneralChatHandler.instance();
    }

    @InvokeEvent
    public void onChat(SendChatMessageEvent event) {
        String chatLine = event.getMessage();

        if (chatLine.startsWith("/") && chatLine.length() > 1) {
            String commandLine = chatLine.replaceFirst("/","");
            String commandName;
            String[] args = new String[] {};

            // Check if arguments are provided.
            if (commandLine.contains(" ")) {
                String[] syntax = commandLine.split(" ");
                commandName = syntax[0];
                args = Arrays.copyOfRange(syntax, 1, syntax.length);
                // SpotifyInformation: If command is "/print hello 2", commandName will equal "print" and args will equal ["hello","2"]
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

    /**
     *
     * @author Forge
     */
    public void autoComplete(String leftOfCursor) {
        latestAutoComplete = null;

        if (leftOfCursor.charAt(0) == '/') {
            leftOfCursor = leftOfCursor.substring(1);

            if (mc.currentScreen instanceof GuiChat) {
                List<String> completions = getTabCompletionOptions(leftOfCursor);
                if (completions != null && !completions.isEmpty()) {
                    if (leftOfCursor.indexOf(' ') == -1) {
                        for (int i = 0; i < completions.size(); i++) {
                            completions.set(i, "/" + completions.get(i));
                        }
                    }

                    Collections.sort(completions);
                    latestAutoComplete = completions.toArray(new String[completions.size()]);
                }
            }
        }
    }

    /**
     *
     * @author Forge
     */
    private List<String> getTabCompletionOptions(String input) {
        String[] astring = input.split(" ", -1);
        String s = astring[0];

        if (astring.length == 1) {
            List<String> list = Lists.newArrayList();

            for (Entry<String, BaseCommand> entry : this.commands.entrySet()) {
                if (CommandBase.doesStringStartWith(s, entry.getKey())) {
                    list.add(entry.getKey());
                }
            }

            return list;
        } else {
			BaseCommand command = this.commands.get(s);

			if (command != null) {
                return command.onTabComplete(dropFirstString(astring));
			}

			return null;
		}
    }

    /**
     *
     * @author Forge
     */
    private String[] dropFirstString(String[] input) {
        String[] astring = new String[input.length - 1];
        System.arraycopy(input, 1, astring, 0, input.length - 1);
        return astring;
    }

    public String[] getLatestAutoComplete() {
        return latestAutoComplete;
    }

    public void clear() {
        this.commands.clear();
    }
}
