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

import cc.hyperium.commands.BaseCommand;
import cc.hyperium.handlers.handlers.chat.GeneralChatHandler;
import net.minecraft.client.Minecraft;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

public class CommandLogs implements BaseCommand {

    @Override
    public String getName() {
        return "logs";
    }

    @Override
    public String getUsage() {
        return "Usage: /logs";
    }

    @Override
    public void onExecute(String[] args) {
        StringBuilder message = new StringBuilder();
        try {
            FileReader in = new FileReader(new File(Minecraft.getMinecraft().mcDataDir, "logs" + File.separator + "latest.log"));
            BufferedReader reader = new BufferedReader(in);
            String line;

            while ((line = reader.readLine()) != null) {
                message.append(line).append("\n");
            }

            reader.close();
            in.close();
        } catch (IOException e) {
            GeneralChatHandler.instance().sendMessage("Error whilst reading log.");
            e.printStackTrace();
            return;
        }

        message = new StringBuilder(message.toString().replaceAll(System.getProperty("user.name"), "{USERNAME}"));

        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(message.toString()), null);
        GeneralChatHandler.instance().sendMessage("Data copied to clipboard. Please paste in hastebin.com (This has been opened), save and send in Discord");
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(new URL("https://hastebin.com").toURI());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
