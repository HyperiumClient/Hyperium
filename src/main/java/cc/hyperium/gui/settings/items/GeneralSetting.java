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

package cc.hyperium.gui.settings.items;

import cc.hyperium.GuiStyle;
import cc.hyperium.Hyperium;
import cc.hyperium.config.ConfigOpt;
import cc.hyperium.config.DefaultConfig;
import cc.hyperium.gui.HyperiumGui;
import cc.hyperium.gui.settings.SettingGui;
import cc.hyperium.gui.settings.components.SelectionItem;
import net.minecraft.client.Minecraft;

import java.util.Arrays;

/**
 * A class containing all the main settings for Hyperium,
 * the settings here are saved when the gui is closed
 */
@SuppressWarnings({"unchecked", "FieldCanBeLocal"})
public class GeneralSetting extends SettingGui {

    /**
     * The configuration instance, for all the settings below
     */
    private DefaultConfig config;

    @ConfigOpt
    public static boolean discordRPEnabled = true;
    @ConfigOpt
    public static boolean fullbrightEnabled = true;
    @ConfigOpt
    public static boolean romanNumeralsEnabled = true;
    @ConfigOpt
    public static boolean discordServerDisplayEnabled = true;
    @ConfigOpt
    public static boolean compactChatEnabled = false;
    @ConfigOpt
    public static boolean voidflickerfixEnabled = true;
    @ConfigOpt
    public static boolean framerateLimiterEnabled = true;
    @ConfigOpt
    public static boolean fastchatEnabled = false;
    @ConfigOpt
    public static boolean shinyPotsEnabled = false;
    @ConfigOpt
    public static boolean smartSoundsEnabled = false;
    @ConfigOpt
    public static boolean numberPingEnabled = true;
    @ConfigOpt
    public static boolean combatParticleFixEnabled = true;
    @ConfigOpt
    public static boolean perspectiveHoldDownEnabled = false;
    @ConfigOpt
    public static String menuStyle = GuiStyle.DEFAULT.toString();
    @ConfigOpt
    public static boolean windowedFullScreen = false;
    @ConfigOpt
    public static boolean bossBarTextOnlyEnabled = false;
    @ConfigOpt
    public static boolean staticFovEnabled = false;
    @ConfigOpt
    public static boolean uploadScreenshotsByDefault = false;
    @ConfigOpt
    public static boolean hideScoreboardNumbers = true;
    @ConfigOpt
    public static boolean blurGuiBackgroundsEnabled = true;
    @ConfigOpt
    public static boolean chromaHudNonHypixelEnabled = true;
    @ConfigOpt
    public static boolean screenshotOnKillEnabled = false;

    private SelectionItem<String> discordRP;

    private SelectionItem<String> fullBright;

    private SelectionItem<String> romanNumerals;

    private SelectionItem<String> discordServerDisplay;

    private SelectionItem<String> compactChat;

    private SelectionItem<String> voidflickerfix;

    private SelectionItem<String> framerateLimiter;

    private SelectionItem<String> shinyPots;

    private SelectionItem<String> smartSounds;

    private SelectionItem<String> numberPing;

    private SelectionItem<String> combatParticleFix;

    private SelectionItem<String> perspectiveHold;

    private SelectionItem<String> menuStyleSelection;

    private SelectionItem<String> fullScreenStyle;

    private SelectionItem<String> bossBarTextOnly;

    private SelectionItem<String> staticFov;

    private SelectionItem<String> uploadByDefault;

    private SelectionItem<String> scoreboardNumbers;

    private SelectionItem<String> blurGuiBackgrounds;

    private SelectionItem<String> chromaHudNonHypixel;

    private SelectionItem<String> screenshotOnKill;

    /** Set to true when a setting is changed, this will trigger a save when the gui is closed */
    private boolean settingsUpdated;

    public GeneralSetting(HyperiumGui previous) {
        super("GENERAL", previous);
        this.config = Hyperium.CONFIG;
        this.config.register(this);
    }

