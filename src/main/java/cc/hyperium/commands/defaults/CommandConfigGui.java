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
import cc.hyperium.gui.main.HyperiumMainGui;
import cc.hyperium.utils.ChatColor;

import java.util.Collections;
import java.util.List;

/**
 * A command to open the clients main configuration menu
 *
 * @author Sk1er
 */
public class CommandConfigGui implements BaseCommand {

    @Override
    public String getName() {
        return "hyperiumconfig";
    }

    @Override
    public String getUsage() {
        return ChatColor.RED + "Usage: /hyperiumconfig";
    }

    @Override
    public List<String> getCommandAliases() {
        // Allow an alias for the main config gui
        return Collections.singletonList("hyperium");
    }

    @Override
    public void onExecute(String[] args) {
        HyperiumMainGui.INSTANCE.show();
    }
}
