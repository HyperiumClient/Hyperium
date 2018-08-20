package cc.hyperium.mods.autotpa.config;

import cc.hyperium.Hyperium;
import cc.hyperium.config.Category;
import cc.hyperium.config.ConfigOpt;
import cc.hyperium.config.SliderSetting;
import cc.hyperium.config.ToggleSetting;

import static cc.hyperium.config.Category.AUTO_GG;

public class AutoTPAConfig {
    @ConfigOpt
    @SliderSetting(name = "Delay", min = 0, max = 5, round = true, isInt = true, category = Category.AUTO_TPA, mods = true)
    public int delay = 1;
    @ConfigOpt
    @ToggleSetting(name = "Enable", mods = true, category = Category.AUTO_TPA)
    public boolean toggled = true;

    public AutoTPAConfig() {
        Hyperium.CONFIG.register(this);
    }

    /**
     * Getter for the AutoTPA delay
     *
     * @return the delay, capped into the limitations
     */
    public int getDelay() {
        return this.delay < 0 ? 1 : this.delay > 5 ? 1 : this.delay;
    }

    /**
     * Sets the delay of the AutoTPA
     *
     * @param delay the delay of AutoTPA, capped within limitations
     */
    public void setDelay(int delay) {
        if (delay >= 0 && this.delay <= 5) {
            this.delay = delay;
        }
    }

    /**
     * Flips the toggle
     */
    public void flipToggle() {
        this.toggled = !this.toggled;
    }

    /**
     * Getter for the toggled method, true for enabled
     *
     * @return true if AutoTPA is enabled
     */
    public boolean isToggled() {
        return this.toggled;
    }
}
