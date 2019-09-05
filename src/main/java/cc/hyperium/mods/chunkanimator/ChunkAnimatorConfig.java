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

package cc.hyperium.mods.chunkanimator;

/*
 * Created by Cubxity on 12/1/2018
 */

import cc.hyperium.config.*;

import static cc.hyperium.config.Category.CHUNK_ANIMATOR;

public class ChunkAnimatorConfig {
    @ConfigOpt
    @ToggleSetting(name = "Enable", mods = true, category = CHUNK_ANIMATOR)
    public static boolean enabled;

    @ConfigOpt
    @SelectorSetting(name = "Mode", category = CHUNK_ANIMATOR, items = {
        "Up from ground",
        "Down from sky 1",
        "Down from sky 2",
        "From sides",
        "From direction you're facing"
    }, mods = true)
    public static String mode = "0";

    @ConfigOpt
    @SliderSetting(min = 0, max = 10000, name = "Animation Duration", category = CHUNK_ANIMATOR, mods = true, isInt = true)
    public static int animDuration = 1000;

    @ConfigOpt
    @ToggleSetting(name = "Disable Around Player", category = CHUNK_ANIMATOR, mods = true)
    public static boolean disableAroundPlayer;
}
