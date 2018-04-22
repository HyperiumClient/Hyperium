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

import java.util.Arrays;

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

    // Dab Mod
    @ConfigOpt
    public static int dabSpeed = 7;

    @ConfigOpt
    public static boolean dabToggle = false;

    // Floss dance
    @ConfigOpt
    public static int flossDanceSpeed = 4;

    @ConfigOpt
    public static boolean flossDanceToggle = false;

    private SelectionItem<String> blockhit;
    private SelectionItem<String> bow;
    private SelectionItem<String> rod;
    private SelectionItem<String> eat;
    private SelectionItem<String> redarmour;
    private SelectionItem<Integer> dabspeed;
    private SelectionItem<String> toggledab;
    private SelectionItem<Integer> flossdancespeed;
    private SelectionItem<String> toggleflossdance;

    public AnimationSettings(HyperiumGui previous) {
        super("ANIMATIONS", previous);
        config = Hyperium.CONFIG;
        config.register(this);
    }

    @Override
    protected void pack() {
        super.pack();
        settingItems.add(blockhit = new SelectionItem<>(0, getX(), getDefaultItemY(0),  width - getX() * 2, "BLOCKHITTING", i->{
            ((SelectionItem)i).nextItem();
            oldBlockhit = ((SelectionItem) i).getSelectedItem().equals("1.7");
        }));
        blockhit.addItem("1.7");
        blockhit.addItem("1.8");
        blockhit.setSelectedItem(oldBlockhit ? "1.7" : "1.8");

        settingItems.add(bow = new SelectionItem<>(1, getX(), getDefaultItemY(1),  width - getX() * 2, "BOW", i->{
            ((SelectionItem)i).nextItem();
            oldBow = ((SelectionItem) i).getSelectedItem().equals("1.7");
        }));
        bow.addItem("1.7");
        bow.addItem("1.8");
        bow.setSelectedItem(oldBow ? "1.7" : "1.8");

        settingItems.add(rod = new SelectionItem<>(2, getX(), getDefaultItemY(2),  width - getX() * 2, "FISHING ROD", i->{
            ((SelectionItem)i).nextItem();
            oldRod = ((SelectionItem) i).getSelectedItem().equals("1.7");
        }));
        rod.addItem("1.7");
        rod.addItem("1.8");
        rod.setSelectedItem(oldRod ? "1.7" : "1.8");

        settingItems.add(eat = new SelectionItem<>(3, getX(), getDefaultItemY(3),  width - getX() * 2, "EATING", i->{
            ((SelectionItem)i).nextItem();
            oldEat = ((SelectionItem) i).getSelectedItem().equals("1.7");
        }));
        eat.addItem("1.7");
        eat.addItem("1.8");
        eat.setSelectedItem(oldEat ? "1.7" : "1.8");


        settingItems.add(redarmour = new SelectionItem<>(4, getX(), getDefaultItemY(4),  width - getX() * 2, "ARMOUR", i->{
            ((SelectionItem)i).nextItem();
            redArmour = ((SelectionItem) i).getSelectedItem().equals("1.7");
        }));
        redarmour.addItem("1.7");
        redarmour.addItem("1.8");
        redarmour.setSelectedItem(redArmour ? "1.7" : "1.8");

        settingItems.add(dabspeed = new SelectionItem<>(5, getX(), getDefaultItemY(5), width - getX() * 2, "DAB SPEED", i -> {
            ((SelectionItem) i).nextItem();
            dabSpeed = 15 - (((SelectionItem<Integer>) i).getSelectedItem() * 2);
        }));
        dabspeed.addItems(Arrays.asList(1, 2, 3, 4, 5));
        dabspeed.setSelectedItem(
                dabSpeed == 13 ? 1 : dabSpeed == 11 ? 2 : dabSpeed == 9 ? 3 : dabSpeed == 7 ? 4 : 5
        );

        settingItems.add(toggledab = new SelectionItem<>(6, getX(), getDefaultItemY(6), width - getX() * 2, "TOGGLE DAB", i -> {
            ((SelectionItem) i).nextItem();
            dabToggle = ((SelectionItem) i).getSelectedItem().equals("ON");
        }));
        toggledab.addItems(Arrays.asList("ON", "OFF"));
        toggledab.setSelectedItem(dabToggle ? "ON" : "OFF");

        settingItems.add(flossdancespeed = new SelectionItem<>(7, getX(), getDefaultItemY(5), width - getX() * 2, "FLOSS DANCE SPEED", i -> {
            ((SelectionItem) i).nextItem();
            flossDanceSpeed = ((SelectionItem<Integer>) i).getSelectedItem();
        }));
        flossdancespeed.addItems(Arrays.asList(1, 2, 3, 4, 5));
        flossdancespeed.setSelectedItem(flossDanceSpeed);

        settingItems.add(toggleflossdance = new SelectionItem<>(8, getX(), getDefaultItemY(6), width - getX() * 2, "TOGGLE FLOSS DANCE", i -> {
            ((SelectionItem) i).nextItem();
            flossDanceToggle = ((SelectionItem) i).getSelectedItem().equals("ON");
        }));
        toggleflossdance.addItems(Arrays.asList("ON", "OFF"));
        toggleflossdance.setSelectedItem(dabToggle ? "ON" : "OFF");

        SelectionItem<Object> flip = new SelectionItem<>(9, getX(), getDefaultItemY(7), width - getX() * 2, "SHOW FLIP EVERYWHERE", i -> {
            ((SelectionItem) i).nextItem();
            Hyperium.INSTANCE.getHandlers().getConfigOptions().showFlipEverywhere = ((SelectionItem) i).getSelectedItem().equals("YES");
        });
        settingItems.add(flip);
        toggleflossdance.addItems(Arrays.asList("YES", "NO"));
        toggleflossdance.setSelectedItem(  Hyperium.INSTANCE.getHandlers().getConfigOptions().showFlipEverywhere  ? "YES" : "NO");


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
