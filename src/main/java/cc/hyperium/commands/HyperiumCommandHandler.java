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

package cc.hyperium.commands;

import cc.hyperium.Hyperium;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.SendChatMessageEvent;
import cc.hyperium.handlers.handlers.chat.GeneralChatHandler;
import cc.hyperium.utils.ChatColor;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.command.CommandBase;
import net.minecraft.util.EnumChatFormatting;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * This is our custom client-side command implementation, it handles most of the
 * command logic and other firing methods. Commands should be register by doing
 * <i>Hyperium.INSTANCE.getHandlers().getHyperiumCommandHandler().registerCommand({@link BaseCommand})</i>
 */
public class HyperiumCommandHandler {

    // If a command is in this
    private final Set<String> disabledCommands = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);

    private final Map<String, BaseCommand> commands = new HashMap<>();

    private final GeneralChatHandler chatHandler;
    private final Minecraft mc;

    private String[] latestAutoComplete;

    public boolean runningCommand = false;

    public HyperiumCommandHandler() {
        this.mc = Minecraft.getMinecraft();
        this.chatHandler = GeneralChatHandler.instance();

        loadDisabledCommands();
    }

    @InvokeEvent
    public void onChat(SendChatMessageEvent event) {
        final String chatLine = event.getMessage();
        // Attempt to execute command if necessary
        if (chatLine.startsWith("/") && chatLine.length() > 1 && executeCommand(chatLine)) {
            // It is one of our commands, we'll cancel the event so it isn't
            // sent to the server, and we'll close the currently opened gui
            event.setCancelled(true);

            if (runningCommand) {
                this.mc.displayGuiScreen(null);
                runningCommand = false;
            }
        }
    }

    /**
     * Execute the provided command, if it exists. Initial leading slash will be removed if it is sent.
     *
     * @param command Command to attempt to execute
     * @return Whether the command was successfully executed
     */
    public boolean executeCommand(String command) {
        final String commandLine = command.startsWith("/") ? command.substring(1) : command;
        String commandName;
        String[] args = new String[]{};

        // Check if arguments are provided.
        if (commandLine.contains(" ")) {
            String[] syntax = commandLine.split(" ");
            commandName = syntax[0];
            args = Arrays.copyOfRange(syntax, 1, syntax.length);
            // SpotifyInformation: If command is "/print hello 2", commandName will equal "print" and args will equal ["hello","2"]
        } else {
            commandName = commandLine;
        }

        // Disabled commands will be ignored
        if (isCommandDisabled(commandName)) {
            return false;
        }

        // Loop through our commands, if the identifier matches the expected command, active the base
        String[] finalArgs = args;
        return this.commands.entrySet().stream().filter(e -> commandName.equals(e.getKey()) && !e.getValue().tabOnly()).findFirst().map(e -> {
            final BaseCommand baseCommand = e.getValue();

            try {
                baseCommand.onExecute(finalArgs);
            } catch (CommandUsageException usageEx) {
                // Throw a UsageException to trigger
                this.chatHandler.sendMessage(ChatColor.RED + baseCommand.getUsage(), false);
            } catch (CommandException knownEx) {
                if (knownEx.getMessage() != null) {
                    this.chatHandler.sendMessage(ChatColor.RED + knownEx.getMessage(), false);
                }
            } catch (Exception exception) {
                exception.printStackTrace();
                this.chatHandler.sendMessage(ChatColor.RED + "An internal error occurred whilst performing this command", false);
                return false;
            }

            return true;
        }).orElse(false);
    }

    /**
     * Registers the command to this CommandHandler instance.
     * also registers any aliases if applicable
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
     * Returns true if this command is in the disabled list. Used to ignore commands
     *
     * @param input the command to check
     * @return true if the command should be ignored
     */
    public boolean isCommandDisabled(String input) {
        if (input == null || input.isEmpty() || input.trim().isEmpty() ||
            input.equalsIgnoreCase("disablecommand") || input.equalsIgnoreCase("hyperium")) {
            return false;
        }

        return this.disabledCommands.contains(input.trim());
    }

    /**
     * If this command is already disabled, we'll remove it from the disabled list
     * and return false to indicate the command is not disabled anymore. If the list
     * does not contain the command we'll add it and return true to indicate it now is.
     *
     * @param input the command to add to the ignored list
     * @return true if now disabled or false if it no longer is
     */
    public boolean addOrRemoveCommand(String input) {
        if (input == null || input.isEmpty() || input.trim().isEmpty() ||
            input.equalsIgnoreCase("disablecommand") || input.equalsIgnoreCase("hyperium")) {
            return false;
        }

        input = input.trim();

        if (isCommandDisabled(input)) {
            this.disabledCommands.remove(input);
            return false;
        } else {
            this.disabledCommands.add(input);
            return true;
        }
    }

    /**
     * @author Forge
     */
    public void autoComplete(String leftOfCursor) {
        latestAutoComplete = null;
        if (leftOfCursor.length() == 0)
            return;
        if (leftOfCursor.charAt(0) == '/') {
            leftOfCursor = leftOfCursor.substring(1);

            if (mc.currentScreen instanceof GuiChat) {
                List<String> completions = getTabCompletionOptions(leftOfCursor);
                if (completions != null && !completions.isEmpty()) {
                    if (leftOfCursor.indexOf(' ') == -1) {
                        for (int i = 0; i < completions.size(); i++) {
                            completions.set(i, EnumChatFormatting.GRAY + "/" + completions.get(i) + EnumChatFormatting.RESET);
                        }
                    }

                    Collections.sort(completions);
                    latestAutoComplete = completions.toArray(new String[0]);
                }
            }
        }
    }

    /**
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

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void loadDisabledCommands() {
        File disabledCommandFile = new File(Hyperium.folder, "disabledcommands.txt");

        try {
            if (!disabledCommandFile.getParentFile().exists()) {
                if (!disabledCommandFile.getParentFile().mkdirs()) {
                    return;
                }
            }

            if (!disabledCommandFile.exists()) {
                disabledCommandFile.createNewFile();
                return;
            }

            FileReader fileReader = new FileReader(disabledCommandFile);
            BufferedReader reader = new BufferedReader(fileReader);

            this.disabledCommands.addAll(reader.lines().collect(Collectors.toList()));

            reader.close();
            fileReader.close();
        } catch (IOException ignored) {

        }
        disabledCommands.add("l");
        disabledCommands.add("lobby");
        disabledCommands.add("hub");
        disabledCommands.add("spawn");


    }

    public void saveDisabledCommands() {
        File disabledCommandFile = new File(Hyperium.folder, "disabledcommands.txt");

        try {
            if (!disabledCommandFile.getParentFile().exists()) {
                if (!disabledCommandFile.getParentFile().mkdirs()) {
                    return;
                }
            }

            if (!disabledCommandFile.exists()) {
                if (!disabledCommandFile.createNewFile()) {
                    return;
                }
            }

            FileWriter fileWriter = new FileWriter(disabledCommandFile);
            BufferedWriter writer = new BufferedWriter(fileWriter);

            for (String s : this.disabledCommands) {
                writer.write(s + System.lineSeparator());
            }

            writer.close();
            fileWriter.close();
        } catch (IOException ignored) {
        }
    }

    public Map<String, BaseCommand> getCommands() {
        return commands;
    }
}
