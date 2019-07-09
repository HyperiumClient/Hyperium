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
