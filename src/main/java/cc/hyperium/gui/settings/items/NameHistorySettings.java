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

import cc.hyperium.Hyperium;
import cc.hyperium.config.DefaultConfig;
import cc.hyperium.gui.HyperiumGui;
import cc.hyperium.gui.settings.SettingGui;

public class NameHistorySettings extends SettingGui {
    /*
    @ConfigOpt
    public static boolean rgbNamesEnabled = false;
    */
    private final DefaultConfig config;

    public NameHistorySettings(HyperiumGui previous) {
        super("NAME HISTORY", previous);
        config = Hyperium.CONFIG;
        config.register(this);
    }

    @Override
    protected void pack() {
        super.pack();
        /*
        SelectionItem<String> rgbNames;
        settingItems.add(rgbNames = new SelectionItem<>(0, getX(), getDefaultItemY(0), width - getX() * 2, "RGB NAMES", i -> {
            ((SelectionItem) i).nextItem();
            rgbNamesEnabled = ((SelectionItem) i).getSelectedItem().equals("ON");
        }));
        rgbNames.addDefaultOnOff();
        rgbNames.setSelectedItem(rgbNamesEnabled ? "ON" : "OFF");
        */
    }

    private int getDefaultItemY(int i) {
        return getY() + 25 + i * 15;
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        config.save();
    }
}
