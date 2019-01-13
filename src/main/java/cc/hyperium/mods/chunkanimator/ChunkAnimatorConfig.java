package cc.hyperium.mods.chunkanimator;

/*
 * Created by Cubxity on 12/1/2018
 */

import cc.hyperium.config.*;

import static cc.hyperium.config.Category.CHUNK_ANIMATOR;

public class ChunkAnimatorConfig {
    @ConfigOpt
    @ToggleSetting(name = "Enable", mods = true, category = CHUNK_ANIMATOR)
    public static boolean enabled = false;

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
    public static boolean disableAroundPlayer = false;
}
