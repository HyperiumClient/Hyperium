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

package cc.hyperium.commands.defaults;

import cc.hyperium.commands.BaseCommand;
import cc.hyperium.commands.CommandException;
import cc.hyperium.gui.hyperium.HyperiumMainGui;
import cc.hyperium.utils.ChatColor;

import java.util.Collections;
import java.util.List;

/**
 * A command to open the clients main configuration menu
 *
 * @author Sk1er
 */
public class CommandConfigGui implements BaseCommand {

    /**
     * Gets the name of the command (text after slash).
     *
     * @return The command name
     */
    @Override
    public String getName() {
        return "hyperiumconfig";
    }

    /**
     * Gets the usage string for the command.
     *
     * @return The command usage
     */
    @Override
    public String getUsage() {
        return ChatColor.RED + "Usage: /hyperiumconfig";
    }

    /**
     * A list of aliases to the main command. This will not be used if the list
     * is empty or {@code null}.
     *
     * @return The command aliases, which behave the same as the {@link #getName()}.
     */
    @Override
    public List<String> getCommandAliases() {
        // Allow an alias for the main config gui
        return Collections.singletonList("hyperium");
    }

    /**
     * Callback when the command is invoked
     *
     * @throws CommandException for errors inside the command, these errors
     *                          will log directly to the players chat (without a prefix)
     */
    @Override
    public void onExecute(String[] args) throws CommandException {
        HyperiumMainGui.INSTANCE.show();
    }
}