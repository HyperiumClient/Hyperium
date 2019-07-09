/*
 *       Copyright (C) 2018-present Hyperium <https://hyperium.cc/>
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU Lesser General Public License as published
 *       by the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU Lesser General Public License for more details.
 *
 *       You should have received a copy of the GNU Lesser General Public License
 *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.commands;

import java.util.ArrayList;
import java.util.List;

/**
 * The basic command implementation
 */
public interface BaseCommand {

    /**
     * Gets the name of the command
     */
    String getName();

    /**
     * Gets the usage string for the command.
     */
    String getUsage();

    /**
     * A list of aliases to the main command
     * this will not be used if null/empty
     */
    default List<String> getCommandAliases() {
        return new ArrayList<>();
    }

    /**
     * Callback when the command is invoked
     *
     * @throws CommandException for errors inside the command, these errors
     *                          will log directly to the players chat (without a prefix)
     */
    void onExecute(String[] args) throws CommandException;

    /**
     * Called when the player clicks tab in the chat menu, used to provide suggestions for a commands arguments
     *
     * @param args the arguments the player has entered
     * @return a String List containing all viable tab completions
     */
    default List<String> onTabComplete(String[] args) {
        return null;
    }

    /**
     * Tells the command handler not to register the command, and to use {@link #onTabComplete(String[])}
     *
     * @return true if this command should not be executed
     */
    default boolean tabOnly() {
        return false;
    }
}
