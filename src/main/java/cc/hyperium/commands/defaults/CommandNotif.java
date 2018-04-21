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
import net.minecraft.client.gui.GuiNewChat;

import java.awt.*;

/**
 * A simple command to clear your chat history & sent commands,
 * simply calls the {@link GuiNewChat#clearChatMessages()} method
 *
 * @author boomboompower
 */
public class CommandNotif implements BaseCommand {

    @Override
    public String getName() {
        return "notif";
    }
    
    @Override
    public String getUsage() {
        return null;
    }
    
    @Override
    public void onExecute(String[] args) {
        Hyperium.INSTANCE.getNotification().display("Hey, Vsauce! Micheal here.", "Are you a camel? Are you a camel? Are you a camel? Are you a camel? Are you a camel? Are you a camel?",
                5, null, null, new Color(255, 0, 0));
    }
}
