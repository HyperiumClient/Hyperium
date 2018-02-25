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
import net.minecraft.client.gui.GuiScreen;

import java.util.Arrays;

public class GeneralSetting extends SettingGui {
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
    public static String menuStyle = GuiStyle.DEFAULT.toString();

    private SelectionItem discordRP;

    private SelectionItem fullBright;

    private SelectionItem romanNumerals;

    private SelectionItem discordServerDisplay;

    private SelectionItem compactChat;

    private SelectionItem voidflickerfix;

    private SelectionItem framerateLimiter;

    private SelectionItem fastChat;

    private SelectionItem menuStyleSelection;


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

        settingItems.add(discordServerDisplay = new SelectionItem(1, getX(), getDefaultItemY(1),  width - getX() * 2, "DISCORD DISPLAY SERVER", i->{
            ((SelectionItem)i).nextItem();
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
        settingItems.add(menuStyleSelection = new SelectionItem(8, getX(), getDefaultItemY(8),  width - getX() * 2, "MENU STYLE", i->{
            ((SelectionItem)i).nextItem();
            menuStyle = ((SelectionItem) i).getSelectedItem();
        }));
        Arrays.stream(GuiStyle.values()).forEach(s -> menuStyleSelection.addItem(s.toString()));
        menuStyleSelection.setSelectedItem(menuStyle);
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
