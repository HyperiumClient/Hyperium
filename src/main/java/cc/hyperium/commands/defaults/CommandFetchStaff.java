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

import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.commands.BaseCommand;
import cc.hyperium.utils.StaffUtils;
import cc.hyperium.Hyperium;

import java.io.IOException;

public class CommandFetchStaff implements BaseCommand {

    @Override
    public String getName() {
        return "fetchstaff";
    }

    @Override
    public String getUsage() {
        return "/fetchstaff";
    }

    @Override
    public void onExecute(String[] args) {
        Multithreading.runAsync(() -> {
            try {
                Hyperium.INSTANCE.getHandlers().getGeneralChatHandler().sendMessage("Fetching staff and boosters...");
                StaffUtils.clearCache();
                Hyperium.INSTANCE.getHandlers().getGeneralChatHandler().sendMessage("Successfully fetched staff and boosters.");
            } catch (IOException e) {
                e.printStackTrace();
                Hyperium.LOGGER.info("[Staff] Failed to fetch staff & boosters");
                Hyperium.INSTANCE.getHandlers().getGeneralChatHandler().sendMessage("Failed to fetch staff & boosters!");
            }
        });
    }
}
