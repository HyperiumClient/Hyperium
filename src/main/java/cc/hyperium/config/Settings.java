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

import cc.hyperium.Hyperium;

import cc.hyperium.styles.ButtonStyle;
import cc.hyperium.styles.ButtonType;
import cc.hyperium.styles.GuiStyle;

import static cc.hyperium.config.Category.ANIMATIONS;
import static cc.hyperium.config.Category.BUTTONS;
import static cc.hyperium.config.Category.CHROMAHUD;
import static cc.hyperium.config.Category.COSMETICS;
import static cc.hyperium.config.Category.GENERAL;
import static cc.hyperium.config.Category.HYPIXEL;
import static cc.hyperium.config.Category.IMPROVEMENTS;
import static cc.hyperium.config.Category.INTEGRATIONS;
import static cc.hyperium.config.Category.ITEM_PHYSIC;
import static cc.hyperium.config.Category.MENUS;
import static cc.hyperium.config.Category.MISC;
import static cc.hyperium.config.Category.REACH;
import static cc.hyperium.config.Category.TOGGLESPRINT;
import static cc.hyperium.config.Category.VANILLA_ENHANCEMENTS;
import static cc.hyperium.config.Category.VICTORYROYALE;

/*
 * Created by Cubxity on 03/06/2018
 */
public class Settings {

    public static final Settings INSTANCE = new Settings();

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;discordRPEnabled")
    @ToggleSetting(name = "gui.settings.discordrp", category = INTEGRATIONS)
    public static boolean DISCORD_RP = true;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;fullbrightEnabled")
    @ToggleSetting(name = "gui.settings.fullbright", category = INTEGRATIONS)
    public static boolean FULLBRIGHT = true;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;romanNumeralsEnabled")
    @ToggleSetting(name = "gui.settings.romannumerals")
    public static boolean ROMAN_NUMERALS = true;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;discordServerDisplayEnabled")
    @ToggleSetting(name = "gui.settings.rpshowserver", category = INTEGRATIONS)
    public static boolean DISCORD_RP_SERVER = true;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;compactChatEnabled")
    @ToggleSetting(name = "gui.settings.compactchat", category = INTEGRATIONS)
    public static boolean COMPACT_CHAT = false;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;voidflickerfixEnabled")
    @ToggleSetting(name = "gui.settings.voidflickerfix", category = IMPROVEMENTS)
    public static boolean VOID_FLICKER_FIX = true;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;framerateLimiterEnabled")
    @ToggleSetting(name = "gui.settings.fpslimiter", category = IMPROVEMENTS)
    public static boolean FPS_LIMITER = true;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;fastchatEnabled")
    @ToggleSetting(name = "gui.settings.fastchat", category = INTEGRATIONS)
    public static boolean FASTCHAT = false;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;shinyPotsEnabled")
    @ToggleSetting(name = "gui.settings.shinypotions", category = ANIMATIONS)
    public static boolean SHINY_POTS = false;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;smartSoundsEnabled")
    @ToggleSetting(name = "gui.settings.smartsounds", category = IMPROVEMENTS)
    public static boolean SMART_SOUNDS = false;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;numberPingEnabled")
    @ToggleSetting(name = "gui.settings.numericping", category = VANILLA_ENHANCEMENTS, mods = true)
    public static boolean NUMBER_PING = true;

    @ConfigOpt
    @ToggleSetting(name = "gui.settings.arrowcountwhenholdingbow", category = VANILLA_ENHANCEMENTS, mods = true)
    public static boolean ARROW_COUNT = true;

    @ConfigOpt
    @ToggleSetting(name = "gui.settings.showenchantmentsabovehotbar", category = VANILLA_ENHANCEMENTS, mods = true)
    public static boolean ENCHANTMENTS_ABOVE_HOTBAR = true;

    @ConfigOpt
    @ToggleSetting(name = "gui.settings.showattackdamageabovehotbar", category = VANILLA_ENHANCEMENTS, mods = true)
    public static boolean DAMAGE_ABOVE_HOTBAR = true;

