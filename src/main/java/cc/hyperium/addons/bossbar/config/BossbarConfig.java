package cc.hyperium.addons.bossbar.config;

import cc.hyperium.Hyperium;
import cc.hyperium.config.ConfigOpt;

public class BossbarConfig {

    public BossbarConfig() {
        Hyperium.CONFIG.register(this);
    }

    /**
     * Whether the bossbar is enabled or not
     */
    @ConfigOpt
    public static boolean bossBarEnabled = true;

    /**
     * Whether the bar is enabled or not
     */
    @ConfigOpt
    public static boolean barEnabled = true;

    /**
     * Whether if the text on the bar is enabled or not
     */
    @ConfigOpt
    public static boolean textEnabled = true;

    /**
     * The X position of the bossbar
     */
    @ConfigOpt
    public static int x = -1;

    /**
     * The Y position of the bossbar
     */
    @ConfigOpt
    public static int y = 12;

}
