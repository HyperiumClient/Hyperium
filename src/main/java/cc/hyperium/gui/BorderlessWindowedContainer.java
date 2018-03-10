/*
 * Hyperium Client, Free client with huds and popular mod
 * Copyright (C) 2018  Hyperium Dev Team
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as published
 *  by the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package cc.hyperium.gui;

import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.TickEvent;
import cc.hyperium.gui.settings.items.GeneralSetting;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import java.awt.*;

public class BorderlessWindowedContainer {

    @InvokeEvent
    public void onTick(TickEvent event) {

        if (System.getProperty("org.lwjgl.opengl.Window.undecorated") == null) {
            System.setProperty("org.lwjgl.opengl.Window.undecorated", "false");
        }

        // Will only active if borderless windowed is enabled and has been disabled prior to enabling.
        if (GeneralSetting.borderlessWindowedEnabled && System.getProperty("org.lwjgl.opengl.Window.undecorated").equals("false")) {
            try {
                System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                Display.setDisplayMode(new DisplayMode((int) screenSize.getWidth(), (int) screenSize.getHeight()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (!GeneralSetting.borderlessWindowedEnabled && System.getProperty("org.lwjgl.opengl.Window.undecorated").equals("true")) {
            System.setProperty("org.lwjgl.opengl.Window.undecorated", "false");
            try {
                Display.setDisplayMode(new DisplayMode(Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight));
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
