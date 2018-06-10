package cc.hyperium.mods.blockoverlay;

public enum BlockOverlayMode {
    NONE("None"),
    DEFAULT("Default"),
    OUTLINE("Outline"),
    FULL("Full");

    private String name;

    BlockOverlayMode(final String name) {
        this.name = name;
    }

    public static BlockOverlayMode getNextMode(final BlockOverlayMode mode) {
        return values()[(mode.ordinal() + 1) % values().length];
    }

    public String getName() {
        return this.name;
    }
}
