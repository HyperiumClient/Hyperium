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

package cc.hyperium.commands.defaults;

import cc.hyperium.Hyperium;
import cc.hyperium.commands.BaseCommand;
import cc.hyperium.mods.sk1ercommon.Multithreading;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandBrowser implements BaseCommand {

    @Override
    public String getName() {
        return "browser";
    }

    @Override
    public String getUsage() {
        return "/browse <URL>";
    }

    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList("browse", "popupbrowser");
    }

    @Override
    public void onExecute(String[] args) {
        if (args.length < 1) {
            return;
        }
        String url = args[0];
        System.out.println("Browsing " + url);
        Multithreading.runAsync(() -> {
            Hyperium.INSTANCE.getHandlers().getBrowserManager().browse(url);
        });

    }

    @Override
    public List<String> onTabComplete(String[] args) {
        return new ArrayList<>();
    }
}
