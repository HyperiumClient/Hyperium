package cc.hyperium.config;

public enum Category {

    //Settings Tab
    GENERAL("General"),
    IMPROVEMENTS("Improvements"),
    INTEGRATIONS("Integrations"),
    COSMETICS("Cosmetics"),
    SPOTIFY("Spotify"),
    ANIMATIONS("Animations"),
    MISC("Misc"),
    MODS("Mods"),
    HYPIXEL("Hypixel"),

    //Mods
    AUTOTIP("Autotip"),
    AUTO_GG("Auto GG"),
    AUTO_TPA("Auto TPA"),
    UTILITIES("Utilities"),
    LEVEL_HEAD("Levelhead"),
    REACH("Reach Display"),
    VANILLA_ENHANCEMENTS("Vanilla Enhancements"),
    CHROMAHUD("ChromaHUD"),
    KEYSTROKES("Keystrokes"),
    MOTION_BLUR("Motion Blur"),
    AUTOFRIEND("Auto Friend"),
    FNCOMPASS("Fornite Compass");
    private String display;

    Category(String display) {
        this.display = display;
    }

    public String getDisplay() {
        return display;
    }
}
