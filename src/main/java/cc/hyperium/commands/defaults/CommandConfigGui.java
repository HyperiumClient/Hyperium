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
import cc.hyperium.gui.ModConfigGui;

import java.util.Arrays;
import java.util.List;

/**
 * A command to open the clients main configuration menu
 *
 * @author Sk1er
 */
public class CommandConfigGui implements BaseCommand {

    private List<String> tabComplete = Arrays.asList("hyperium", "hype", "config", "hyperiumcon", "hy");

    @Override
    public String getName() {
        return "hyperiumconfig";
    }

    @Override
    public String getUsage() {
        return "hyperiumconfig";
    }

    @Override
    public void onExecute(String[] args) {
        new ModConfigGui().show();
    }

    @Override
    public List<String> onTabComplete(String[] args) {
        return tabComplete;
    }
}
