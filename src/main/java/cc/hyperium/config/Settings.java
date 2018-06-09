package cc.hyperium.config;

import cc.hyperium.GuiStyle;
import cc.hyperium.Hyperium;

import static cc.hyperium.config.Category.*;

/*
 * Created by Cubxity on 03/06/2018
 */
public class Settings {
    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;discordRPEnabled")
    @ToggleSetting(name = "Discord RP", category = INTEGRATIONS)
    public static boolean DISCORD_RP = true;
    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;fullbrightEnabled")
    @ToggleSetting(name = "Fullbright")
    public static boolean FULLBRIGHT = true;
    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;romanNumeralsEnabled")
    @ToggleSetting(name = "Roman numerals")
    public static boolean ROMAN_NUMERALS = true;
    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;discordServerDisplayEnabled")
    @ToggleSetting(name = "RP Show server", category = INTEGRATIONS)
    public static boolean DISCORD_RP_SERVER = true;
    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;compactChatEnabled")
    @ToggleSetting(name = "Compact chat")
    public static boolean COMPACT_CHAT = false;
    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;voidflickerfixEnabled")
    @ToggleSetting(name = "Void flicker fix", category = IMPROVEMENTS)
    public static boolean VOID_FLICKER_FIX = true;
    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;framerateLimiterEnabled")
    @ToggleSetting(name = "FPS limiter", category = IMPROVEMENTS)
    public static boolean FPS_LIMITER = true;
    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;fastchatEnabled")
    @ToggleSetting(name = "Fast chat")
    public static boolean FASTCHAT = false;
    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;shinyPotsEnabled")
    @ToggleSetting(name = "Shiny pots")
    public static boolean SHINY_POTS = false;
    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;smartSoundsEnabled")
    @ToggleSetting(name = "Smart sounds", category = IMPROVEMENTS)
    public static boolean SMART_SOUNDS = false;
    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;numberPingEnabled")
    @ToggleSetting(name = "Number ping")
    public static boolean NUMBER_PING = true;
    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;combatParticleFixEnabled")
    @ToggleSetting(name = "Crit particle fix", category = IMPROVEMENTS)
    public static boolean CRIT_FIX = true;
    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;perspectiveHoldDownEnabled")
    @ToggleSetting(name = "Perspective key hold")
    public static boolean PERSPECTIVE_HOLD = false;
    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;menuStyle")
    public static String MENU_STYLE = GuiStyle.DEFAULT.toString();
    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;windowedFullScreen")
    @ToggleSetting(name = "Windowed fullscreen", category = IMPROVEMENTS)
    public static boolean WINDOWED_FULLSCREEN = false;
    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;bossBarTextOnlyEnabled")
    @ToggleSetting(name = "Bossbar text only")
    public static boolean BOSSBAR_TEXT_ONLY = false;
    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;staticFovEnabled")
    @ToggleSetting(name = "Static FOV")
    public static boolean STATIC_FOV = false;
    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;uploadScreenshotsByDefault")
    @ToggleSetting(name = "Upload screenshots")
    public static boolean DEFAULT_UPLOAD_SS = false;
    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;hideScoreboardNumbers")
    @ToggleSetting(name = "Hide scoreboard numbers")
    public static boolean HIDE_SCOREBOARD_NUMBERS = true;
    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;blurGuiBackgroundsEnabled")
    @ToggleSetting(name = "Blurred GUI background")
    public static boolean BLUR_GUI = false;
    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;chromaHudNonHypixelEnabled")
    @ToggleSetting(name = "Enable chromahud on all servers")
    public static boolean CHROMAHUD_ALL = true; // enables in non-hypixel
    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;screenshotOnKillEnabled")
    @ToggleSetting(name = "Screenshot on kill")
    public static boolean SCREENSHOT_KILL = false;
    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;spotifyControlsEnabled")
    @ToggleSetting(name = "Show Spotify controls",  category = SPOTIFY)
    public static boolean SPOTIFY_CONTROLS = false;
    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;hypixelZooEnabled")
    @ToggleSetting(name = "Welcome to the hypixel zoo")
    public static boolean HYPIXEL_ZOO = false;
    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;hypixelZooEnabled;oldResourcePackGui")
    @ToggleSetting(name = "Legacy resource pack GUI")
    public static boolean LEGACY_RP = false;
    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.CosmeticSettings;dabSpeed")
    public static int DAB_SPEED = 7;
    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.CosmeticSettings;dabToggle")
    public static boolean DAB_TOGGLE = false;
    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.CosmeticSettings;flossDanceSpeed")
    public static int FLOSS_SPEED = 4;
    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.CosmeticSettings;flossDanceToggle")
    public static boolean FLOSS_TOGGLE = false;
    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.CosmeticSettings;flip_type")
    public static int FLIP_TYPE = 1;
    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.AnimationSettings;oldBlockhit")
    @ToggleSetting(name = "1.7 blockhitting")
    public static boolean OLD_BLOCKHIT = true;
    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.AnimationSettings;oldBow")
    @ToggleSetting(name = "1.7 bow")
    public static boolean OLD_BOW = true;
    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.AnimationSettings;oldRod")
    @ToggleSetting(name = "1.7 rod")
    public static boolean OLD_ROD = true;
    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.AnimationSettings;redArmour")
    @ToggleSetting(name = "1.7 armour")
    public static boolean OLD_ARMOUR = true;
    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.AnimationSettings;oldEat")
    @ToggleSetting(name = "1.7 eating")
    public static boolean OLD_EATING = true;
    @ConfigOpt
    @ToggleSetting(name = "RGB in name history")
    public static boolean NH_RGB_NAMES = false;
    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.CosmeticSettings;fliptoggle")
    @ToggleSetting(name = "Flip toggle", category = COSMETICS)
    public static boolean isFlipToggle = true;
    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.CosmeticSettings;fliptype")
    public static int flipType = 1;
    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.BackgroundSettings;backgroundSelect")
    @SelectorSetting(name = "Background", items =
            {
                    "1",
                    "2",
                    "3",
                    "4",
                    "5",
                    "6",
                    "CUSTOM"
            }
    )
    public static String BACKGROUND = "4";
    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.BackgroundSettings;fastWorldGuiEnabled")
    @ToggleSetting(name = "Fast container")
    public static boolean FAST_CONTAINER = false;
    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.BackgroundSettings;maxParticles")
    public static int MAX_PARTICLES = 200;
    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.BackgroundSettings;renderOverInventory")
    @ToggleSetting(name = "Particles in inventory", category = COSMETICS)
    public static boolean PARTICLES_INV = true;
    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.BackgroundSettings;particlesModeString")
    @SelectorSetting(name = "Particles mode", items =
            {
                    "OFF",
                    "PLAIN 1",
                    "PLAIN 2",
                    "CHROMA 1",
                    "CHROMA 2"
            }
    )
    public static String PARTICLE_MODE = "OFF";

    public static void register() {
        Hyperium.CONFIG.register(new Settings()); // values r static soo whatever
    }

    public static void save() {
        Hyperium.CONFIG.save();
    }
}
