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

  @SuppressWarnings("InstantiationOfUtilityClass")
  public static final Settings INSTANCE = new Settings();

  @ConfigOpt
  @ToggleSetting(name = "gui.settings.discordrp", category = DISCORD, mods = true)
  public static boolean DISCORD_RP = true;

  @ConfigOpt
  @ToggleSetting(name = "gui.settings.rpshowserver", category = DISCORD, mods = true)
  public static boolean DISCORD_RP_SERVER = true;

  @ConfigOpt
  @ToggleSetting(name = "gui.settings.fullbright", category = QOL)
  public static boolean FULLBRIGHT = true;

  @ConfigOpt
  @ToggleSetting(name = "gui.settings.romannumerals", category = QOL)
  public static boolean ROMAN_NUMERALS = true;


  @ConfigOpt
  @ToggleSetting(name = "gui.settings.compactchat", category = QOL)
  public static boolean COMPACT_CHAT;

  @ConfigOpt
  @ToggleSetting(name = "gui.settings.voidflickerfix", category = QOL)
  public static boolean VOID_FLICKER_FIX = true;

  @ConfigOpt
  @ToggleSetting(name = "gui.settings.fpslimiter", category = HYPIXEL)
  public static boolean FPS_LIMITER = true;

  @ConfigOpt
  @ToggleSetting(name = "gui.settings.fastchat", category = PERFORMANCE)
  public static boolean FASTCHAT;

  @ConfigOpt
  @ToggleSetting(name = "gui.settings.shinypotions", category = ANIMATIONS, mods = true)
  public static boolean SHINY_POTS;

  @ConfigOpt
  @ToggleSetting(name = "gui.settings.smartsounds", category = QOL)
  public static boolean SMART_SOUNDS;

  @ConfigOpt
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

  @ConfigOpt
  @ToggleSetting(name = "gui.settings.holdperspectivekey", category = QOL)
  public static boolean PERSPECTIVE_HOLD;

  @ConfigOpt
  @ToggleSetting(name = "gui.settings.windowedfullscreen", category = QOL)
  public static boolean WINDOWED_FULLSCREEN;

  @ConfigOpt
  @SliderSetting(name = "gui.settings.guialpha", min = 0, max = 255, category = GENERAL)
  public static double SETTINGS_ALPHA = 100;

  @ConfigOpt
  @ToggleSetting(name = "gui.settings.uploadscreenshots")
  public static boolean DEFAULT_UPLOAD_SS;

  @ConfigOpt
  @ToggleSetting(name = "gui.settings.blurredguibackground")
  public static boolean BLUR_GUI;

  @ConfigOpt
  @SelectorSetting(name = "Hat Type", category = COSMETICS, items = "NONE")
  public static String HAT_TYPE = "NONE";

  @ConfigOpt
  @SelectorSetting(name = "Companion Type", category = COSMETICS, items = "NONE")
  public static String COMPANION_TYPE = "NONE";

  @ConfigOpt
  @ToggleSetting(name = "gui.settings.welcometothehypixelzoo", category = HYPIXEL)
  public static boolean HYPIXEL_ZOO = true;

  @ConfigOpt
  @ToggleSetting(category = COSMETICS, name = "gui.settings.showparticleauras")
  public static boolean SHOW_PARTICLES = true;

  @ConfigOpt
  @ToggleSetting(category = GENERAL, name = "Show own name tag")
  public static boolean SHOW_OWN_NAME;

  @ConfigOpt
  @ToggleSetting(category = COSMETICS, name = "gui.settings.showcosmeticseverywhere")
  public static boolean SHOW_COSMETICS_EVERYWHERE = true;

  @ConfigOpt
  @ToggleSetting(name = "gui.settings.17blockhitting", category = ANIMATIONS, mods = true)
  public static boolean OLD_BLOCKHIT = true;

  @ConfigOpt
  @ToggleSetting(name = "gui.settings.17bowposition", category = ANIMATIONS, mods = true)
  public static boolean OLD_BOW = true;

  @ConfigOpt
  @ToggleSetting(name = "gui.settings.17rodposition", category = ANIMATIONS, mods = true)
  public static boolean OLD_ROD = true;

  @ConfigOpt
  @ToggleSetting(name = "gui.settings.17redarmour", category = ANIMATIONS, mods = true)
  public static boolean OLD_ARMOUR = true;

  @ConfigOpt
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

  @ConfigOpt
  @ToggleSetting(name = "gui.settings.fliptogglemode", category = COSMETICS)
  public static boolean isFlipToggle = true;

  @ConfigOpt
  public static int flipType = 1;

  @ConfigOpt
  @SelectorSetting(name = "gui.settings.fliptype", category = COSMETICS, items = {})
  public static String FLIP_TYPE_STRING = "FLIP";

  @ConfigOpt
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

  @ConfigOpt
  @ToggleSetting(name = "gui.settings.transparentcontainerbackgrounds", category = PERFORMANCE)
  public static boolean FAST_CONTAINER;

  @ConfigOpt
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

  @ConfigOpt
  public static int MAX_PARTICLES = 200;

  @ConfigOpt
  @SelectorSetting(name = "gui.settings.maxparticles", category = COSMETICS, items = {"200"})
  // Items configured in override
  public static String MAX_PARTICLE_STRING = "200";

  @ConfigOpt
  @SliderSetting(name = "gui.settings.headitemscale", category = ANIMATIONS, min = 1, max = 3, mods = true)
  public static double HEAD_SCALE_FACTOR = 1.0;

  @ConfigOpt
  @ToggleSetting(name = "gui.settings.particlesininventory", category = COSMETICS)
  public static boolean PARTICLES_INV = true;

  @ConfigOpt
  @SelectorSetting(name = "gui.settings.deadmau5ears", category = COSMETICS, items = {})
  public static String EARS_STATE = "ON";

  @ConfigOpt
  @ToggleSetting(category = MISC, name = "gui.settings.shownotificationcenter")
  public static boolean SHOW_INGAME_NOTIFICATION_CENTER = true;

  @ConfigOpt
  @ToggleSetting(category = POPUP_EVENTS, mods = true, name = "gui.settings.showconfirmationpopup")
  public static boolean SHOW_INGAME_CONFIRMATION_POPUP = true;

  @ConfigOpt
  @ToggleSetting(category = GENERAL, name = "gui.settings.persistentchatmessages")
  public static boolean PERSISTENT_CHAT;

  @ConfigOpt
  @ToggleSetting(category = HYPIXEL, name = "gui.settings.friendsfirstintab")
  public static boolean FRIENDS_FIRST_IN_TAB = true;

  @ConfigOpt
  @ToggleSetting(category = GENERAL, name = "gui.settings.onlineindicator")
  public static boolean SHOW_ONLINE_PLAYERS = true;

  @ConfigOpt
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

  @ConfigOpt
  @ToggleSetting(name = "gui.settings.fastworldswitching", category = PERFORMANCE)
  public static boolean FAST_WORLD_LOADING = true;

  @ConfigOpt
  @ToggleSetting(name = "gui.settings.showparticlein1stperson", category = COSMETICS)
  public static boolean SHOW_PART_1ST_PERSON;

  @ConfigOpt
  @ToggleSetting(name = "Show Companion in 1st person", category = COSMETICS)
  public static boolean SHOW_COMPANION_IN_1ST_PERSON = true;

  @ConfigOpt
  @ToggleSetting(name = "gui.settings.motionblurenabled", category = Category.MOTION_BLUR, mods = true)
  public static boolean MOTION_BLUR_ENABLED;

  @ConfigOpt
  @SliderSetting(name = "gui.settings.motionblurintensity", min = 0F, max = 7F, category = Category.MOTION_BLUR, mods = true)
  public static double MOTION_BLUR_AMOUNT = 4.0F;

  @ToggleSetting(name = "gui.settings.showuserdotsonnametags", category = GENERAL)
  public static boolean SHOW_DOTS_ON_NAME_TAGS;

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
  @SliderSetting(name = "gui.settings.itemphysicsspeed", min = 0, max = 5, category = ITEM_PHYSIC, mods = true)
  public static double ITEM_PHYSIC_SPEED = 1;

  @ConfigOpt
  public static String GUI_FONT = "Roboto Condensed";

  @ConfigOpt
  @ToggleSetting(name = "gui.settings.betterf1", category = VANILLA_ENHANCEMENTS, mods = true)
  public static boolean BETTERF1;

  @ConfigOpt
  @ToggleSetting(name = "gui.settings.disableenchantglint", category = PERFORMANCE)
  public static boolean DISABLE_ENCHANT_GLINT;

  @ConfigOpt
  @ToggleSetting(name = "gui.settings.disabletitles", category = VANILLA_ENHANCEMENTS, mods = true)
  public static boolean HIDE_TITLES;

  @ConfigOpt
  @SliderSetting(name = "gui.settings.fpslimiteramount", isInt = true, min = 5, max = 60, category = HYPIXEL)
  public static int FPS_LIMITER_AMOUNT = 30;

  @ConfigOpt
  @ToggleSetting(name = "gui.settings.disablelightning", category = PERFORMANCE)
  public static boolean DISABLE_LIGHTNING;

  @ConfigOpt
  @ToggleSetting(name = "gui.settings.disablearmorstands", category = PERFORMANCE)
  public static boolean DISABLE_ARMORSTANDS;

  @ConfigOpt
  @ToggleSetting(name = "gui.settings.disableitemframes", category = PERFORMANCE)
  public static boolean DISABLE_ITEMFRAMES;

  @ConfigOpt
  @ToggleSetting(name = "gui.settings.confirmdisconnect", category = MISC)
  public static boolean CONFIRM_DISCONNECT;

  @ConfigOpt
  @ToggleSetting(name = "gui.settings.confirmquit", category = MISC)
  public static boolean CONFIRM_QUIT;

  @ConfigOpt
  @ToggleSetting(name = "gui.settings.clearglass", category = QOL)
  public static boolean CLEAR_GLASS;

  @ConfigOpt
  @ToggleSetting(name = "gui.settings.portalsounds", category = VANILLA_ENHANCEMENTS, mods = true)
  public static boolean PORTAL_SOUNDS;

  @ConfigOpt
  @ToggleSetting(name = "gui.settings.disableachievements", category = VANILLA_ENHANCEMENTS, mods = true)
  public static boolean DISABLE_ACHIEVEMENTS;

  @ConfigOpt
  @ToggleSetting(name = "gui.settings.disableshadowtext", category = PERFORMANCE)
  public static boolean DISABLE_SHADOW_TEXT;

  @ConfigOpt
  @ToggleSetting(name = "gui.settings.shinypotions.matchcolor", category = ANIMATIONS, mods = true)
  public static boolean SHINY_POTS_MATCH_COLOR;

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
  @ToggleSetting(name = "gui.settings.disableendportals", category = PERFORMANCE)
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

  @ConfigOpt
  @SliderSetting(name = "gui.settings.wingsred", min = 0, max = 255, isInt = true, category = COSMETICS)
  public static int WINGS_RED = 255;

  @ConfigOpt
  @SliderSetting(name = "gui.settings.wingsgreen", min = 0, max = 255, isInt = true, category = COSMETICS)
  public static int WINGS_GREEN = 255;

  @ConfigOpt
  @SliderSetting(name = "gui.settings.wingsblue", min = 0, max = 255, isInt = true, category = COSMETICS)
  public static int WINGS_BLUE = 255;

  @ConfigOpt
  @ToggleSetting(name = "gui.settings.tabheads", category = PERFORMANCE)
  public static boolean TAB_HEADS = true;

  @ConfigOpt
  @ToggleSetting(name = "gui.settings.gpucloudrenderer", category = PERFORMANCE)
  public static boolean GPU_CLOUD_RENDERER;

  @ConfigOpt
  @ToggleSetting(name = "gui.settings.showofflinedots", category = GENERAL)
  public static boolean OFFLINE_DOTS = true;

  @ConfigOpt
  @ToggleSetting(name = "gui.settings.customhypixeljoinleave", category = HYPIXEL)
  public static boolean CUSTOM_JOIN_LEAVE_MESSAGES = true;

  @ConfigOpt
  @ToggleSetting(name = "gui.settings.cleanermenus", category = QOL)
  public static boolean CLEANER_MENUS;

  @ConfigOpt
  @ToggleSetting(name = "gui.settings.smoothchat", category = QOL)
  public static boolean SMOOTH_CHAT;

  @ConfigOpt
  @ToggleSetting(name = "gui.settings.disablearrowentities", category = PERFORMANCE)
  public static boolean DISABLE_ARROW_ENTITIES;

  @ConfigOpt
  @ToggleSetting(name = "gui.settings.doubleplantfix", category = QOL)
  public static boolean DOUBLE_PLANT_FIX;

  @ConfigOpt
  @ToggleSetting(name = "gui.settings.chatkeeper", category = QOL)
  public static boolean CHAT_KEEPER = true;

  @ConfigOpt
  @ToggleSetting(name = "gui.settings.crosshairperspective", category = QOL)
  public static boolean CROSSHAIR_PERSPECTIVE;

  @ConfigOpt
  @SliderSetting(name = "gui.settings.tabopacity", category = QOL, min = 0, max = 100, isInt = true)
  public static int TAB_OPACITY = 100;

  @ConfigOpt
  @ToggleSetting(name = "gui.settings.customtabopacity", category = QOL)
  public static boolean CUSTOM_TAB_OPACITY;

  public static void register() {
    Hyperium.CONFIG.register(INSTANCE);
  }

  public static void save() {
    Hyperium.CONFIG.save();
  }
}
