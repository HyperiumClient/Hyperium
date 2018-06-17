package cc.hyperium.config;

import static cc.hyperium.config.Category.ANIMATIONS;
import static cc.hyperium.config.Category.COSMETICS;
import static cc.hyperium.config.Category.GENERAL;
import static cc.hyperium.config.Category.IMPROVEMENTS;
import static cc.hyperium.config.Category.INTEGRATIONS;
import static cc.hyperium.config.Category.MISC;
import static cc.hyperium.config.Category.SPOTIFY;
import static cc.hyperium.config.Category.WINGS;

import cc.hyperium.GuiStyle;
import cc.hyperium.Hyperium;

/*
 * Created by Cubxity on 03/06/2018
 */
public class Settings {
    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;discordRPEnabled")
    @ToggleSetting(name = "Discord RP", category = INTEGRATIONS)
    public static boolean DISCORD_RP = true;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;fullbrightEnabled")
    @ToggleSetting(name = "Fullbright", category = INTEGRATIONS)
    public static boolean FULLBRIGHT = true;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;romanNumeralsEnabled")
    @ToggleSetting(name = "Roman Numerals")
    public static boolean ROMAN_NUMERALS = true;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;discordServerDisplayEnabled")
    @ToggleSetting(name = "RP Show Server", category = INTEGRATIONS)
    public static boolean DISCORD_RP_SERVER = true;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;compactChatEnabled")
    @ToggleSetting(name = "Compact Chat", category = INTEGRATIONS)
    public static boolean COMPACT_CHAT = false;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;voidflickerfixEnabled")
    @ToggleSetting(name = "Void Flicker Fix", category = IMPROVEMENTS)
    public static boolean VOID_FLICKER_FIX = true;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;framerateLimiterEnabled")
    @ToggleSetting(name = "FPS Limiter (in Limbo)", category = IMPROVEMENTS)
    public static boolean FPS_LIMITER = true;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;fastchatEnabled")
    @ToggleSetting(name = "Fast Chat", category = INTEGRATIONS)
    public static boolean FASTCHAT = false;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;shinyPotsEnabled")
    @ToggleSetting(name = "Shiny Potions", category = ANIMATIONS)
    public static boolean SHINY_POTS = false;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;smartSoundsEnabled")
    @ToggleSetting(name = "Smart Sounds", category = IMPROVEMENTS)
    public static boolean SMART_SOUNDS = false;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;numberPingEnabled")
    @ToggleSetting(name = "Numeric Ping", category = INTEGRATIONS)
    public static boolean NUMBER_PING = true;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;combatParticleFixEnabled")
    @ToggleSetting(name = "Crit Particle Fix", category = IMPROVEMENTS)
    public static boolean CRIT_FIX = true;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;perspectiveHoldDownEnabled")
    @ToggleSetting(name = "Hold Perspective Key")
    public static boolean PERSPECTIVE_HOLD = false;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;menuStyle")
    public static String MENU_STYLE = GuiStyle.DEFAULT.toString();
    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;windowedFullScreen")
    @ToggleSetting(name = "Windowed Fullscreen", category = IMPROVEMENTS)
    public static boolean WINDOWED_FULLSCREEN = false;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;bossBarTextOnlyEnabled")
    @ToggleSetting(name = "Bossbar - Text Only")
    public static boolean BOSSBAR_TEXT_ONLY = false;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;staticFovEnabled")
    @ToggleSetting(name = "Static FOV")
    public static boolean STATIC_FOV = false;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;uploadScreenshotsByDefault")
    @ToggleSetting(name = "Upload Screenshots (by default)")
    public static boolean DEFAULT_UPLOAD_SS = false;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;hideScoreboardNumbers")
    @ToggleSetting(name = "Hide Scoreboard Numbers")
    public static boolean HIDE_SCOREBOARD_NUMBERS = true;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;blurGuiBackgroundsEnabled")
    @ToggleSetting(name = "Blurred GUI Background")
    public static boolean BLUR_GUI = false;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;chromaHudNonHypixelEnabled")
    @ToggleSetting(name = "Chromahud - On All Servers")
    public static boolean CHROMAHUD_ALL = true;
    // enables in non-hypixel
    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;screenshotOnKillEnabled")
    @ToggleSetting(name = "Screenshot On Kill")
    public static boolean SCREENSHOT_KILL = false;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;spotifyControlsEnabled")
    @ToggleSetting(name = "Show Spotify Controls", category = SPOTIFY)
    public static boolean SPOTIFY_CONTROLS = false;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;hypixelZooEnabled")
    @ToggleSetting(name = "\"Welcome to the Hypixel Zoo!\"")
    public static boolean HYPIXEL_ZOO = true;


    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;hypixelZooEnabled;oldResourcePackGui")
    @ToggleSetting(name = "Legacy Resource Pack GUI")
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
    @ToggleSetting(name = "1.7 Blockhitting", category = ANIMATIONS)
    public static boolean OLD_BLOCKHIT = true;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.AnimationSettings;oldBow")
    @ToggleSetting(name = "1.7 Bow Position", category = ANIMATIONS)
    public static boolean OLD_BOW = true;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.AnimationSettings;oldRod")
    @ToggleSetting(name = "1.7 Rod Position", category = ANIMATIONS)
    public static boolean OLD_ROD = true;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.AnimationSettings;redArmour")
    @ToggleSetting(name = "1.7 Red Armour (on hit)", category = ANIMATIONS)
    public static boolean OLD_ARMOUR = true;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.AnimationSettings;oldEat")
    @ToggleSetting(name = "1.7 Eating", category = ANIMATIONS)
    public static boolean OLD_EATING = true;

