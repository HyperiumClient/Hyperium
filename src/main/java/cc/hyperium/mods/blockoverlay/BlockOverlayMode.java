package cc.hyperium.mods.blockoverlay;

/*
 * Created by Cubxity on 21/04/2018
 */
public enum BlockOverlayMode {
    NONE("None"),
    DEFAULT("Default"),
    OUTLINE("Outline"),
    FULL("Full");

    String name;

    BlockOverlayMode(final String name) {
        this.name = name;
    }

    public static BlockOverlayMode getNextMode(final BlockOverlayMode mode) {
        return values()[(mode.ordinal() + 1) % values().length];
    }
}
