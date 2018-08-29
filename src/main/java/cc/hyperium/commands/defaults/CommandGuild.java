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
import cc.hyperium.handlers.handlers.chat.GeneralChatHandler;
import net.minecraft.client.gui.GuiNewChat;

/**
 * A simple command to clear your chat history & sent commands,
 * simply calls the {@link GuiNewChat#clearChatMessages()} method
 *
 * @author boomboompower
 */
public class CommandGuild implements BaseCommand {

    @Override
    public String getName() {
        return "hguild";
    }

    @Override
    public String getUsage() {
        return "/hguild <player,name> <player,name>";
    }

    @Override
    public void onExecute(String[] args) {
        if (args.length <= 1) {
            GeneralChatHandler.instance().sendMessage(getUsage());
        } else {
            if (args.length == 2 && args[0].equalsIgnoreCase("player")) {
                Hyperium.INSTANCE.getHandlers().getStatsHandler().loadGuildByPlayer(args[1]);
            } else {
                if (args[0].equalsIgnoreCase("name")) {
                    StringBuilder builder = new StringBuilder();
                    for (int i = 1; i < args.length; i++) {
                        builder.append(args[i]);
                    }
                    Hyperium.INSTANCE.getHandlers().getStatsHandler().loadGuildByName(builder.toString());
                } else {
                    GeneralChatHandler.instance().sendMessage(getUsage());
                }
            }
        }
    }
}