    @ConfigOpt
    @ToggleSetting(name = "1.7 Sneaking Animation", category = ANIMATIONS)
    public static boolean OLD_SNEAKING = false;

    @ConfigOpt
    @ToggleSetting(name = "1.7 Blocking", category = ANIMATIONS)
    public static boolean OLD_BLOCKING = false;

    @ConfigOpt
    @ToggleSetting(name = "1.7 Item Held", category = ANIMATIONS)
    public static boolean OLD_ITEM_HELD = false;

    @ConfigOpt
    @ToggleSetting(name = "Name History RGB")
    public static boolean NH_RGB_NAMES = false;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.CosmeticSettings;fliptoggle")
    @ToggleSetting(name = "Flip - Keybind Toggle", category = COSMETICS)
    public static boolean isFlipToggle = true;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.CosmeticSettings;fliptype")
    public static int flipType = 1;

    @ConfigOpt
    @SelectorSetting(name = "Flip Type", category = COSMETICS, items = {})//OVERRIDEN
    public static String FLIP_TYPE_STRING = "FLIP";
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
    @ToggleSetting(name = "Transparent Container Backgrounds")
    public static boolean FAST_CONTAINER = false;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.BackgroundSettings;maxParticles")
    public static int MAX_PARTICLES = 200;

    @ConfigOpt
    @SelectorSetting(name = "Max Particles", category = COSMETICS, items = {"200"}) // Items configured in override
    public static String MAX_PARTICLE_STRING = "200";


    @ConfigOpt()
    public static double HEAD_SCALE_FACTOR = 1.0;

    @ConfigOpt
    @SelectorSetting(name = "Head Item Scale", category = ANIMATIONS, items = {"1.0", "1.25", "1.5", "1.75", "2.0", "2.5"})
    // Items configured in override
    public static String HEAD_SCALE_FACTOR_STRING = "1.0";


    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.BackgroundSettings;renderOverInventory")
    @ToggleSetting(name = "Particles in Inventory", category = COSMETICS)
    public static boolean PARTICLES_INV = true;


    @ConfigOpt
    @SelectorSetting(name = "Deadmau5 Ears", category = COSMETICS, items = {})
    public static String EARS_STATE = "ON";


