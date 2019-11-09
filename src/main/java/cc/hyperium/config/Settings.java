/*
 *       Copyright (C) 2018-present Hyperium <https://hyperium.cc/>
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU Lesser General Public License as published
 *       by the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU Lesser General Public License for more details.
 *
 *       You should have received a copy of the GNU Lesser General Public License
 *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.config;

import cc.hyperium.Hyperium;

import static cc.hyperium.config.Category.*;

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
    public static boolean COMPACT_CHAT;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;voidflickerfixEnabled")
    @ToggleSetting(name = "gui.settings.voidflickerfix", category = IMPROVEMENTS)
    public static boolean VOID_FLICKER_FIX = true;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;framerateLimiterEnabled")
    @ToggleSetting(name = "gui.settings.fpslimiter", category = IMPROVEMENTS)
    public static boolean FPS_LIMITER = true;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;fastchatEnabled")
    @ToggleSetting(name = "gui.settings.fastchat", category = INTEGRATIONS)
    public static boolean FASTCHAT;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;shinyPotsEnabled")
    @ToggleSetting(name = "gui.settings.shinypotions", category = ANIMATIONS, mods = true)
    public static boolean SHINY_POTS;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;smartSoundsEnabled")
    @ToggleSetting(name = "gui.settings.smartsounds", category = IMPROVEMENTS)
    public static boolean SMART_SOUNDS;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;numberPingEnabled")
    @ToggleSetting(name = "gui.settings.numericping", category = VANILLA_ENHANCEMENTS, mods = true)
    public static boolean NUMBER_PING = true;

    @ConfigOpt
    @ToggleSetting(name = "gui.settings.itemcounter", category = VANILLA_ENHANCEMENTS, mods = true)
    public static boolean ITEM_COUNTER = true;

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
    public static boolean HOTBAR_KEYS;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;perspectiveHoldDownEnabled")
    @ToggleSetting(name = "gui.settings.holdperspectivekey")
    public static boolean PERSPECTIVE_HOLD;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;windowedFullScreen")
    @ToggleSetting(name = "gui.settings.windowedfullscreen", category = IMPROVEMENTS)
    public static boolean WINDOWED_FULLSCREEN;

    @ConfigOpt
    @SliderSetting(name = "gui.settings.guialpha", min = 0, max = 255, category = GENERAL)
    public static double SETTINGS_ALPHA = 100;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;uploadScreenshotsByDefault")
    @ToggleSetting(name = "gui.settings.uploadscreenshots")
    public static boolean DEFAULT_UPLOAD_SS;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;blurGuiBackgroundsEnabled")
    @ToggleSetting(name = "gui.settings.blurredguibackground")
    public static boolean BLUR_GUI;

    @ConfigOpt
    @SelectorSetting(name = "Hat Type", category = COSMETICS, items = "NONE")
    public static String HAT_TYPE = "NONE";

    @ConfigOpt
    @SelectorSetting(name = "Companion Type", category = COSMETICS, items = "NONE")
    public static String COMPANION_TYPE = "NONE";

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.GeneralSetting;hypixelZooEnabled")
    @ToggleSetting(name = "gui.settings.welcometothehypixelzoo", category = HYPIXEL)
    public static boolean HYPIXEL_ZOO = true;

    @ConfigOpt
    @ToggleSetting(category = COSMETICS, name = "gui.settings.showparticleauras")
    public static boolean SHOW_PARTICLES = true;

    @ConfigOpt
    @ToggleSetting(category = GENERAL, name = "Show own name tag")
    public static boolean SHOW_OWN_NAME;

    @ConfigOpt(alt = "cc.hyperium.handlers.handlers.OtherConfigOptions;showCosmeticsEveryWhere")
    @ToggleSetting(category = COSMETICS, name = "gui.settings.showcosmeticseverywhere")
    public static boolean SHOW_COSMETICS_EVERYWHERE = true;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.CosmeticSettings;dabToggle")
    public static boolean DAB_TOGGLE;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.CosmeticSettings;tPoseToggle")
    public static boolean TPOSE_TOGGLE;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.CosmeticSettings;flossDanceToggle")
    public static boolean FLOSS_TOGGLE;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.AnimationSettings;oldBlockhit")
    @ToggleSetting(name = "gui.settings.17blockhitting", category = ANIMATIONS, mods = true)
    public static boolean OLD_BLOCKHIT = true;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.AnimationSettings;oldBow")
    @ToggleSetting(name = "gui.settings.17bowposition", category = ANIMATIONS, mods = true)
    public static boolean OLD_BOW = true;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.AnimationSettings;oldRod")
    @ToggleSetting(name = "gui.settings.17rodposition", category = ANIMATIONS, mods = true)
    public static boolean OLD_ROD = true;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.AnimationSettings;redArmour")
    @ToggleSetting(name = "gui.settings.17redarmour", category = ANIMATIONS, mods = true)
    public static boolean OLD_ARMOUR = true;

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.AnimationSettings;oldEat")
    @ToggleSetting(name = "gui.settings.17eating", category = ANIMATIONS, mods = true)
    public static boolean OLD_EATING = true;

    @ConfigOpt
    @ToggleSetting(name = "gui.settings.17sneakinganimation", category = ANIMATIONS, mods = true)
    public static boolean OLD_SNEAKING;

    @ConfigOpt
    @ToggleSetting(name = "gui.settings.17blocking", category = ANIMATIONS, mods = true)
    public static boolean OLD_BLOCKING;

    @ConfigOpt
    @ToggleSetting(name = "gui.settings.17itemheld", category = ANIMATIONS, mods = true)
    public static boolean OLD_ITEM_HELD;

    @ConfigOpt
    @ToggleSetting(name = "gui.settings.17debug", category = ANIMATIONS, mods = true)
    public static boolean OLD_DEBUG;

    @ConfigOpt
    @ToggleSetting(name = "gui.settings.17health", category = ANIMATIONS, mods = true)
    public static boolean OLD_HEALTH;

    @ConfigOpt
    @ToggleSetting(name = "gui.settings.customswordanimation", category = ANIMATIONS, mods = true)
    public static boolean CUSTOM_SWORD_ANIMATION;

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
    @SelectorSetting(name = "gui.settings.background", category = MISC, items =
        {
            "1",
            "2",
            "3",
            "4",
            "5",
            "6",
            "7",
            "CUSTOM"
        }
    )
    public static String BACKGROUND = "1";

    @ConfigOpt(alt = "cc.hyperium.gui.settings.items.BackgroundSettings;fastWorldGuiEnabled")
    @ToggleSetting(name = "gui.settings.transparentcontainerbackgrounds")
    public static boolean FAST_CONTAINER;

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
    @SliderSetting(name = "gui.settings.headitemscale", category = ANIMATIONS, min = 1, max = 3, mods = true)
    public static double HEAD_SCALE_FACTOR = 1.0;

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
    @ToggleSetting(category = POPUP_EVENTS, mods = true, name = "gui.settings.showconfirmationpopup")
    public static boolean SHOW_INGAME_CONFIRMATION_POPUP = true;

    @ConfigOpt(alt = "cc.hyperium.handlers.handlers.OtherConfigOptions;savePreviusChatMessages")
    @ToggleSetting(category = GENERAL, name = "gui.settings.persistentchatmessages")
    public static boolean PERSISTENT_CHAT;

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
    public static double WINGS_OFFSET;

    @ConfigOpt
    @SliderSetting(name = "gui.settings.wingsscale", min = 50, max = 200, category = COSMETICS)
    public static double WINGS_SCALE = 100D;

    @ToggleSetting(name = "gui.settings.disable_dances", category = COSMETICS)
    @ConfigOpt
    public static boolean DISABLE_DANCES;

    @ConfigOpt
    @ToggleSetting(name = "gui.settings.fastworldswitching", category = IMPROVEMENTS)
    public static boolean FAST_WORLD_LOADING = true;

    @ConfigOpt
    @ToggleSetting(name = "gui.settings.showparticlein1stperson", category = COSMETICS)
    public static boolean SHOW_PART_1ST_PERSON;

    @ConfigOpt
    @ToggleSetting(name = "Show Companion in 1st person", category = COSMETICS)
    public static boolean SHOW_COMPANION_IN_1ST_PERSON = true;

    @ConfigOpt
    @ToggleSetting(name = "gui.settings.showhitdistances", category = REACH, mods = true)
    public static boolean SHOW_HIT_DISTANCES;

    @ConfigOpt
    @ToggleSetting(name = "gui.settings.motionblurenabled", category = Category.MOTION_BLUR, mods = true)
    public static boolean MOTION_BLUR_ENABLED;

    @ConfigOpt
    @SliderSetting(name = "gui.settings.motionblurintensity", min = 0F, max = 7F, category = Category.MOTION_BLUR, mods = true)
    public static double MOTION_BLUR_AMOUNT = 4.0F;

    @ToggleSetting(name = "gui.settings.showuserdotsonnametags", category = INTEGRATIONS)
    public static boolean SHOW_DOTS_ON_NAME_TAGS;

    @ConfigOpt
    @SelectorSetting(name = "gui.settings.colortype", mods = true, category = REACH, items = {"RGB", "CHROMA"})
    public static String REACH_COLOR_TYPE = "RGB";

    @ConfigOpt
    @SliderSetting(name = "gui.settings.red", mods = true, category = REACH, isInt = true, min = 0, max = 255)
    public static int REACH_RED = 255;

    @ConfigOpt
    @SliderSetting(name = "gui.settings.blue", mods = true, category = REACH, isInt = true, min = 0, max = 255)
    public static int REACH_BLUE = 255;

    @ConfigOpt
    @SliderSetting(name = "gui.settings.green", mods = true, category = REACH, isInt = true, min = 0, max = 255)
    public static int REACH_GREEN = 255;

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
    @ToggleSetting(name = "gui.settings.sendguildwelcomemessage", category = HYPIXEL)
    public static boolean SEND_GUILD_WELCOME_MESSAGE = true;

    @ConfigOpt
    @ToggleSetting(name = "gui.settings.thankwatchdog", category = HYPIXEL)
    public static boolean THANK_WATCHDOG;

    @ConfigOpt
    @ToggleSetting(name = "gui.settings.itemphysics", category = ITEM_PHYSIC, mods = true)
    public static boolean ITEM_PHYSIC_ENABLED;

    @ConfigOpt
    public static String GUI_FONT = "Roboto Condensed";

    @ConfigOpt
    @ToggleSetting(name = "gui.settings.betterf1", category = VANILLA_ENHANCEMENTS, mods = true)
    public static boolean BETTERF1;

    @ConfigOpt
    @ToggleSetting(name = "gui.settings.disableenchantglint", category = IMPROVEMENTS)
    public static boolean DISABLE_ENCHANT_GLINT;

    @ConfigOpt
    @ToggleSetting(name = "gui.settings.disabletitles", category = VANILLA_ENHANCEMENTS, mods = true)
    public static boolean HIDE_TITLES;

    @ConfigOpt
    @SliderSetting(name = "gui.settings.fpslimiteramount", isInt = true, min = 5, max = 60, category = IMPROVEMENTS)
    public static int FPS_LIMITER_AMOUNT = 30;

    @ConfigOpt
    @ToggleSetting(name = "gui.settings.disablelightning", category = IMPROVEMENTS)
    public static boolean DISABLE_LIGHTNING;

    @ConfigOpt
    @ToggleSetting(name = "gui.settings.disablearmorstands", category = IMPROVEMENTS)
    public static boolean DISABLE_ARMORSTANDS;

    @ConfigOpt
    @ToggleSetting(name = "gui.settings.disableitemframes", category = IMPROVEMENTS)
    public static boolean DISABLE_ITEMFRAMES;

    @ConfigOpt
    @ToggleSetting(name = "gui.settings.confirmdisconnect", category = MISC)
    public static boolean CONFIRM_DISCONNECT;

    @ConfigOpt
    @ToggleSetting(name = "gui.settings.confirmquit", category = MISC)
    public static boolean CONFIRM_QUIT;

    @ConfigOpt
    @ToggleSetting(name = "gui.settings.clearglass", category = GENERAL)
    public static boolean CLEAR_GLASS;

    @ConfigOpt
    @ToggleSetting(name = "gui.settings.portalsounds", category = VANILLA_ENHANCEMENTS, mods = true)
    public static boolean PORTAL_SOUNDS;

    @ConfigOpt
    @ToggleSetting(name = "gui.settings.disableachievements", category = VANILLA_ENHANCEMENTS, mods = true)
    public static boolean DISABLE_ACHIEVEMENTS;

    @ConfigOpt
    @ToggleSetting(name = "gui.settings.disableshadowtext", category = IMPROVEMENTS)
    public static boolean DISABLE_SHADOW_TEXT;

    @ConfigOpt
    @ToggleSetting(name = "gui.settings.shinypotions.matchcolor", category = ANIMATIONS, mods = true)
    public static boolean SHINY_POTS_MATCH_COLOR;

    @ConfigOpt
    @ToggleSetting(name = "gui.settings.automyposition.enabled", category = AUTOMYPOSITION, mods = true)
    public static boolean AUTO_MY_POSITION;

    @ConfigOpt
    @SliderSetting(name = "gui.settings.automyposition.delay", min = 0, max = 5, category = AUTOMYPOSITION, mods = true)
    public static double AUTO_MY_POSITION_DELAY = 2;

    @ConfigOpt
    @ToggleSetting(name = "gui.settings.bossbarmod.bossbarall", category = BOSSBARMOD, mods = true)
    public static boolean BOSSBAR_ALL = true;

    @ConfigOpt
    @ToggleSetting(name = "gui.settings.bossbarmod.bossbartext", category = BOSSBARMOD, mods = true)
    public static boolean BOSSBAR_TEXT = true;

    @ConfigOpt
    @ToggleSetting(name = "gui.settings.bossbarmod.bossbarhealthbar", category = BOSSBARMOD, mods = true)
    public static boolean BOSSBAR_BAR = true;

    @ConfigOpt
    @SliderSetting(name = "gui.settings.bossbarmod.bossbarscale", min = 0.25F, max = 2.0F, category = BOSSBARMOD, mods = true)
    public static double BOSSBAR_SCALE = 1.0;

    @ConfigOpt
    public static double BOSSBAR_X = .5;

    @ConfigOpt
    public static double BOSSBAR_Y = .05;

    @ConfigOpt
    @ToggleSetting(name = "gui.settings.hyperiumloadingscreen", category = MISC)
    public static boolean HYPERIUM_LOADING_SCREEN = true;

    @ConfigOpt
    @SliderSetting(name = "gui.settings.fireheight", min = -2, max = 2, category = GENERAL)
    public static double FIRE_HEIGHT;

    @ConfigOpt
    @ToggleSetting(name = "gui.settings.entityradiustoggle", category = ENTITYRADIUS, mods = true)
    public static boolean ENABLE_ENTITY_RADIUS;

    @ConfigOpt
    @SliderSetting(name = "gui.settings.entityradius", min = 1, max = 64, category = ENTITYRADIUS, mods = true)
    public static double ENTITY_RADIUS = 64;

    @ConfigOpt
    @ToggleSetting(name = "gui.settings.hyperiumtips", category = GENERAL)
    public static boolean HYPERIUM_TIPS = true;

    @ConfigOpt
    @ToggleSetting(name = "gui.settings.disableendportals", category = IMPROVEMENTS)
    public static boolean DISABLE_END_PORTALS;

    @ConfigOpt
    @ToggleSetting(name = "gui.settings.hideleatherarmor", category = HYPIXEL)
    public static boolean HIDE_LEATHER_ARMOR;

    @ConfigOpt
    public static String SERVER_BUTTON_NAME = "Join Hypixel";

    @ConfigOpt
    public static String SERVER_IP = "mc.hypixel.net";

    @ConfigOpt
    @SliderSetting(name = "gui.settings.sprintfovmodifier", min = -5, max = 5, category = FOV_MODIFIER, mods = true)
    public static double SPRINTING_FOV_MODIFIER = 1;

    @ConfigOpt
    @SliderSetting(name = "gui.settings.bowfovmodifier", min = -5, max = 5, category = FOV_MODIFIER, mods = true)
    public static double BOW_FOV_MODIFIER = 1;

    @ConfigOpt
    @SliderSetting(name = "gui.settings.speedfovmodifier", min = -5, max = 5, category = FOV_MODIFIER, mods = true)
    public static double SPEED_FOV_MODIFIER = 1;

    @ConfigOpt
    @SliderSetting(name = "gui.settings.slownessfovmodifier", min = -5, max = 5, category = FOV_MODIFIER, mods = true)
    public static double SLOWNESS_FOV_MODIFIER = 1;

    public static void register() {
        Hyperium.CONFIG.register(INSTANCE);
    }

    public static void save() {
        Hyperium.CONFIG.save();
    }
}
