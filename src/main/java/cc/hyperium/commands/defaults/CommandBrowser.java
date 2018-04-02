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

import cc.hyperium.Hyperium;
import cc.hyperium.commands.BaseCommand;
import cc.hyperium.commands.CommandUsageException;
import cc.hyperium.mods.sk1ercommon.Multithreading;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandBrowser implements BaseCommand {

    private List<String> tabComplete = Arrays.asList("br", "bro", "brow", "brows");

    @Override
    public String getName() {
        return "browser";
    }

    @Override
    public String getUsage() {
        return "Usage: /browse <URL>";
    }

    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList("browse", "popupbrowser");
    }

    @Override
    public void onExecute(String[] args) throws CommandUsageException {
        if (args.length < 1) {
            throw new CommandUsageException();
        }
        String url = args[0];
        System.out.println("Browsing " + url);
        Multithreading.runAsync(() -> Hyperium.INSTANCE.getHandlers().getBrowserManager().browse(url));
    }

    @Override
    public List<String> onTabComplete(String[] args) {
        return tabComplete;
    }
}
