/*
 *     Copyright (C) 2018  Hyperium <https://hyperium.cc/>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.config;

import cc.hyperium.GuiStyle;
import cc.hyperium.Hyperium;

import static cc.hyperium.config.Category.*;

/*
 * Created by Cubxity on 03/06/2018
 */
public class Settings {

    public static final Settings INSTANCE = new Settings();

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
    @ToggleSetting(name = "Shiny Potions", category = ANIMATIONS, mods = true)
    public static boolean SHINY_POTS = false;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;smartSoundsEnabled")
    @ToggleSetting(name = "Smart Sounds", category = IMPROVEMENTS)
    public static boolean SMART_SOUNDS = false;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;numberPingEnabled")
    @ToggleSetting(name = "Numeric Ping", category = VANILLA_ENCHANTMENTS, mods = true)
    public static boolean NUMBER_PING = true;

    @ConfigOpt
    @ToggleSetting(name = "Arrow Count When Holding Bow", category = VANILLA_ENCHANTMENTS, mods = true)
    public static boolean ARROW_COUNT = true;

    @ConfigOpt
    @ToggleSetting(name = "Show Enchantments above hotbar", category = VANILLA_ENCHANTMENTS, mods = true)
    public static boolean ENCHANTMENTS_ABOVE_HOTBAR = true;

    @ConfigOpt
    @ToggleSetting(name = "Show Attack Damage above hotbar", category = VANILLA_ENCHANTMENTS, mods = true)
    public static boolean DAMAGE_ABOVE_HOTBAR = true;

    @ConfigOpt
    @ToggleSetting(name = "Multi-byte input (Input fix)", category = VANILLA_ENCHANTMENTS, mods = true)
    public static boolean INPUT_FIX = false;


    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;combatParticleFixEnabled")
    @ToggleSetting(name = "Crit Particle Fix", category = IMPROVEMENTS)
    public static boolean CRIT_FIX = true;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;perspectiveHoldDownEnabled")
    @ToggleSetting(name = "Hold Perspective Key")
    public static boolean PERSPECTIVE_HOLD = false;

    @ConfigOpt
    @ToggleSetting(category = IMPROVEMENTS, name = "Optimized Item Renderer (BETA)")
    public static boolean OPTIMIZED_ITEM_RENDERER = false;

    @ConfigOpt
    @ToggleSetting(name = "Optimized Texture Loading (BETA)", category = IMPROVEMENTS)
    public static boolean OPTIMIZED_TEXTURE_LOADING = false;

    @ConfigOpt
    @ToggleSetting(name = "Optimized Font Renderer (BETA)", category = IMPROVEMENTS)
    public static boolean OPTIMIZED_FONT_RENDERER = false;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;windowedFullScreen")
    @ToggleSetting(name = "Windowed Fullscreen", category = IMPROVEMENTS)
    public static boolean WINDOWED_FULLSCREEN = false;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;bossBarTextOnlyEnabled")
    @ToggleSetting(name = "Bossbar - Text Only")
    public static boolean BOSSBAR_TEXT_ONLY = false;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;staticFovEnabled")
    @ToggleSetting(name = "Static FOV", category = IMPROVEMENTS)
    public static boolean STATIC_FOV = false;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;uploadScreenshotsByDefault")
    @ToggleSetting(name = "Upload Screenshots (by default)")
    public static boolean DEFAULT_UPLOAD_SS = false;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;hideScoreboardNumbers")
    @ToggleSetting(name = "Hide Scoreboard Numbers", category = IMPROVEMENTS)
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
    @ToggleSetting(name = "Show Spotify Controls", category = SPOTIFY, mods = true)
    public static boolean SPOTIFY_CONTROLS = false;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;hypixelZooEnabled")
    @ToggleSetting(name = "\"Welcome to the Hypixel Zoo!\"")
    public static boolean HYPIXEL_ZOO = true;

    @ConfigOpt
    @ToggleSetting(category = COSMETICS, name = "Show Particle Auras")
    public static boolean SHOW_PARTICLES = true;

    @ConfigOpt(alt = "cc.hyperium.handlers.handlers.OtherConfigOptions;showCosmeticsEveryWhere")
    @ToggleSetting(category = COSMETICS, name = "Show Cosmetics Everywhere")
    public static boolean SHOW_COSMETICS_EVERYWHERE = true;

