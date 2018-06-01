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
import cc.hyperium.gui.settings.SettingItem;
import cc.hyperium.gui.settings.components.OnOffSetting;
import cc.hyperium.gui.settings.components.SelectionItem;
import net.minecraft.client.Minecraft;

import java.util.Arrays;
import java.util.function.Consumer;

/**
 * A class containing all the main settings for Hyperium,
 * the settings here are saved when the gui is closed
 */
@SuppressWarnings({"unchecked", "FieldCanBeLocal"})
public class GeneralSetting extends SettingGui {

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
    public static boolean blurGuiBackgroundsEnabled = false;
    @ConfigOpt
    public static boolean chromaHudNonHypixelEnabled = true;
    @ConfigOpt
    public static boolean screenshotOnKillEnabled = false;
    @ConfigOpt
    public static boolean spotifyControlsEnabled = false;
    @ConfigOpt
    public static boolean hypixelZooEnabled = false;
    /**
     * The configuration instance, for all the settings below
     */
    private final DefaultConfig config;
    /**
     * Set to true when a setting is changed, this will trigger a save when the gui is closed
     */
    private boolean settingsUpdated;
    private int currentID = 0;

    public GeneralSetting(HyperiumGui previous) {
        super("GENERAL", previous);
        this.config = Hyperium.CONFIG;
        this.config.register(this);
    }

    @Override
    protected void pack() {
        super.pack();
        currentID = 0;

        registerOnOffSetting("DISCORD RICH PRESENCE", discordRPEnabled, on -> discordRPEnabled = on);
        registerOnOffSetting("DISCORD DISPLAY SERVER", discordServerDisplayEnabled, on -> discordServerDisplayEnabled = on);
        registerOnOffSetting("FULLBRIGHT", fullbrightEnabled, on -> fullbrightEnabled = on);
        registerOnOffSetting("ROMAN NUMERALS", romanNumeralsEnabled, on -> romanNumeralsEnabled = on);
        registerOnOffSetting("COMPACT CHAT", compactChatEnabled, on -> compactChatEnabled = on);
        registerOnOffSetting("VOID FLICKER FIX", voidflickerfixEnabled, on -> voidflickerfixEnabled = on);
        registerOnOffSetting("SMART FRAMERATE", framerateLimiterEnabled, on -> framerateLimiterEnabled = on);
        registerOnOffSetting("SHINY POTS", shinyPotsEnabled, on -> shinyPotsEnabled = on);
        registerOnOffSetting("BETTER SOUNDS", smartSoundsEnabled, on -> smartSoundsEnabled = on);
        registerOnOffSetting("PING NUMBER", numberPingEnabled, on -> numberPingEnabled = on);
        registerOnOffSetting("COMBAT PARTICLE FIX", combatParticleFixEnabled, on -> combatParticleFixEnabled = on);
        registerOnOffSetting("PERSPECTIVE HOLD", perspectiveHoldDownEnabled, on -> perspectiveHoldDownEnabled = on);

        SelectionItem<String> item = registerCustomSetting(
                "MENU STYLE",
                menuStyle,
                (i) -> menuStyle = (String) ((SelectionItem) i).getSelectedItem()
        );
        Arrays.stream(GuiStyle.values()).forEach(s -> item.addItem(s.toString()));

        registerOnOffSetting("WINDOWED FULLSCREEN", windowedFullScreen, on -> {
            windowedFullScreen = on;

            Minecraft.getMinecraft().toggleFullscreen();
            try {
                // hacky fix to concurrent exception
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Minecraft.getMinecraft().toggleFullscreen();
        });

        registerOnOffSetting("BOSSBAR TEXT ONLY", bossBarTextOnlyEnabled, on -> bossBarTextOnlyEnabled = on);
        registerOnOffSetting("STATIC FOV", staticFovEnabled, on -> staticFovEnabled = on);
        registerOnOffSetting("UPLOAD SCREENSHOTS BY DEFAULT", uploadScreenshotsByDefault, on -> uploadScreenshotsByDefault = on);
        registerOnOffSetting("HIDE SCOREBOARD NUMBERS", hideScoreboardNumbers, on -> hideScoreboardNumbers = on);
        registerOnOffSetting("BLUR GUI BACKGROUNDS", blurGuiBackgroundsEnabled, on -> blurGuiBackgroundsEnabled = on);
        registerOnOffSetting("DISPLAY CHROMA-HUD ON OTHER SERVERS", chromaHudNonHypixelEnabled, on -> chromaHudNonHypixelEnabled = on);
        registerOnOffSetting("TAKE SCREENSHOT ON KILL", screenshotOnKillEnabled, on -> screenshotOnKillEnabled = on);
        registerOnOffSetting("SHOW SPOTIFY CONTROLS", spotifyControlsEnabled, on -> spotifyControlsEnabled = on);
        registerOnOffSetting("HYPIXEL ZOO SOUND EFFECT ON JOIN", hypixelZooEnabled, on -> hypixelZooEnabled = on);
        registerOnOffSetting("FRIENDS FIRST IN TAB", Hyperium.INSTANCE.getHandlers().getConfigOptions().friendsFirstIntag, on -> Hyperium.INSTANCE.getHandlers().getConfigOptions().friendsFirstIntag = on);
        registerOnOffSetting("SHOW HYPERIUM USERS IN TAB", Hyperium.INSTANCE.getHandlers().getConfigOptions().showOnlinePlayers, on -> Hyperium.INSTANCE.getHandlers().getConfigOptions().showOnlinePlayers = on);

    }

    private void registerOnOffSetting(String name, boolean enabled, Consumer<Boolean> callback) {
        OnOffSetting setting = new OnOffSetting(
                currentID,
                getX(),
                getDefaultItemY(currentID),
                this.width - (getX() * 2),
                name
        );
        setting.setEnabled(enabled).setConsumer(
                callback.andThen((on) -> settingsUpdated = true)
        );

        this.settingItems.add(setting);

        currentID++;
    }

    private SelectionItem<String> registerCustomSetting(String name, String selected, Consumer<SettingItem> callback) {
        SelectionItem<String> item = new SelectionItem<>(
                currentID,
                getX(),
                getDefaultItemY(currentID),
                this.width - (getX() * 2),
                name,
                callback.andThen((i) -> {
                    settingsUpdated = true;
                    ((SelectionItem) i).nextItem();
                })
        );
        item.setSelectedItem(selected);

        this.settingItems.add(item);

        currentID++;

        return item;
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
