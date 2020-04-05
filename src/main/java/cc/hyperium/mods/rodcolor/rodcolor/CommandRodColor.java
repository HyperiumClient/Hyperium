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
package cc.hyperium.mods.rodcolor.rodcolor;

import cc.hyperium.commands.BaseCommand;
import com.google.common.collect.ImmutableList;

import java.util.Collections;
import java.util.List;

/**
 * Class which handles command input for "/rodcolor"
 */
public class CommandRodColor implements BaseCommand {

    /**
     * Gets the name of the command
     */
    @Override
    public String getName() {
        return "rodcolor";
    }

    /**
     * Gets the usage string for the command.
     */
    @Override
    public String getUsage() {
        return "/rodcolor";
    }

    @Override
    public List<String> getCommandAliases() {
        return ImmutableList.of();
    }

    /**
     * Callback when the command is invoked
     *
     * @param args The arguments that were passed
     */
    @Override
    public void onExecute(String[] args) {
        RodColor.openGUI = true;
    }

    @Override
    public List<String> onTabComplete(String[] args) {
        return Collections.emptyList();
    }

}