    @ConfigOpt
    @ToggleSetting(name = "gui.settings.armorprotectionpotentional", category = VANILLA_ENHANCEMENTS, mods = true)
    public static boolean ARMOR_PROT_POTENTIONAL = true;

    @ConfigOpt
    @ToggleSetting(name = "gui.settings.armorprojectileprotpotentional", category = VANILLA_ENHANCEMENTS, mods = true)
    public static boolean ARMOR_PROJ_POTENTIONAL = true;

    @ConfigOpt
    @ToggleSetting(name = "gui.settings.hotbarkeys", category = VANILLA_ENHANCEMENTS, mods = true)
    public static boolean HOTBAR_KEYS = false;

    @ConfigOpt
    @ToggleSetting(name = "gui.settings.hidecrosshairinf5", category = VANILLA_ENHANCEMENTS, mods = true)
    public static boolean CROSSHAIR_IN_F5 = false;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;combatParticleFixEnabled")
    @ToggleSetting(name = "gui.settings.critparticlefix", category = IMPROVEMENTS)
    public static boolean CRIT_FIX = true;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;perspectiveHoldDownEnabled")
    @ToggleSetting(name = "gui.settings.holdperspectivekey")
    public static boolean PERSPECTIVE_HOLD = false;

    @ConfigOpt
    @ToggleSetting(category = IMPROVEMENTS, name = "gui.settings.optimizeditemrenderer")
    public static boolean OPTIMIZED_ITEM_RENDERER = false;

    @ConfigOpt
    @ToggleSetting(name = "gui.settings.optimizedtextureloading", category = IMPROVEMENTS)
    public static boolean OPTIMIZED_TEXTURE_LOADING = false;

    @ConfigOpt
    @ToggleSetting(name = "gui.settings.optimizedfontrenderer", category = IMPROVEMENTS)
    public static boolean OPTIMIZED_FONT_RENDERER = false;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;windowedFullScreen")
    @ToggleSetting(name = "gui.settings.windowedfullscreen", category = IMPROVEMENTS)
    public static boolean WINDOWED_FULLSCREEN = false;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;bossBarTextOnlyEnabled")
    @ToggleSetting(name = "gui.settings.bossbartextonly")
    public static boolean BOSSBAR_TEXT_ONLY = false;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;staticFovEnabled")
    @ToggleSetting(name = "gui.settings.staticfov", category = IMPROVEMENTS)
    public static boolean STATIC_FOV = false;

    @ConfigOpt
    public static int SETTINGS_ALPHA = 100;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;uploadScreenshotsByDefault")
    @ToggleSetting(name = "gui.settings.uploadscreenshots")
    public static boolean DEFAULT_UPLOAD_SS = false;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;blurGuiBackgroundsEnabled")
    @ToggleSetting(name = "gui.settings.blurredguibackground")
    public static boolean BLUR_GUI = false;

    @ConfigOpt
    @SelectorSetting(name = "Hat Type", category = COSMETICS, items = "NONE")
    public static String HAT_TYPE = "NONE";

    @ConfigOpt
    @SelectorSetting(name = "Companion Type", category = COSMETICS, items = "NONE")
    public static String COMPANION_TYPE = "NONE";

    @ConfigOpt
    public static boolean SPOTIFY_CONTROLS = false;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;hypixelZooEnabled")
    @ToggleSetting(name = "gui.settings.welcometothehypixelzoo", category = HYPIXEL)
    public static boolean HYPIXEL_ZOO = true;

    @ConfigOpt
    @ToggleSetting(category = COSMETICS, name = "gui.settings.showparticleauras")
    public static boolean SHOW_PARTICLES = true;

    @ConfigOpt
    @ToggleSetting(category = GENERAL, name = "Show own name tag")
    public static boolean SHOW_OWN_NAME = false;

