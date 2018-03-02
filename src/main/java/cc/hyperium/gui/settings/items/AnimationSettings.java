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

import cc.hyperium.Hyperium;
import cc.hyperium.config.ConfigOpt;
import cc.hyperium.config.DefaultConfig;
import cc.hyperium.gui.settings.SettingGui;
import cc.hyperium.gui.settings.components.SelectionItem;
import net.minecraft.client.gui.GuiScreen;

public class AnimationSettings extends SettingGui {

    private DefaultConfig config;

    // 1.7 Animations

    @ConfigOpt
    public static boolean oldBlockhit = true;

    @ConfigOpt
    public static boolean oldBow = true;

    @ConfigOpt
    public static boolean oldRod = true;

    @ConfigOpt
    public static boolean oldEat = true;

    @ConfigOpt
    public static boolean redArmour = true;

    private SelectionItem blockhit;
    private SelectionItem bow;
    private SelectionItem rod;
    private SelectionItem eat;
    private SelectionItem redarmour;



    public AnimationSettings(GuiScreen previous) {
        super("ANIMATIONS", previous);
        config = Hyperium.CONFIG;
        config.register(this);
    }

    @Override
    protected void pack() {
        super.pack();
        settingItems.add(blockhit = new SelectionItem(0, getX(), getDefaultItemY(0),  width - getX() * 2, "BLOCKHITTING", i->{
            ((SelectionItem)i).nextItem();
            oldBlockhit = ((SelectionItem) i).getSelectedItem().equals("1.7");
        }));
        blockhit.addItem("1.7");
        blockhit.addItem("1.8");
        blockhit.setSelectedItem(oldBlockhit ? "1.7" : "1.8");

        settingItems.add(bow = new SelectionItem(1, getX(), getDefaultItemY(1),  width - getX() * 2, "BOW", i->{
            ((SelectionItem)i).nextItem();
            oldBow = ((SelectionItem) i).getSelectedItem().equals("1.7");
        }));
        bow.addItem("1.7");
        bow.addItem("1.8");
        bow.setSelectedItem(oldBow ? "1.7" : "1.8");

        settingItems.add(rod = new SelectionItem(2, getX(), getDefaultItemY(2),  width - getX() * 2, "FISHING ROD", i->{
            ((SelectionItem)i).nextItem();
            oldRod = ((SelectionItem) i).getSelectedItem().equals("1.7");
        }));
        rod.addItem("1.7");
        rod.addItem("1.8");
        rod.setSelectedItem(oldRod ? "1.7" : "1.8");

        settingItems.add(eat = new SelectionItem(3, getX(), getDefaultItemY(3),  width - getX() * 2, "EATING", i->{
            ((SelectionItem)i).nextItem();
            oldEat = ((SelectionItem) i).getSelectedItem().equals("1.7");
        }));
        eat.addItem("1.7");
        eat.addItem("1.8");
        eat.setSelectedItem(oldEat ? "1.7" : "1.8");


        settingItems.add(redarmour = new SelectionItem(4, getX(), getDefaultItemY(4),  width - getX() * 2, "ARMOUR", i->{
            ((SelectionItem)i).nextItem();
            redArmour = ((SelectionItem) i).getSelectedItem().equals("1.7");
        }));
        redarmour.addItem("1.7");
        redarmour.addItem("1.8");
        redarmour.setSelectedItem(redArmour ? "1.7" : "1.8");

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
