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
import cc.hyperium.config.ConfigOpt;

public class Configuration {

    @ConfigOpt
    public static boolean enabled = true;
    @ConfigOpt
    public static int offsetX;
    @ConfigOpt
    public static int offsetY;
    @ConfigOpt
    public static float scale = 1.0f;
    @ConfigOpt
    public static boolean redNumbers = true;
    @ConfigOpt
    public static boolean shadow;
    @ConfigOpt
    public static int rgb;
    @ConfigOpt
    public static int alpha = 50;
    @ConfigOpt
    public static boolean chromaEnabled;
    @ConfigOpt
    public static int chromaSpeed = 2;
    @ConfigOpt
    public static GuiSidebar.ChromaType chromaType = GuiSidebar.ChromaType.ONE;

}
