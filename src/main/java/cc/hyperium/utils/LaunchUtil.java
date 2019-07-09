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

package cc.hyperium.utils;

import cc.hyperium.Hyperium;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

import net.minecraft.client.Minecraft;

/**
 * @author KodingKing
 */
public class LaunchUtil {

    public static void launch() {
        try {
            boolean windows = InstallerUtils.getOS() == InstallerUtils.OSType.Windows;
            //Class<?> c = getClass();
            //String n = c.getName().replace('.', '/');
            String cs = "";
            for (URL u : ((URLClassLoader) Hyperium.class.getClassLoader()).getURLs()) {
                if (u.getPath().contains("Hyperium")) {
                    cs = u.getPath();
                }
            }
            System.out.println("cs=" + cs);
            Runtime.getRuntime().exec(new String[]{
                windows ? "cmd" : "bash",
                windows ? "/c" : "-c",
                "java",
                "-jar",
                cs,
                Minecraft.getMinecraft().mcDataDir.getAbsolutePath()
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
