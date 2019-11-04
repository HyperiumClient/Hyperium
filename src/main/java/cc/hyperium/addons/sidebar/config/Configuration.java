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

package cc.hyperium.addons.sidebar.config;

import cc.hyperium.addons.sidebar.gui.GuiSidebar;

public class Configuration {

    public static boolean enabled = true;
    public static int offsetX;
    public static int offsetY;
    public static float scale = 1.0f;
    public static boolean redNumbers = true;
    public static boolean shadow;
    public static int rgb;
    public static int alpha = 50;
    public static boolean chromaEnabled;
    public static int chromaSpeed = 2;
    public static GuiSidebar.ChromaType chromaType = GuiSidebar.ChromaType.ONE;
}