    @Override
    protected void pack() {
        super.pack();

        this.settingItems.add(this.discordRP = new SelectionItem(0, getX(), getDefaultItemY(0), this.width - getX() * 2, "DISCORD RICH PRESENCE", i -> {
            ((SelectionItem) i).nextItem();
            discordRPEnabled = ((SelectionItem) i).getSelectedItem().equals("ON");
            this.settingsUpdated = true;
        }));
        this.discordRP.addDefaultOnOff();
        this.discordRP.setSelectedItem(discordRPEnabled ? "ON" : "OFF");

        this.settingItems.add(this.discordServerDisplay = new SelectionItem<>(1, getX(), getDefaultItemY(1), this.width - getX() * 2, "DISCORD DISPLAY SERVER", i -> {
            ((SelectionItem<String>) i).nextItem();
            discordServerDisplayEnabled = ((SelectionItem) i).getSelectedItem().equals("ON");
            this.settingsUpdated = true;
        }));
        this.discordServerDisplay.addDefaultOnOff();
        this.discordServerDisplay.setSelectedItem(discordServerDisplayEnabled ? "ON" : "OFF");

        this.settingItems.add(this.fullBright = new SelectionItem(2, getX(), getDefaultItemY(2), this.width - getX() * 2, "FULLBRIGHT", i -> {
            ((SelectionItem) i).nextItem();
            fullbrightEnabled = ((SelectionItem) i).getSelectedItem().equals("ON");
            this.settingsUpdated = true;
        }));
        this.fullBright.addDefaultOnOff();
        this.fullBright.setSelectedItem(fullbrightEnabled ? "ON" : "OFF");

        this.settingItems.add(this.romanNumerals = new SelectionItem(3, getX(), getDefaultItemY(3), this.width - getX() * 2, "ROMAN NUMERALS", i -> {
            ((SelectionItem) i).nextItem();
            romanNumeralsEnabled = ((SelectionItem) i).getSelectedItem().equals("ON");
            this.settingsUpdated = true;
        }));
        this.romanNumerals.addDefaultOnOff();
        this.romanNumerals.setSelectedItem(romanNumeralsEnabled ? "ON" : "OFF");

        this.settingItems.add(this.compactChat = new SelectionItem(4, getX(), getDefaultItemY(4), this.width - getX() * 2, "COMPACT CHAT", i -> {
            ((SelectionItem) i).nextItem();
            compactChatEnabled = ((SelectionItem) i).getSelectedItem().equals("ON");
            this.settingsUpdated = true;
        }));
        this.compactChat.addDefaultOnOff();
        this.compactChat.setSelectedItem(compactChatEnabled ? "ON" : "OFF");

        this.settingItems.add(this.voidflickerfix = new SelectionItem(5, getX(), getDefaultItemY(5), this.width - getX() * 2, "VOID FLICKER FIX", i -> {
            ((SelectionItem) i).nextItem();
            voidflickerfixEnabled = ((SelectionItem) i).getSelectedItem().equals("ON");
            this.settingsUpdated = true;
        }));
        this.voidflickerfix.addDefaultOnOff();
        this.voidflickerfix.setSelectedItem(voidflickerfixEnabled ? "ON" : "OFF");

        this.settingItems.add(this.framerateLimiter = new SelectionItem(6, getX(), getDefaultItemY(6), this.width - getX() * 2, "SMART FRAMERATE", i -> {
            ((SelectionItem) i).nextItem();
            framerateLimiterEnabled = ((SelectionItem) i).getSelectedItem().equals("ON");
            this.settingsUpdated = true;
        }));
        this.framerateLimiter.addDefaultOnOff();
        this.framerateLimiter.setSelectedItem(framerateLimiterEnabled ? "ON" : "OFF");

        this.settingItems.add(this.shinyPots = new SelectionItem(7, getX(), getDefaultItemY(7), this.width - getX() * 2, "SHINY POTS", i -> {
            ((SelectionItem) i).nextItem();
            shinyPotsEnabled = ((SelectionItem) i).getSelectedItem().equals("ON");
            this.settingsUpdated = true;
        }));
        this.shinyPots.addDefaultOnOff();
        this.shinyPots.setSelectedItem(shinyPotsEnabled ? "ON" : "OFF");

        this.settingItems.add(
                this.smartSounds = new SelectionItem(8, getX(), getDefaultItemY(8), this.width - getX() * 2, "BETTER SOUNDS", i -> {
                    ((SelectionItem) i).nextItem();
                    smartSoundsEnabled = ((SelectionItem) i).getSelectedItem().equals("ON");
                    this.settingsUpdated = true;
                }));
        this.smartSounds.addDefaultOnOff();
        this.smartSounds.setSelectedItem(smartSoundsEnabled ? "ON" : "OFF");

        this.settingItems.add(this.numberPing = new SelectionItem(9, getX(), getDefaultItemY(9), this.width - getX() * 2, "PING NUMBER", i -> {
            ((SelectionItem) i).nextItem();
            numberPingEnabled = ((SelectionItem) i).getSelectedItem().equals("ON");
            this.settingsUpdated = true;
        }));
        numberPing.addDefaultOnOff();
        numberPing.setSelectedItem(numberPingEnabled ? "ON" : "OFF");

        this.settingItems.add(this.combatParticleFix = new SelectionItem(10, getX(), getDefaultItemY(10), this.width - getX() * 2, "COMBAT PARTICLE FIX", i -> {
            ((SelectionItem) i).nextItem();
            combatParticleFixEnabled = ((SelectionItem) i).getSelectedItem().equals("ON");
            this.settingsUpdated = true;
        }));
        combatParticleFix.addDefaultOnOff();
        combatParticleFix.setSelectedItem(combatParticleFixEnabled ? "ON" : "OFF");

        this.settingItems.add(this.perspectiveHold = new SelectionItem(11, getX(), getDefaultItemY(11), this.width - getX() * 2, "PERSPECTIVE HOLD", i -> {
            ((SelectionItem) i).nextItem();
            perspectiveHoldDownEnabled = ((SelectionItem) i).getSelectedItem().equals("ON");
            this.settingsUpdated = true;
        }));
        perspectiveHold.addDefaultOnOff();
        perspectiveHold.setSelectedItem(perspectiveHoldDownEnabled ? "ON" : "OFF");

        this.settingItems.add(this.menuStyleSelection = new SelectionItem(12, getX(), getDefaultItemY(12), this.width - getX() * 2, "MENU STYLE", i -> {
            ((SelectionItem) i).nextItem();
            menuStyle = (String) ((SelectionItem) i).getSelectedItem();
            this.settingsUpdated = true;
        }));
        Arrays.stream(GuiStyle.values()).forEach(s -> this.menuStyleSelection.addItem(s.toString()));
        this.menuStyleSelection.setSelectedItem(menuStyle);

        this.settingItems.add(this.fullScreenStyle = new SelectionItem<>(13, getX(), getDefaultItemY(13),this.width - getX() * 2, "WINDOWED FULLSCREEN", i -> {
            ((SelectionItem) i).nextItem();
            windowedFullScreen = ((SelectionItem) i).getSelectedItem().equals("ON");
            this.settingsUpdated = true;
            // redo it
            Minecraft.getMinecraft().toggleFullscreen();
            try {
                // hacky fix to concurrent exception
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Minecraft.getMinecraft().toggleFullscreen();
        }));
        fullScreenStyle.addDefaultOnOff();
        fullScreenStyle.setSelectedItem(windowedFullScreen ? "ON" : "OFF");

        this.settingItems.add(this.bossBarTextOnly = new SelectionItem(14, getX(), getDefaultItemY(14), this.width - getX() * 2, "BOSSBAR TEXT ONLY", i -> {
            ((SelectionItem) i).nextItem();
            bossBarTextOnlyEnabled = ((SelectionItem) i).getSelectedItem().equals("ON");
            this.settingsUpdated = true;
        }));
        bossBarTextOnly.addDefaultOnOff();
        bossBarTextOnly.setSelectedItem(bossBarTextOnlyEnabled ? "ON" : "OFF");

        this.settingItems.add(this.staticFov = new SelectionItem(15, getX(), getDefaultItemY(15), this.width - getX() * 2, "STATIC FOV", i -> {
            ((SelectionItem) i).nextItem();
            staticFovEnabled = ((SelectionItem) i).getSelectedItem().equals("ON");
            this.settingsUpdated = true;
        }));
        staticFov.addDefaultOnOff();
        staticFov.setSelectedItem(staticFovEnabled ? "ON" : "OFF");

        this.settingItems.add(this.uploadByDefault = new SelectionItem(16, getX(), getDefaultItemY(16), this.width - getX() * 2, "UPLOAD SCREENSHOTS BY DEFAULT", i -> {
            ((SelectionItem) i).nextItem();
            uploadScreenshotsByDefault = ((SelectionItem) i).getSelectedItem().equals("ON");
            this.settingsUpdated = true;
        }));
        uploadByDefault.addDefaultOnOff();
        uploadByDefault.setSelectedItem(uploadScreenshotsByDefault ? "ON" : "OFF");

        this.settingItems.add(this.scoreboardNumbers = new SelectionItem<>(17, getX(), getDefaultItemY(17), this.width - getX() * 2, "HIDE SCOREBOARD NUMBERS", i -> {
            ((SelectionItem) i).nextItem();
            hideScoreboardNumbers = ((SelectionItem) i).getSelectedItem().equals("ON");
            this.settingsUpdated = true;
        }));
        scoreboardNumbers.addDefaultOnOff();
        scoreboardNumbers.setSelectedItem(hideScoreboardNumbers ? "ON" : "OFF");

        this.settingItems.add(this.blurGuiBackgrounds = new SelectionItem<>(18, getX(), getDefaultItemY(18), this.width - getX() * 2, "BLUR GUI BACKGROUNDS", i -> {
            ((SelectionItem) i).nextItem();
            blurGuiBackgroundsEnabled = ((SelectionItem) i).getSelectedItem().equals("ON");
            this.settingsUpdated = true;
        }));
        blurGuiBackgrounds.addDefaultOnOff();
        blurGuiBackgrounds.setSelectedItem(blurGuiBackgroundsEnabled ? "ON" : "OFF");

        this.settingItems.add(this.chromaHudNonHypixel = new SelectionItem<>(19, getX(), getDefaultItemY(19), this.width - getX() * 2, "DISPLAY CHROMA-HUD ON OTHER SERVERS", i -> {
            ((SelectionItem) i).nextItem();
            chromaHudNonHypixelEnabled = ((SelectionItem) i).getSelectedItem().equals("ON");
            this.settingsUpdated = true;
        }));
        chromaHudNonHypixel.addDefaultOnOff();
        chromaHudNonHypixel.setSelectedItem(chromaHudNonHypixelEnabled ? "ON" : "OFF");

        this.settingItems.add(this.screenshotOnKill = new SelectionItem<>(20, getX(), getDefaultItemY(20), this.width - getX() * 2, "TAKE SCREENSHOT ON KILL", i -> {
            ((SelectionItem) i).nextItem();
            screenshotOnKillEnabled = ((SelectionItem) i).getSelectedItem().equals("ON");
            this.settingsUpdated = true;
        }));
        screenshotOnKill.addDefaultOnOff();
        screenshotOnKill.setSelectedItem(screenshotOnKillEnabled ? "ON" : "OFF");
    }

    /**
     * A getter for the y location of the given setting
     *
     * @param i the item number
     * @return the y location the element will be rendered at
     */
    private int getDefaultItemY(int i) {
        return getY() + 25 + i * 15;
    }

    @Override
    public void onGuiClosed() {
        // If a setting has been modified, we'll save the config.
        if (this.settingsUpdated) {
            this.config.save();
        }
    }
}