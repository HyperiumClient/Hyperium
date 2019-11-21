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

package cc.hyperium.commands.defaults;

import cc.hyperium.Hyperium;
import cc.hyperium.commands.BaseCommand;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandGarbageCollect implements BaseCommand {

    @Override
    public String getName() {
        return "gcollect";
    }

    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList("garbagecollect", "gc");
    }

    @Override
    public String getUsage() {
        return "/gcollect";
    }

    @Override
    public void onExecute(String[] args) {
        Hyperium.INSTANCE.getHandlers().getGeneralChatHandler().sendMessage("Performing a Garbage Collect.");
        long start = System.currentTimeMillis();
        System.gc();
        long end = System.currentTimeMillis();
        float sec = (end - start) / 1000F;
        Hyperium.INSTANCE.getHandlers().getGeneralChatHandler().sendMessage("Garbage Collect took " + sec + " seconds.");
    }
}
