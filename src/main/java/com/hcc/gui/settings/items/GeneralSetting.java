/*
 *     Hypixel Community Client, Client optimized for Hypixel Network
 *     Copyright (C) 2018  HCC Dev Team
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

package com.hcc.gui.settings.items;

import com.hcc.HCC;
import com.hcc.config.ConfigOpt;
import com.hcc.config.DefaultConfig;
import com.hcc.gui.settings.SettingGui;
import com.hcc.gui.settings.components.SelectionItem;
import net.minecraft.client.gui.GuiScreen;

public class GeneralSetting extends SettingGui {
    private DefaultConfig config;

    @ConfigOpt
    private boolean discordRPEnabled = true;

    private SelectionItem discordRP;
    public GeneralSetting(GuiScreen previous) {
        super("GENERAL", previous);
        config = HCC.CONFIG;
        config.loadToClass(this);
    }

    @Override
    protected void pack() {
        super.pack();
        settingItems.add(discordRP = new SelectionItem(0, getX(), getDefaultItemY(0),  width - getX() * 2, "DISCORD RICH PRESENCE", i->{
            ((SelectionItem)i).nextItem();
            discordRPEnabled = ((SelectionItem) i).getSelectedItem().equals("ON");
            config.saveToJsonFromRamObject(this);
        }));
        discordRP.addDefaultOnOff();
        discordRP.setSelectedItem(discordRPEnabled ? "ON" : "OFF");
    }

    private int getDefaultItemY(int i) {
        return getY()+25 + i * 15;
    }

}
