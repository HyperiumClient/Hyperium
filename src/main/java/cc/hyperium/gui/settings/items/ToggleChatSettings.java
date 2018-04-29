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
import cc.hyperium.gui.ModConfigGui;
import cc.hyperium.gui.settings.SettingGui;
import cc.hyperium.gui.settings.components.SelectionItem;
import cc.hyperium.mods.togglechat.ToggleChatMod;
import cc.hyperium.mods.togglechat.toggles.ToggleBase;

/**
 * Hyperium implementation of the ToggleChat settings gui
 *
 * @author boomboompower
 */
public class ToggleChatSettings extends SettingGui {

    private final ToggleChatMod mod;

    private boolean settingsUpdated = false;

    public ToggleChatSettings(ModConfigGui previous) {
        super("TOGGLECHAT", previous);

        this.mod = (ToggleChatMod) Hyperium.INSTANCE.getModIntegration().getToggleChat();
    }

    @Override
    protected void pack() {
        super.pack();

        // Id counter
        int pos = 0;

        for (ToggleBase base : this.mod.getToggleHandler().getToggles().values()) {
            SelectionItem<String> selectionItem = new SelectionItem<>(pos, getX(), getDefaultItemY(pos++), this.width - getX() * 2, base.getName(), i -> {
                // Move to the next item
                ((SelectionItem) i).nextItem();

                // Set the base enabled or disabled
                base.setEnabled(((SelectionItem) i).getSelectedItem().equals("ON"));

                // Trigger our settings changed flag
                this.settingsUpdated = true;
            });
            selectionItem.addDefaultOnOff();
            selectionItem.setSelectedItem(base.isEnabled() ? "ON" : "OFF");

            // Profit
            this.settingItems.add(selectionItem);
        }
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
        if (this.settingsUpdated) {
            this.mod.getConfigLoader().saveToggles();
        }
    }
}

