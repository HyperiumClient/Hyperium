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

package cc.hyperium.addons.bossbar.config;

import cc.hyperium.Hyperium;
import cc.hyperium.config.ConfigOpt;

public class BossbarConfig {
    public BossbarConfig() {
        Hyperium.CONFIG.register(this);
    }

    @ConfigOpt
    public static boolean bossBarEnabled = true;
    @ConfigOpt
    public static boolean barEnabled = true;
    @ConfigOpt
    public static boolean textEnabled = true;
    @ConfigOpt
    public static int x = -1;
    @ConfigOpt
    public static int y = 12;

}
