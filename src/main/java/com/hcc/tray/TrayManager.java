/*
 *     Hypixel Community Client, Client optimized for Hypixel Network
 *     Copyright (C) 2018  HCC Dev Team
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

package com.hcc.tray;

import net.minecraft.client.Minecraft;

import javax.imageio.ImageIO;
import java.awt.*;

public class TrayManager {
    private TrayIcon tray;
    public void init() throws Exception{
        if(SystemTray.isSupported()){
            tray = new TrayIcon(ImageIO.read(getClass().getResourceAsStream("/assets/hcc/icons/icon-32x.png")),"");
            PopupMenu menu = new PopupMenu();
            MenuItem exitItem = new MenuItem("exit");
            exitItem.addActionListener(action -> Minecraft.getMinecraft().shutdown());
            tray.setPopupMenu(menu);
            SystemTray.getSystemTray().add(tray);
        }
    }
}