    @ConfigOpt(alt = "cc.hyperium.handlers.handlers.OtherConfigOptions;showCosmeticsEveryWhere")
    @ToggleSetting(category = COSMETICS, name = "gui.settings.showcosmeticseverywhere")
    public static boolean SHOW_COSMETICS_EVERYWHERE = true;

    @ConfigOpt
    @ToggleSetting(category = COSMETICS, name = "gui.settings.loadoptifinecapes")
    public static boolean LOAD_OPTIFINE_CAPES = true;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.CosmeticSettings;dabToggle")
    public static boolean DAB_TOGGLE = false;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.CosmeticSettings;tPoseToggle")
    public static boolean TPOSE_TOGGLE = false;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.CosmeticSettings;flossDanceToggle")
    public static boolean FLOSS_TOGGLE = false;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.AnimationSettings;oldBlockhit")
    @ToggleSetting(name = "gui.settings.17blockhitting", category = ANIMATIONS)
    public static boolean OLD_BLOCKHIT = true;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.AnimationSettings;oldBow")
    @ToggleSetting(name = "gui.settings.17bowposition", category = ANIMATIONS)
    public static boolean OLD_BOW = true;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.AnimationSettings;oldRod")
    @ToggleSetting(name = "gui.settings.17rodposition", category = ANIMATIONS)
    public static boolean OLD_ROD = true;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.AnimationSettings;redArmour")
    @ToggleSetting(name = "gui.settings.17redarmour", category = ANIMATIONS)
    public static boolean OLD_ARMOUR = true;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.AnimationSettings;oldEat")
    @ToggleSetting(name = "gui.settings.17eating", category = ANIMATIONS)
    public static boolean OLD_EATING = true;

    @ConfigOpt
    @ToggleSetting(name = "gui.settings.17sneakinganimation", category = ANIMATIONS)
    public static boolean OLD_SNEAKING = false;

    @ConfigOpt
    @ToggleSetting(name = "gui.settings.17blocking", category = ANIMATIONS)
    public static boolean OLD_BLOCKING = false;

    @ConfigOpt
    @ToggleSetting(name = "gui.settings.17itemheld", category = ANIMATIONS)
    public static boolean OLD_ITEM_HELD = false;

    @ConfigOpt
    @ToggleSetting(name = "gui.settings.17debug", category = ANIMATIONS)
    public static boolean OLD_DEBUG = false;

    @ConfigOpt
    @ToggleSetting(name = "gui.settings.17health", category = ANIMATIONS)
    public static boolean OLD_HEALTH = false;

    @ConfigOpt
    @ToggleSetting(name = "gui.settings.customswordanimation", category = ANIMATIONS)
    public static boolean CUSTOM_SWORD_ANIMATION = false;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.CosmeticSettings;fliptoggle")
    @ToggleSetting(name = "gui.settings.fliptogglemode", category = COSMETICS)
    public static boolean isFlipToggle = true;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.CosmeticSettings;fliptype")
    public static int flipType = 1;

