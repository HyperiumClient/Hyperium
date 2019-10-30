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

package cc.hyperium.mods.autogg.config;

import cc.hyperium.Hyperium;
import cc.hyperium.config.Category;
import cc.hyperium.config.ConfigOpt;
import cc.hyperium.config.SliderSetting;
import cc.hyperium.config.ToggleSetting;

import static cc.hyperium.config.Category.AUTO_GG;

/**
 * Minified config class for AutoGG, uses Hyperium's config things
 */
public class AutoGGConfig {


    @ConfigOpt
    @ToggleSetting(name = "Hide GG's at end of game", mods = true, category = AUTO_GG)
    public boolean ANTI_GG;

    @ConfigOpt
    @SliderSetting(name = "Delay", min = 0, max = 5, isInt = true, category = AUTO_GG, mods = true)
    public int delay = 1;

    @ConfigOpt
    @ToggleSetting(name = "Enable", mods = true, category = AUTO_GG)
    public boolean toggled = true;

    @ConfigOpt
    @ToggleSetting(name = "Say Good Game instead of GG", mods = true, category = AUTO_GG)
    public boolean sayGoodGameInsteadOfGG;

    @ConfigOpt
    @ToggleSetting(name = "Say Lowercase", mods = true, category = AUTO_GG)
    public boolean lowercase = true;

    public AutoGGConfig() {
        Hyperium.CONFIG.register(this);
    }

    /**
     * Getter for the AutoGG delay
     *
     * @return the delay, capped into the limitations
     */
    public int getDelay() {
        return delay < 0 ? 1 : delay > 5 ? 1 : delay;
    }

    /**
     * Sets the delay of the AutoGG
     *
     * @param delay the delay of AutoGG, capped within limitations
     */
    public void setDelay(int delay) {
        if (delay >= 0 && this.delay <= 5) this.delay = delay;
    }

    /**
     * Flips the toggle
     */
    public void flipToggle() {
        toggled = !toggled;
    }

    /**
     * Getter for the toggled method, true for enabled
     *
     * @return true if AutoGG is enabled
     */
    public boolean isToggled() {
        return toggled;
    }
}