    @ConfigOpt
    @ToggleSetting(category = COSMETICS, name = "Load optifine capes")
    public static boolean LOAD_OPTIFINE_CAPES = true;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.CosmeticSettings;dabToggle")
    public static boolean DAB_TOGGLE = false;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.CosmeticSettings;tPoseToggle")
    public static boolean TPOSE_TOGGLE = false;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.CosmeticSettings;flossDanceToggle")
    public static boolean FLOSS_TOGGLE = false;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.CosmeticSettings;flip_type")
    public static int FLIP_TYPE = 1;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.AnimationSettings;oldBlockhit")
    @ToggleSetting(name = "1.7 Blockhitting", category = ANIMATIONS, mods = true)
    public static boolean OLD_BLOCKHIT = true;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.AnimationSettings;oldBow")
    @ToggleSetting(name = "1.7 Bow Position", category = ANIMATIONS, mods = true)
    public static boolean OLD_BOW = true;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.AnimationSettings;oldRod")
    @ToggleSetting(name = "1.7 Rod Position", category = ANIMATIONS, mods = true)
    public static boolean OLD_ROD = true;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.AnimationSettings;redArmour")
    @ToggleSetting(name = "1.7 Red Armour (on hit)", category = ANIMATIONS, mods = true)
    public static boolean OLD_ARMOUR = true;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.AnimationSettings;oldEat")
    @ToggleSetting(name = "1.7 Eating", category = ANIMATIONS, mods = true)
    public static boolean OLD_EATING = true;

    @ConfigOpt
    @ToggleSetting(name = "1.7 Sneaking Animation", category = ANIMATIONS, mods = true)
    public static boolean OLD_SNEAKING = false;

    @ConfigOpt
    @ToggleSetting(name = "1.7 Blocking", category = ANIMATIONS, mods = true)
    public static boolean OLD_BLOCKING = false;

    @ConfigOpt
    @ToggleSetting(name = "1.7 Item Held", category = ANIMATIONS, mods = true)
    public static boolean OLD_ITEM_HELD = false;

    @ConfigOpt
    @ToggleSetting(name = "1.7 Debug", category = ANIMATIONS, mods = true)
    public static boolean OLD_DEBUG = false;

    @ConfigOpt
    @ToggleSetting(name = "1.7 Health", category = ANIMATIONS, mods = true)
    public static boolean OLD_HEALTH = false;

    @ConfigOpt
    @ToggleSetting(name = "Custom Sword Animation", category = ANIMATIONS, mods = true)
    public static boolean CUSTOM_SWORD_ANIMATION = false;

    @ConfigOpt
    @ToggleSetting(name = "Name History RGB", category = MISC)
    public static boolean NH_RGB_NAMES = false;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.CosmeticSettings;fliptoggle")
    @ToggleSetting(name = "Flip - Toggle Mode", category = COSMETICS)
    public static boolean isFlipToggle = true;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.CosmeticSettings;fliptype")
    public static int flipType = 1;

    @ConfigOpt
    @SelectorSetting(name = "Flip Type", category = COSMETICS, items = {})//OVERRIDEN
    public static String FLIP_TYPE_STRING = "FLIP";

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.CosmeticSettings;tposetoggle")
    @ToggleSetting(name = "TPose - Toggle Mode", category = COSMETICS)
    public static boolean TPOSE_TOGGLE_MODE = true;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.BackgroundSettings;backgroundSelect")
    @SelectorSetting(name = "Background", category = MISC, items =
            {
                    "1",
                    "2",
                    "3",
                    "4",
                    "5",
                    "6",
                    "CUSTOM",
                    "DEFAULT"
            }
    )
    public static String BACKGROUND = "4";

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.BackgroundSettings;fastWorldGuiEnabled")
    @ToggleSetting(name = "Transparent Container Backgrounds")
    public static boolean FAST_CONTAINER = false;

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

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.BackgroundSettings;maxParticles")
    public static int MAX_PARTICLES = 200;

    @ConfigOpt
    @SelectorSetting(name = "Max Particles", category = COSMETICS, items = {"200"}) // Items configured in override
    public static String MAX_PARTICLE_STRING = "200";

    @ConfigOpt
    public static double HEAD_SCALE_FACTOR = 1.0;

    @ConfigOpt
    @SelectorSetting(name = "Head Item Scale", category = ANIMATIONS, items = {"1.0", "1.25", "1.5", "1.75", "2.0", "2.5"})
    // Items configured in override
    public static String HEAD_SCALE_FACTOR_STRING = "1.0";

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.BackgroundSettings;renderOverInventory")
    @ToggleSetting(name = "Particles In Inventory", category = COSMETICS)
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

