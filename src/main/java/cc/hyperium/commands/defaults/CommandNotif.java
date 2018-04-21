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
import cc.hyperium.mods.sk1ercommon.Multithreading;
import net.minecraft.client.gui.GuiNewChat;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

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
        Multithreading.runAsync(() -> {
            try {
                URL url = new URL("http://www.vsauce.com/img/highlight-logo.png");
                HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
                httpcon.addRequestProperty("User-Agent", "");
                Hyperium.INSTANCE.getNotification().display("Hey, Vsauce! Michael here.", "Are you a camel?",
                        5, ImageIO.read(httpcon.getInputStream()), null, new Color(137, 244, 66));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
