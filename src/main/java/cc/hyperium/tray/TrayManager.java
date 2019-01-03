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

package cc.hyperium.tray;

import cc.hyperium.Hyperium;
import cc.hyperium.Metadata;
import cc.hyperium.config.Settings;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.HypixelFriendRequestEvent;
import cc.hyperium.event.HypixelPartyInviteEvent;
import cc.hyperium.event.InvokeEvent;
import net.minecraft.client.Minecraft;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;

// This class is for the Hyperium tray icon code.  

public class TrayManager {

    private TrayIcon tray;

    public TrayManager() {
        EventBus.INSTANCE.register(this);
    }

    public TrayIcon getTray() {
        return tray;
    }

    public void init() throws Exception {
        if (SystemTray.isSupported()) {
            Hyperium Hyperiumutils = Hyperium.INSTANCE;
            tray = new TrayIcon(ImageIO.read(getClass().getResourceAsStream("/assets/hyperium/icons/icon-16x.png")), ""); //resolution fix
            PopupMenu menu = new PopupMenu();

            MenuItem aboutItem = new MenuItem("About");
            aboutItem.addActionListener(action -> trayDisplayAboutInfo());

            MenuItem exitItem = new MenuItem("Exit");
            exitItem.addActionListener(action -> Minecraft.getMinecraft().shutdown());

            // Add items
            menu.add(aboutItem);
            menu.add(exitItem);

            tray.setPopupMenu(menu);
            SystemTray.getSystemTray().add(tray);
        }
    }

    /**
     * Party requests
     *
     * @param event the event
     */
    @InvokeEvent
    public void onPartyRequest(HypixelPartyInviteEvent event) {
        if (this.tray != null && Settings.SHOW_INGAME_CONFIRMATION_POPUP) {
            this.tray.displayMessage("Hypixel", "Party request from " + event.getFrom(), TrayIcon.MessageType.NONE);
        }
    }

    /**
     * Friend requests
     *
     * @param event the event
     */
    @InvokeEvent
    public void onFriendRequest(HypixelFriendRequestEvent event) {
        if (this.tray != null && Settings.SHOW_INGAME_CONFIRMATION_POPUP) {
            this.tray.displayMessage("Hypixel", "Friend request from " + event.getFrom(), TrayIcon.MessageType.NONE);
        }
    }

    private void trayDisplayAboutInfo() {
        JOptionPane popup = new JOptionPane();
        JOptionPane.showMessageDialog(popup, "Hyperium is a Hypixel Based 1.8.9 Client developed by Sk1er, CoalOres, Cubxity, KevinPriv and boomboompower. Version: " + Metadata
            .getVersion(), "Hyperium - About", JOptionPane.PLAIN_MESSAGE);
    }
}