    @ConfigOpt(alt = "cc.hyperium.handlers.handlers.OtherConfigOptions;friendsFirstIntag")
    @ToggleSetting(category = GENERAL, name = "Friends First In Tab")
    public static boolean FRIENDS_FIRST_IN_TAB = true;

    @ConfigOpt(alt = "cc.hyperium.handlers.handlers.OtherConfigOptions;showOnlinePlayers")
    @ToggleSetting(category = GENERAL, name = "Online Indicator")
    public static boolean SHOW_ONLINE_PLAYERS = true;

    @ConfigOpt(alt = "cc.hyperium.handlers.handlers.OtherConfigOptions;turnPeopleIntoBlock")
    @ToggleSetting(category = COSMETICS, name = "Show Players As Blocks")
    public static boolean TURN_PEOPLE_INTO_BLOCKS = false;

    @ConfigOpt(alt = "cc.hyperium.handlers.handlers.OtherConfigOptions;pingOnDm")
    @ToggleSetting(category = GENERAL, name = "Ping On DM")
    public static boolean PING_ON_DM = true;

    @ConfigOpt()
    @ToggleSetting(category = GENERAL, name = "Sprint Bypass Static FOV")
    public static boolean staticFovSprintModifier;

    @ConfigOpt()
    @ToggleSetting(category = GENERAL, name = "Sprint And Perspective Messages")
    public static boolean SPRINT_PERSPECTIVE_MESSAGES = true;

    @ConfigOpt(alt = "cc.hyperium.handlers.handlers.chat.AutoWhoChatHandler;enabled")
    @ToggleSetting(name = "Auto Who", category = INTEGRATIONS)
    public static boolean AUTO_WHO = true;

    @ConfigOpt
    @SelectorSetting(name = "Show Dragon Head", items = {}, category = COSMETICS)
    public static String SHOW_DRAGON_HEAD = "OFF";

    @ConfigOpt
    @SelectorSetting(name = "Show Wings", items = {}, category = COSMETICS)
    public static String SHOW_WINGS = "ON";

    @ConfigOpt
    @SliderSetting(name = "Wings Scale", min = 50, max = 200, category = COSMETICS)
    public static double WINGS_SCALE = 100D;

    @ConfigOpt
    @ToggleSetting(name = "Show ChromaHUD", category = CHROMAHUD, mods = true)
    public static boolean SHOW_CHROMAHUD = true;

    @ConfigOpt
    @SelectorSetting(name = "Main Menu Style", items =
            {
                    "HYPERIUM",
                    "DEFAULT"
            }, category = MISC
    )
    public static String MENU_STYLE = GuiStyle.DEFAULT.name();

    @ConfigOpt
    @ToggleSetting(name = "Spotify Notifications", category = SPOTIFY, mods = true)
    public static boolean SPOTIFY_NOTIFICATIONS = true;

    @ConfigOpt
    @ToggleSetting(name = "Force Spotify to Never Load", category = SPOTIFY, mods = true)
    public static boolean SPOTIFY_FORCE_DISABLE = false;

    @ConfigOpt
    @ToggleSetting(name = "Update Notifications in Hyperium Settings", category = MISC)
    public static boolean UPDATE_NOTIFICATIONS = true;

    @ConfigOpt
    @ToggleSetting(name = "Fast World Switching (May increase memory usage)", category = IMPROVEMENTS)
    public static boolean FAST_WORLD_LOADING = false;
    @ConfigOpt
    public static int MAX_WORLD_PARTICLES_INT = 10000;

    @ConfigOpt
    @SelectorSetting(category = IMPROVEMENTS, name = "Max World Particles", items = {
            "1000",
            "2000",
            "4000",
            "6000",
            "8000",
            "10000",
            "20000",
            "50000",

    })
    public static String MAX_WORLD_PARTICLES_STRING = "10000";

    @ConfigOpt
    @ToggleSetting(name = "Show particle in 1st person", category = COSMETICS)
    public static boolean SHOW_PART_1ST_PERSON = false;

    @ConfigOpt
    @ToggleSetting(name = "Always show super secret settings", category = MISC)
    public static boolean ALWAYS_SHOW_SUPER_SECRET_SETTINGS = false;

    @ConfigOpt
    @ToggleSetting(name = "Show Hit Distances", category = REACH, mods = true)
    public static boolean SHOW_HIT_DISTANCES = false;

