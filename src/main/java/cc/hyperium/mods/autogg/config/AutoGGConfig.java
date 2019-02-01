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
    public boolean ANTI_GG = false;
    @ConfigOpt
    @SliderSetting(name = "Delay", min = 0, max = 5, isInt = true, category = Category.AUTO_GG, mods = true)
    public int delay = 1;
    @ConfigOpt
    @ToggleSetting(name = "Enable", mods = true, category = Category.AUTO_GG)
    public boolean AUTOGG = true;
    @ConfigOpt
    @ToggleSetting(name = "Say Good Game instead of GG", mods = true, category = AUTO_GG)
    public boolean AUTOGG_GOODGAME_GG = false;
    @ConfigOpt
    @ToggleSetting(name = "Say Lowercase", mods = true, category = AUTO_GG)
    public boolean AUTOGG_LOWERCASE = false;

    public AutoGGConfig() {
        Hyperium.CONFIG.register(this);
    }

    /**
     * Getter for the AutoGG delay
     *
     * @return the delay, capped into the limitations
     */
    public int getDelay() {
        return this.delay < 0 ? 1 : this.delay > 5 ? 1 : this.delay;
    }

    /**
     * Sets the delay of the AutoGG
     *
     * @param delay the delay of AutoGG, capped within limitations
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
        this.AUTOGG = !this.AUTOGG;
    }

    /**
     * Getter for the AUTOGG method, true for enabled
     *
     * @return true if AutoGG is enabled
     */
    public boolean isToggled() {
        return this.AUTOGG;
    }
}
