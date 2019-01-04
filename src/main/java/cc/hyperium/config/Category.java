package cc.hyperium.config;

public enum Category {

    // Settings Tab
    GENERAL("General"),
    IMPROVEMENTS("Improvements"),
    INTEGRATIONS("Integrations"),
    COSMETICS("Cosmetics"),
    SPOTIFY("Spotify"),
    ANIMATIONS("Animations"),
    MISC("Misc"),
    MODS("Mods"),
    HYPIXEL("Hypixel"),
    BUTTONS("Buttons"),
    MENUS("Menus"),

    // Mods
    AUTOTIP("Autotip"),
    AUTO_GG("Auto GG"),
    LEVEL_HEAD("Levelhead"),
    REACH("Reach Display"),
    VANILLA_ENHANCEMENTS("Vanilla Enhancements"),
    CHROMAHUD("ChromaHUD"),
    KEYSTROKES("Keystrokes"),
    MOTION_BLUR("Motion Blur"),
    AUTOFRIEND("Auto Friend"),
    GLINTCOLORIZER("Glint Colorizer"),
    FNCOMPASS("Fortnite Compass"),
    TAB_TOGGLE("Tab Toggle"),
    ITEM_PHYSIC("Item Physics"),
    VICTORYROYALE("Victory Royale"),
    TOGGLESPRINT("Toggle Sprint"),
    CHUNK_ANIMATOR("Chunk Animator");

    private String display;

    Category(String display) {
        this.display = display;
    }

    public String getDisplay() {
        return display;
    }
}