    @ConfigOpt
    @ToggleSetting(name = "Enabled", category = Category.MOTION_BLUR, mods = true)
    public static boolean MOTION_BLUR_ENABLED = false;

    @ConfigOpt
    @SliderSetting(name = "Motion Blur Intensity", min = 0F, max = 7F, category = Category.MOTION_BLUR, mods = true)
    public static double MOTION_BLUR_AMOUNT = 4.0F;

    @ConfigOpt
    @ToggleSetting(name = "Enabled", category = Category.AUTOFRIEND, mods = true)
    public static boolean AUTOFRIEND_TOGGLE = false;

    @ConfigOpt
    @ToggleSetting(name = "Show Friend Messages", category = Category.AUTOFRIEND, mods = true)
    public static boolean AUTOFRIEND_MESSAGES = true;

    @ConfigOpt
    @ToggleSetting(name = "Enabled", category = Category.FNCOMPASS, mods = true)
    public static boolean FNCOMPASS_ENABLED = false;

    @ConfigOpt
    @ToggleSetting(name = "Show Background", category = Category.FNCOMPASS, mods = true)
    public static boolean FNCOMPASS_BACKGROUND = true;

    @ConfigOpt
    @ToggleSetting(name = "Chroma", category = Category.FNCOMPASS, mods = true)
    public static boolean FNCOMPASS_CHROMA = false;

    @ConfigOpt
    @ToggleSetting(name = "Shadow", category = Category.FNCOMPASS, mods = true)
    public static boolean FNCOMPASS_SHADOW = true;

    @ConfigOpt
    @SelectorSetting(category = Category.FNCOMPASS, name = "Details", items = {
            "0",
            "1",
            "2"
    })
    public static String FNCOMPASS_DETAILS = "2";

    @ToggleSetting(name = "Show user dots on nametags", category = INTEGRATIONS)
    public static boolean SHOW_DOTS_ON_NAME_TAGS = false;

    @ConfigOpt
    @SelectorSetting(name = "Color Type", mods = true, category = REACH, items = {"RGB", "CHROMA"})
    public static String REACH_COLOR_TYPE = "RGB";

    @ConfigOpt
    @SliderSetting(name = "Red", mods = true, category = REACH, isInt = true, min = 0, max = 255)
    public static int REACH_RED = 255;

    @ConfigOpt
    @SliderSetting(name = "Blue", mods = true, category = REACH, isInt = true, min = 0, max = 255)
    public static int REACH_BLUE = 0;

    @ConfigOpt
    @SliderSetting(name = "Green", mods = true, category = REACH, isInt = true, min = 0, max = 255)
    public static int REACH_GREEN = 255;

    @ConfigOpt
    @ToggleSetting(name = "Togglesprint", category = UTILITIES, mods = true)
    public static boolean ENABLE_TOGGLE_SPRINT = true;


    @ConfigOpt
    @ToggleSetting(name = "Broadcast Levelups")
    public static boolean BROADCAST_LEVELUPS = true;

    @ConfigOpt
    @ToggleSetting(name = "Broadcast Achievements")
    public static boolean BROADCAST_ACHIEVEMENTS = true;


    @ConfigOpt
    @SelectorSetting(name = "Show Butt", category = COSMETICS, items = {"YES", "NO"})
    public static String SHOW_BUTT = "YES";

    @ConfigOpt
    @ToggleSetting(name = "Send Current Server", category = GENERAL)
    public static boolean SEND_SERVER = true;

    @ConfigOpt
    @ToggleSetting(name = "Send guild welcome message", category = INTEGRATIONS)
    public static boolean SEND_GUILD_WELCOME_MESSAGE = true;


    @ConfigOpt
    public static boolean AUTO_DAB_ENABLED = false;

    @ConfigOpt
    public static int AUTO_DAB_LENGTH = 5;

    @ConfigOpt
    public static boolean AUTO_DAB_THIRD_PERSON = true;

    @ConfigOpt
    public static boolean AUTO_NICO = false;


    @ConfigOpt
    @ToggleSetting(name = "Show Browser", category = IMPROVEMENTS)
    public static boolean SHOW_BROWSER = false;


    @ConfigOpt
    @ToggleSetting(name = "Item Physics", category = GENERAL)
    public static boolean ITEM_PHYSIC_ENABLED = false;

    @ConfigOpt
    public static long TOTAL_PLAYTIME = 0;

    private Settings() {
    }

    public static void register() {
        Hyperium.CONFIG.register(INSTANCE);
    }

    public static void save() {
        Hyperium.CONFIG.save();
    }
}
