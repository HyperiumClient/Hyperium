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

package cc.hyperium.mods.levelhead.command;

import cc.hyperium.Hyperium;
import cc.hyperium.commands.BaseCommand;
import cc.hyperium.mods.levelhead.Levelhead;
import cc.hyperium.mods.levelhead.guis.LevelheadGui;

public class LevelheadCommand implements BaseCommand {
    @Override
    public String getName() {
        return "levelhead";
    }

    @Override
    public String getUsage() {
        return "/" + getName();
    }

    @Override
    public void onExecute(String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("dumpcache")) {
            Levelhead.getInstance().getDisplayManager().clearCache();
            Hyperium.INSTANCE.getHandlers().getGeneralChatHandler().sendMessage("Cleared Cache");
            return;
        }

        Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new LevelheadGui());
    }
}
