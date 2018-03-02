/*
 * Hyperium Client, Free client with huds and popular mod
 *     Copyright (C) 2018  Hyperium Dev Team
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.gui.settings.items;

import cc.hyperium.GuiStyle;
import cc.hyperium.Hyperium;
import cc.hyperium.config.ConfigOpt;
import cc.hyperium.config.DefaultConfig;
import cc.hyperium.gui.settings.SettingGui;
import cc.hyperium.gui.settings.components.SelectionItem;
import cc.hyperium.mixins.gui.MixinGuiMainMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import java.util.Arrays;

@SuppressWarnings("unchecked")
public class GeneralSetting extends SettingGui {
    private DefaultConfig config;

    @ConfigOpt
    public static boolean darkModeEnabled = true;
    @ConfigOpt
    public static boolean numberPingEnabled = false;
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
    public static String menuStyle = GuiStyle.DEFAULT.toString();

    private SelectionItem<String> discordRP;

    private SelectionItem<String> fullBright;

    private SelectionItem<String> romanNumerals;

    private SelectionItem<String> discordServerDisplay;

    private SelectionItem<String> compactChat;

    private SelectionItem<String> voidflickerfix;

    private SelectionItem<String> framerateLimiter;

    private SelectionItem<String> fastChat;
    
    private SelectionItem<String> shinyPots;
    
    private SelectionItem<String> smartSounds;

    private SelectionItem<String> menuStyleSelection;

    private SelectionItem<String> numberPing;

    private SelectionItem<String> darkMode;


    public GeneralSetting(GuiScreen previous) {
        super("GENERAL", previous);
        config = Hyperium.CONFIG;
        config.register(this);
    }

    @Override
    protected void pack() {
        super.pack();

        settingItems.add(discordRP = new SelectionItem(0, getX(), getDefaultItemY(0),  width - getX() * 2, "DISCORD RICH PRESENCE", i->{
            ((SelectionItem)i).nextItem();
            discordRPEnabled = ((SelectionItem) i).getSelectedItem().equals("ON");
        }));
        discordRP.addDefaultOnOff();
        discordRP.setSelectedItem(discordRPEnabled ? "ON" : "OFF");

        settingItems.add(discordServerDisplay = new SelectionItem<>(1, getX(), getDefaultItemY(1),  width - getX() * 2, "DISCORD DISPLAY SERVER", i->{
            ((SelectionItem<String>)i).nextItem();
            discordServerDisplayEnabled = ((SelectionItem) i).getSelectedItem().equals("ON");
        }));
        discordServerDisplay.addDefaultOnOff();
        discordServerDisplay.setSelectedItem(discordServerDisplayEnabled ? "ON" : "OFF");

        settingItems.add(fullBright = new SelectionItem(2, getX(), getDefaultItemY(2),  width - getX() * 2, "FULLBRIGHT", i->{
            ((SelectionItem)i).nextItem();
            fullbrightEnabled = ((SelectionItem) i).getSelectedItem().equals("ON");
        }));
        fullBright.addDefaultOnOff();
        fullBright.setSelectedItem(fullbrightEnabled ? "ON" : "OFF");

        settingItems.add(romanNumerals = new SelectionItem(3, getX(), getDefaultItemY(3),  width - getX() * 2, "ROMAN NUMERALS", i->{
            ((SelectionItem)i).nextItem();
            romanNumeralsEnabled = ((SelectionItem) i).getSelectedItem().equals("ON");
        }));
        romanNumerals.addDefaultOnOff();
        romanNumerals.setSelectedItem(romanNumeralsEnabled ? "ON" : "OFF");

        settingItems.add(compactChat = new SelectionItem(4, getX(), getDefaultItemY(4),  width - getX() * 2, "COMPACT CHAT", i->{
            ((SelectionItem)i).nextItem();
            compactChatEnabled = ((SelectionItem) i).getSelectedItem().equals("ON");
        }));
        compactChat.addDefaultOnOff();
        compactChat.setSelectedItem(compactChatEnabled ? "ON" : "OFF");

        settingItems.add(voidflickerfix = new SelectionItem(5, getX(), getDefaultItemY(5),  width - getX() * 2, "VOID FLICKER FIX", i->{
            ((SelectionItem)i).nextItem();
            voidflickerfixEnabled = ((SelectionItem) i).getSelectedItem().equals("ON");
        }));
        voidflickerfix.addDefaultOnOff();
        voidflickerfix.setSelectedItem(voidflickerfixEnabled ? "ON" : "OFF");

        settingItems.add(framerateLimiter = new SelectionItem(6, getX(), getDefaultItemY(6),  width - getX() * 2, "SMART FRAMERATE", i->{
            ((SelectionItem)i).nextItem();
            framerateLimiterEnabled = ((SelectionItem) i).getSelectedItem().equals("ON");
        }));
        framerateLimiter.addDefaultOnOff();
        framerateLimiter.setSelectedItem(framerateLimiterEnabled ? "ON" : "OFF");

        settingItems.add(fastChat = new SelectionItem(7, getX(), getDefaultItemY(7),  width - getX() * 2, "FASTCHAT", i->{
            ((SelectionItem)i).nextItem();
            fastchatEnabled = ((SelectionItem) i).getSelectedItem().equals("ON");
        }));
        fastChat.addDefaultOnOff();
        fastChat.setSelectedItem(fastchatEnabled ? "ON" : "OFF");
    
        settingItems.add(shinyPots = new SelectionItem(8, getX(), getDefaultItemY(8),  width - getX() * 2, "SHINY POTS", i-> {
            ((SelectionItem)i).nextItem();
            shinyPotsEnabled = ((SelectionItem) i).getSelectedItem().equals("ON");
        }));
        shinyPots.addDefaultOnOff();
        shinyPots.setSelectedItem(shinyPotsEnabled ? "ON" : "OFF");
    
        settingItems.add(
            smartSounds = new SelectionItem(9, getX(), getDefaultItemY(9),  width - getX() * 2, "BETTER SOUNDS", i-> {
            ((SelectionItem)i).nextItem();
            smartSoundsEnabled = ((SelectionItem) i).getSelectedItem().equals("ON");
        }));
        smartSounds.addDefaultOnOff();
        smartSounds.setSelectedItem(smartSoundsEnabled ? "ON" : "OFF");
        
        settingItems.add(menuStyleSelection = new SelectionItem(10, getX(), getDefaultItemY(10),  width - getX() * 2, "MENU STYLE", i->{
            ((SelectionItem)i).nextItem();
            menuStyle = (String)((SelectionItem)i).getSelectedItem();
        }));
        Arrays.stream(GuiStyle.values()).forEach(s -> menuStyleSelection.addItem(s.toString()));
        menuStyleSelection.setSelectedItem(menuStyle);

        settingItems.add(numberPing = new SelectionItem(11, getX(), getDefaultItemY(11),  width - getX() * 2, "SHOW NUMBER PING", i->{
            ((SelectionItem)i).nextItem();
            numberPingEnabled = ((SelectionItem) i).getSelectedItem().equals("OFF");
        }));
        numberPing.addDefaultOnOff();
        numberPing.setSelectedItem(numberPingEnabled ? "ON" : "OFF");

        settingItems.add(darkMode = new SelectionItem(12, getX(), getDefaultItemY(12),  width - getX() * 2, "DARK MODE", i->{
            ((SelectionItem)i).nextItem();
            darkModeEnabled = ((SelectionItem) i).getSelectedItem().equals("ON");
        }));
        darkMode.addDefaultOnOff();
        darkMode.setSelectedItem(darkModeEnabled ? "ON" : "OFF");
    }

    private int getDefaultItemY(int i) {
        return getY()+25 + i * 15;
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        config.save();
    }
}