    @ConfigOpt
    @SelectorSetting(name = "gui.settings.fliptype", category = COSMETICS, items = {})//OVERRIDEN
    public static String FLIP_TYPE_STRING = "FLIP";

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.CosmeticSettings;tposetoggle")
    @ToggleSetting(name = "gui.settings.tposetogglemode", category = COSMETICS)
    public static boolean TPOSE_TOGGLE_MODE = true;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.BackgroundSettings;backgroundSelect")
    @SelectorSetting(name = "gui.settings.background", category = MENUS, items =
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
    @ToggleSetting(name = "gui.settings.transparentcontainerbackgrounds")
    public static boolean FAST_CONTAINER = false;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.BackgroundSettings;particlesModeString")
    @SelectorSetting(name = "gui.settings.particlesmode", items =
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
    @SelectorSetting(name = "gui.settings.maxparticles", category = COSMETICS, items = {"200"})
    // Items configured in override
    public static String MAX_PARTICLE_STRING = "200";

    @ConfigOpt
    public static double HEAD_SCALE_FACTOR = 1.0;

    @ConfigOpt
    @SelectorSetting(name = "gui.settings.headitemscale", category = ANIMATIONS, items = {"1.0", "1.25", "1.5", "1.75", "2.0", "2.5"})
    // Items configured in override
    public static String HEAD_SCALE_FACTOR_STRING = "1.0";

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.BackgroundSettings;renderOverInventory")
    @ToggleSetting(name = "gui.settings.particlesininventory", category = COSMETICS)
    public static boolean PARTICLES_INV = true;

    @ConfigOpt
    @SelectorSetting(name = "gui.settings.deadmau5ears", category = COSMETICS, items = {})
    public static String EARS_STATE = "ON";

    @ConfigOpt(alt = "cc.hyperium.handlers.handlers.OtherConfigOptions;SHOW_INGAME_NOTIFICATION_CENTER")
    @ToggleSetting(category = MISC, name = "gui.settings.shownotificationcenter")
    public static boolean SHOW_INGAME_NOTIFICATION_CENTER = true;

    @ConfigOpt(alt = "cc.hyperium.handlers.handlers.OtherConfigOptions;SHOW_INGAME_CONFIRMATION_POPUP")
    @ToggleSetting(category = MISC, name = "gui.settings.showconfirmationpopup")
    public static boolean SHOW_INGAME_CONFIRMATION_POPUP = true;

    @ConfigOpt(alt = "cc.hyperium.handlers.handlers.OtherConfigOptions;savePreviusChatMessages")
    @ToggleSetting(category = GENERAL, name = "gui.settings.persistentchatmessages")
    public static boolean PERSISTENT_CHAT = false;

    @ConfigOpt(alt = "cc.hyperium.handlers.handlers.OtherConfigOptions;friendsFirstIntag")
    @ToggleSetting(category = HYPIXEL, name = "gui.settings.friendsfirstintab")
    public static boolean FRIENDS_FIRST_IN_TAB = true;

    @ConfigOpt(alt = "cc.hyperium.handlers.handlers.OtherConfigOptions;showOnlinePlayers")
    @ToggleSetting(category = GENERAL, name = "gui.settings.onlineindicator")
    public static boolean SHOW_ONLINE_PLAYERS = true;

    @ConfigOpt(alt = "cc.hyperium.handlers.handlers.OtherConfigOptions;pingOnDm")
    @ToggleSetting(category = HYPIXEL, name = "gui.settings.pingondm")
    public static boolean PING_ON_DM = true;

    @ConfigOpt
    @ToggleSetting(category = IMPROVEMENTS, name = "Improved Particle Handling (BETA)")
    public static boolean IMPROVE_PARTICLES = true;

    @ConfigOpt
    @ToggleSetting(category = IMPROVEMENTS, name = "Improved Entity Handling (BETA)")
    public static boolean IMPROVE_ENTITY_HANDLING = false;

    public static boolean IMPROVE_PARTICLE_RUN = false; /* any reason why this isnt used at all? */

    @ConfigOpt()
    @ToggleSetting(category = GENERAL, name = "gui.settings.sprintbypassstaticfov")
    public static boolean staticFovSprintModifier;

    @ConfigOpt()
    @ToggleSetting(category = GENERAL, name = "gui.settings.sprintandperspectivemessages")
    public static boolean SPRINT_PERSPECTIVE_MESSAGES = true;

    @ConfigOpt
    @SelectorSetting(name = "gui.settings.showdragonhead", items = {}, category = COSMETICS)
    public static String SHOW_DRAGON_HEAD = "OFF";

    @ConfigOpt
    @SelectorSetting(name = "gui.settings.showwings", items = {}, category = COSMETICS)
    public static String SHOW_WINGS = "ON";

    @ConfigOpt
    @SliderSetting(name = "gui.settings.wingsheight", min = -40, max = 40, category = COSMETICS)
    public static double WINGS_OFFSET = 0D;

    @ConfigOpt
    @SliderSetting(name = "gui.settings.wingsscale", min = 50, max = 200, category = COSMETICS)
    public static double WINGS_SCALE = 100D;

    @ToggleSetting(name = "gui.settings.disable_dances", category = COSMETICS)
    @ConfigOpt
    public static boolean DISABLE_DANCES = false;

    @ConfigOpt
    @ToggleSetting(name = "gui.settings.showchromahud", category = CHROMAHUD, mods = true)
    public static boolean SHOW_CHROMAHUD = true;

    @ConfigOpt
    @ToggleSetting(name = "gui.settings.chromahudprefixsquarebrace", category = CHROMAHUD, mods = true)
    public static boolean CHROMAHUD_SQUAREBRACE_PREFIX_OPTION = false;

    @ConfigOpt
    @ToggleSetting(name = "gui.settings.directionhudshort", category = CHROMAHUD, mods = true)
    public static boolean SHORT_DIRECTION_HUD = false;

    @ConfigOpt
    @SelectorSetting(name = "gui.settings.mainmenustyle", items =
        {
            "HYPERIUM",
            "DEFAULT"
        }, category = MENUS
    )
    public static String MENU_STYLE = GuiStyle.DEFAULT.name();

    @ConfigOpt
    @SelectorSetting(name = "gui.settings.pausemenustyle", items =
        {
            "HYPERIUM",
            "DEFAULT"
        }, category = MENUS
    )
    public static String PAUSE_STYLE = GuiStyle.HYPERIUM.name();

    @ConfigOpt
    public static boolean SPOTIFY_NOTIFICATIONS = false;

    @ConfigOpt
    public static boolean SPOTIFY_FORCE_DISABLE = true;

    @ConfigOpt
    @ToggleSetting(name = "gui.settings.updatenotificationsinhyperiumsettings", category = MISC)
    public static boolean UPDATE_NOTIFICATIONS = true;

    @ConfigOpt
    @ToggleSetting(name = "gui.settings.fastworldswitching", category = IMPROVEMENTS)
    public static boolean FAST_WORLD_LOADING = false;
    @ConfigOpt
    public static int MAX_WORLD_PARTICLES_INT = 10000;

    @ConfigOpt
    @SelectorSetting(category = IMPROVEMENTS, name = "gui.settings.maxworldparticles", items = {
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
    @ToggleSetting(name = "gui.settings.showparticlein1stperson", category = COSMETICS)
    public static boolean SHOW_PART_1ST_PERSON = false;

    @ConfigOpt
    @ToggleSetting(name = "Show Companion in 1st person", category = COSMETICS)
    public static boolean SHOW_COMPANION_IN_1ST_PERSON = true;
    @ConfigOpt
    @ToggleSetting(name = "gui.settings.showhitdistances", category = REACH, mods = true)
    public static boolean SHOW_HIT_DISTANCES = false;

    @ConfigOpt
    @ToggleSetting(name = "gui.settings.motionblurenabled", category = Category.MOTION_BLUR, mods = true)
    public static boolean MOTION_BLUR_ENABLED = false;

    @ConfigOpt
    @SliderSetting(name = "gui.settings.motionblurintensity", min = 0F, max = 7F, category = Category.MOTION_BLUR, mods = true)
    public static double MOTION_BLUR_AMOUNT = 4.0F;

    @ConfigOpt
    @ToggleSetting(name = "gui.settings.autofriendenabled", category = Category.AUTOFRIEND, mods = true)
    public static boolean AUTOFRIEND_TOGGLE = false;

    @ConfigOpt
    @ToggleSetting(name = "gui.settings.showfriendmessages", category = Category.AUTOFRIEND, mods = true)
    public static boolean AUTOFRIEND_MESSAGES = true;

    @ConfigOpt
    @ToggleSetting(name = "gui.settings.fortnitecompassenabled", category = Category.FNCOMPASS, mods = true)
    public static boolean FNCOMPASS_ENABLED = false;

    @ConfigOpt
    @ToggleSetting(name = "gui.settings.showbackground", category = Category.FNCOMPASS, mods = true)
    public static boolean FNCOMPASS_BACKGROUND = true;

    @ConfigOpt
    @ToggleSetting(name = "gui.settings.chroma", category = Category.FNCOMPASS, mods = true)
    public static boolean FNCOMPASS_CHROMA = false;

    @ConfigOpt
    @ToggleSetting(name = "gui.settings.shadow", category = Category.FNCOMPASS, mods = true)
    public static boolean FNCOMPASS_SHADOW = true;

    @ConfigOpt
    @SelectorSetting(category = Category.FNCOMPASS, name = "gui.settings.details", items = {
        "0",
        "1",
        "2"
    }, mods = true)
    public static String FNCOMPASS_DETAILS = "2";

    @ToggleSetting(name = "gui.settings.showuserdotsonnametags", category = INTEGRATIONS)
    public static boolean SHOW_DOTS_ON_NAME_TAGS = false;

    @ConfigOpt
    @SelectorSetting(name = "gui.settings.colortype", mods = true, category = REACH, items = {"RGB", "CHROMA"})
    public static String REACH_COLOR_TYPE = "RGB";

    @ConfigOpt
    @SliderSetting(name = "gui.settings.red", mods = true, category = REACH, isInt = true, min = 0, max = 255)
    public static int REACH_RED = 255;

    @ConfigOpt
    @SliderSetting(name = "gui.settings.blue", mods = true, category = REACH, isInt = true, min = 0, max = 255)
    public static int REACH_BLUE = 0;

    @ConfigOpt
    @SliderSetting(name = "gui.settings.green", mods = true, category = REACH, isInt = true, min = 0, max = 255)
    public static int REACH_GREEN = 255;

    @ConfigOpt
    @ToggleSetting(name = "gui.settings.togglesprint", category = TOGGLESPRINT, mods = true)
    public static boolean ENABLE_TOGGLE_SPRINT = true;

    @ConfigOpt
    @ToggleSetting(name = "gui.settings.broadcastlevelups", category = HYPIXEL)
    public static boolean BROADCAST_LEVELUPS = true;

    @ConfigOpt
    @ToggleSetting(name = "gui.settings.broadcastachievements", category = HYPIXEL)
    public static boolean BROADCAST_ACHIEVEMENTS = true;

    @ConfigOpt
    @ToggleSetting(name = "gui.settings.hyperiumprefix", category = GENERAL)
    public static boolean HYPERIUM_CHAT_PREFIX = true;

    @ConfigOpt
    @SelectorSetting(name = "gui.settings.showbutt", category = COSMETICS, items = {"YES", "NO"})
    public static String SHOW_BUTT = "YES";

    @ConfigOpt
    @ToggleSetting(name = "gui.settings.sendcurrentserver", category = GENERAL)
    public static boolean SEND_SERVER = true;

    @ConfigOpt
    @ToggleSetting(name = "gui.settings.sendguildwelcomemessage", category = HYPIXEL)
    public static boolean SEND_GUILD_WELCOME_MESSAGE = true;

    @ConfigOpt
    @ToggleSetting(name = "gui.settings.thankwatchdog", category = HYPIXEL)
    public static boolean THANK_WATCHDOG = false;

    @ConfigOpt
    @ToggleSetting(name = "gui.settings.itemphysics", category = ITEM_PHYSIC, mods = true)
    public static boolean ITEM_PHYSIC_ENABLED = false;

    @ConfigOpt
    public static String GUI_FONT = "Roboto Condensed";

    @ConfigOpt
    public static boolean BROWSER_DOWNLOAD = false;

    @ConfigOpt
    public static long TOTAL_PLAYTIME = 0;

    @ConfigOpt
    @ToggleSetting(name = "gui.settings.victoryroyale", category = VICTORYROYALE, mods = true)
    public static boolean VICTORY_ROYALE = false;

    @ConfigOpt
    @SelectorSetting(name = "gui.settings.mainmenuserver", category = GENERAL, items = {
        "HYPIXEL",
        "HIVE",
        "MINEPLEX",
        "CUBECRAFT",
        "MINESAGA",
        "SKYCADE"
    })
    public static String MAIN_MENU_SERVER = "HYPIXEL";

    @ConfigOpt
    @ToggleSetting(name = "gui.settings.betterf1", category = VANILLA_ENHANCEMENTS, mods = true)
    public static boolean BETTERF1 = false;

    @ConfigOpt
    @SelectorSetting(name = "gui.settings.buttonstyle", category = BUTTONS, items = {
        "HYPERIUM",
        "HYPERIUM 2"
    })
    public static String BUTTON_STYLE = ButtonStyle.HYPERIUM.name();

    @ConfigOpt
    @SliderSetting(name = "gui.settings.buttonred", isInt = true, min = 0, max = 255, category = BUTTONS)
    public static int BUTTON_RED = 255;

    @ConfigOpt
    @SliderSetting(name = "gui.settings.buttongreen", isInt = true, min = 0, max = 255, category = BUTTONS)
    public static int BUTTON_GREEN = 255;

    @ConfigOpt
    @SliderSetting(name = "gui.settings.buttonblue", isInt = true, min = 0, max = 255, category = BUTTONS)
    public static int BUTTON_BLUE = 255;

    @ConfigOpt
    @SelectorSetting(name = "gui.settings.buttontype", category = BUTTONS, items = {
        "DEFAULT",
        "RGB",
        "CHROMA"
    })
    public static String BUTTON_TYPE = ButtonType.DEFAULT.name();


    @ConfigOpt
    @ToggleSetting(name = "gui.settings.disableenchantglint", category = IMPROVEMENTS)
    public static boolean DISABLE_ENCHANT_GLINT = false;

    @ConfigOpt
    @ToggleSetting(name = "gui.settings.disabletitles", category = VANILLA_ENHANCEMENTS, mods = true)
    public static boolean HIDE_TITLES = false;

    @ConfigOpt
    @SliderSetting(name = "gui.settings.fpslimiteramount", isInt = true, min = 5, max = 60, category = IMPROVEMENTS)
    public static int FPS_LIMITER_AMOUNT = 30;

    @ConfigOpt
    @ToggleSetting(name = "gui.settings.disablelightning", category = IMPROVEMENTS)
    public static boolean DISABLE_LIGHTNING = false;

    /*

    For whoever tries to implement this, purpose is for when DISABLE_LIGHTNING is enabled, if the player still wants Lightning Bolts in UHC only, they will have to turn this on
    have fun trying to add it lol - asbyth

    @ConfigOpt
    //@ToggleSetting(name = "gui.settings.keeplightinguhc", category = IMPROVEMENTS)
    public static boolean UHC_LIGHTNING = false;
    */

    @ConfigOpt
    @ToggleSetting(name = "gui.settings.disablearmorstands", category = IMPROVEMENTS)
    public static boolean DISABLE_ARMORSTANDS = false;

    @ConfigOpt
    @ToggleSetting(name = "gui.settings.disableitemframes", category = IMPROVEMENTS)
    public static boolean DISABLE_ITEMFRAMES = false;

    @ConfigOpt
    @ToggleSetting(name = "gui.settings.confirmdisconnect", category = MISC)
    public static boolean CONFIRM_DISCONNECT = false;

    @ConfigOpt
    @ToggleSetting(name = "gui.settings.confirmquit", category = MISC)
    public static boolean CONFIRM_QUIT = false;

    private Settings() {
    }

    public static void register() {
        Hyperium.CONFIG.register(INSTANCE);
    }

    public static void save() {
        Hyperium.CONFIG.save();
    }
}
