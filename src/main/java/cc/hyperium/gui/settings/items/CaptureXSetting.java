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
import cc.hyperium.config.ConfigOpt;
import cc.hyperium.config.DefaultConfig;
import cc.hyperium.gui.HyperiumGui;
import cc.hyperium.gui.settings.SettingGui;
import cc.hyperium.gui.settings.components.SelectionItem;
import cc.hyperium.mods.capturex.CaptureMode;

import java.util.Arrays;

@SuppressWarnings("unchecked")
public class CaptureXSetting extends SettingGui {
    private DefaultConfig config;

    @ConfigOpt
    private static String modeStr = "OFF";

    public static CaptureMode mode = CaptureMode.OFF;

    @ConfigOpt
    public static int captureLength = 1;

    private SelectionItem<CaptureMode> modeSelection;

    private SelectionItem<Integer> lengthSelection;

    public CaptureXSetting(HyperiumGui previous) {
        super("CAPTUREX", previous);
        config = Hyperium.CONFIG;
        config.register(this);
        mode = CaptureMode.valueOf(modeStr);
    }

    @Override
    protected void pack() {
        super.pack();
        settingItems.add(modeSelection = new SelectionItem<>(0, getX(), getDefaultItemY(0),  width - getX() * 2, "CAPTURE MODE", i -> {
            ((SelectionItem)i).nextItem();
            mode = ((SelectionItem<CaptureMode>) i).getSelectedItem();
            modeStr = mode.toString();
        }));
        modeSelection.addItems(Arrays.asList(CaptureMode.values()));
        modeSelection.setSelectedItem(mode);

        settingItems.add(lengthSelection = new SelectionItem<>(1, getX(), getDefaultItemY(1), width - getX() * 2, "CAPTURE LENGTH", i -> {
            ((SelectionItem)i).nextItem();
            captureLength = ((SelectionItem<Integer>)i).getSelectedItem();
        }));
        lengthSelection.addItems(Arrays.asList(1, 3, 5, 8, 10));
        lengthSelection.setSelectedItem(captureLength);
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
