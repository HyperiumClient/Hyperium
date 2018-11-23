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
import cc.hyperium.handlers.handlers.tracking.StatisticViewingGui;

public class CommandStatistics implements BaseCommand {

    /**
     * Gets the name of the command (text after slash).
     *
     * @return The command name
     */
    @Override
    public String getName() {
        return "statistics";
    }

    /**
     * Gets the usage string for the command.
     *
     * @return The command usage
     */
    @Override
    public String getUsage() {
        return "/statistics";
    }

    /**
     * Callback when the command is invoked
     */
    @Override
    public void onExecute(String[] args) {
        new StatisticViewingGui().show();
    }
}
