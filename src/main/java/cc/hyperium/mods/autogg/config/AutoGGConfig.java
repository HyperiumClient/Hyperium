package cc.hyperium.mods.autogg.config;

import cc.hyperium.Hyperium;
import cc.hyperium.config.ConfigOpt;

/**
 * Minified config class for AutoGG, uses Hyperium's config things
 */
public class AutoGGConfig {

    @ConfigOpt
    private int delay = 1;

    @ConfigOpt
    private boolean toggled = true;

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
        this.toggled = !this.toggled;
    }

    /**
     * Getter for the toggled method, true for enabled
     *
     * @return true if AutoGG is enabled
     */
    public boolean isToggled() {
        return this.toggled;
    }
}