    @ConfigOpt(alt = "cc.hyperium.handlers.handlers.OtherConfigOptions;SHOW_INGAME_NOTIFICATION_CENTER")
    @ToggleSetting(category = MISC, name = "Show Notification Center")
    public static boolean SHOW_INGAME_NOTIFICATION_CENTER = true;


    @ConfigOpt(alt = "cc.hyperium.handlers.handlers.OtherConfigOptions;SHOW_INGAME_CONFIRMATION_POPUP")
    @ToggleSetting(category = MISC, name = "Show Confirmation Popup")
    public static boolean SHOW_INGAME_CONFIRMATION_POPUP = true;

    @ConfigOpt(alt = "cc.hyperium.handlers.handlers.OtherConfigOptions;savePreviusChatMessages")
    @ToggleSetting(category = GENERAL, name = "Persistent Chat Messages")
    public static boolean PERSISTENT_CHAT = false;

    @ConfigOpt(alt = "cc.hyperium.handlers.handlers.OtherConfigOptions;showCosmeticsEveryWhere")
    @ToggleSetting(category = COSMETICS, name = "Show Cosmetics Everywhere")
    public static boolean SHOW_COSMETICS_EVERYWHERE = true;


    @ConfigOpt(alt = "cc.hyperium.handlers.handlers.OtherConfigOptions;friendsFirstIntag")
    @ToggleSetting(category = GENERAL, name = "Friends First In Tab")
    public static boolean FRIENDS_FIRST_IN_TAB = true;


    @ConfigOpt(alt = "cc.hyperium.handlers.handlers.OtherConfigOptions;showOnlinePlayers")
    @ToggleSetting(category = GENERAL, name = "Online Indicator")
    public static boolean SHOW_ONLINE_PLAYERS = true;

    @ConfigOpt(alt = "cc.hyperium.handlers.handlers.OtherConfigOptions;turnPeopleIntoBlock")
    @ToggleSetting(category = COSMETICS, name = "Show Players as Blocks")
    public static boolean TURN_PEOPLE_INTO_BLOCKS = false;


    @ConfigOpt(alt = "cc.hyperium.handlers.handlers.OtherConfigOptions;pingOnDm")
    @ToggleSetting(category = GENERAL, name = "Ping on DM")
    public static boolean PING_ON_DM = true;


    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.BackgroundSettings;particlesModeString")
    @SelectorSetting(name = "Particles Mode", items =
            {
                    "OFF",
                    "PLAIN 1",
                    "PLAIN 2",
                    "CHROMA 1",
                    "CHROMA 2"
            }, category = COSMETICS
    )
    public static String PARTICLE_MODE = "OFF";

    @ConfigOpt(alt = "cc.hyperium.cosmetics.wings.WingsCosmetic;location")
    @SelectorSetting(name = "Wing Type", items =
            {
                    "Dragon wings",
                    "Angel wings"
            }, category = WINGS
    )
    public static String wingsSELECTED = "Dragon wings";

    @ConfigOpt()
    @ToggleSetting(category = GENERAL, name = "Sprint Bypass Static FOV")
    public static boolean staticFovSprintModifier;


    @ConfigOpt(alt = "cc.hyperium.handlers.handlers.chat.AutoWhoChatHandler;enabled")
    @ToggleSetting(name = "Auto Who", category = INTEGRATIONS)
    public static boolean AUTO_WHO = true;


    @ConfigOpt
    @ToggleSetting(name = "Show Dragon Head", category = COSMETICS)
    public static boolean SHOW_DRAGON_HEAD = true;

    @ConfigOpt
    @ToggleSetting(name = "Show Wings", category = WINGS)
    public static boolean SHOW_WINGS = true;

    @ConfigOpt
    @SliderSetting(name = "Wings Scale", min = 50, max = 200, category = WINGS)
    public static double WINGS_SCALE = 100D;

    public static void register() {
        Hyperium.CONFIG.register(new Settings()); // values r static soo whatever
    }

    public static void save() {
        Hyperium.CONFIG.save();
    }
}
