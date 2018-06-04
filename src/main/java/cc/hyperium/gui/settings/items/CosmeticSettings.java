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

public class CosmeticSettings extends SettingGui {

    // Dab Mod
    /*
    @ConfigOpt
    public static int dabSpeed = 7;

    // 1.7 Animations
    @ConfigOpt
    public static boolean dabToggle = false;
    // Floss dance
    @ConfigOpt
    public static int flossDanceSpeed = 4;
    @ConfigOpt
    public static boolean flossDanceToggle = false;
    @ConfigOpt
    public static int flip_type = 1;
*/


    private final DefaultConfig config;

    public CosmeticSettings(HyperiumGui previous) {
        super("COSMETICS", previous);
        config = Hyperium.CONFIG;
        config.register(this);
    }

    @Override
    protected void pack() {
        super.pack();
        /*
        SelectionItem<String> showEars;
        settingItems.add(showEars = new SelectionItem<>(0, getX(), getDefaultItemY(0), width - getX() * 2, "SHOW EARS", i -> {
            ((SelectionItem) i).nextItem();
            if (Hyperium.INSTANCE.getCosmetics().getDeadmau5Cosmetic().isSelfUnlocked()) {
                boolean yes = ((SelectionItem) i).getSelectedItem().equals("YES");
                Hyperium.INSTANCE.getHandlers().getConfigOptions().enableDeadmau5Ears = yes;
                NettyClient.getClient().write(ServerCrossDataPacket.build(new JsonHolder().put("internal", true).put("ears", yes)));
            }
        }));
        if (Hyperium.INSTANCE.getCosmetics().getDeadmau5Cosmetic().isSelfUnlocked()) {
            showEars.addItem("YES");
            showEars.addItem("NO");
            showEars.setSelectedItem(Hyperium.INSTANCE.getHandlers().getConfigOptions().enableDeadmau5Ears ? "YES" : "NO");
        } else {
            showEars.addItem("NOT PURCHASED");
            showEars.setSelectedItem("NOT PURCHASED");
        }
        SelectionItem<Integer> dabspeed;
        settingItems.add(dabspeed = new SelectionItem<>(1, getX(), getDefaultItemY(1), width - getX() * 2, "DAB SPEED", i -> {
            ((SelectionItem) i).nextItem();
            dabSpeed = 15 - (((SelectionItem<Integer>) i).getSelectedItem() * 2);
        }));
        dabspeed.addItems(Arrays.asList(1, 2, 3, 4, 5));
        dabspeed.setSelectedItem(
                dabSpeed == 13 ? 1 : dabSpeed == 11 ? 2 : dabSpeed == 9 ? 3 : dabSpeed == 7 ? 4 : 5
        );

        SelectionItem<String> toggledab;
        settingItems.add(toggledab = new SelectionItem<>(2, getX(), getDefaultItemY(2), width - getX() * 2, "TOGGLE DAB", i -> {
            ((SelectionItem) i).nextItem();
            dabToggle = ((SelectionItem) i).getSelectedItem().equals("ON");
        }));
        toggledab.addItems(Arrays.asList("ON", "OFF"));
        toggledab.setSelectedItem(dabToggle ? "ON" : "OFF");

        SelectionItem<Integer> flossdancespeed;
        settingItems.add(flossdancespeed = new SelectionItem<>(3, getX(), getDefaultItemY(3), width - getX() * 2, "FLOSS DANCE SPEED", i -> {
            ((SelectionItem) i).nextItem();
            flossDanceSpeed = ((SelectionItem<Integer>) i).getSelectedItem();
        }));
        flossdancespeed.addItems(Arrays.asList(1, 2, 3, 4, 5));
        flossdancespeed.setSelectedItem(flossDanceSpeed);

        SelectionItem<String> toggleflossdance;
        settingItems.add(toggleflossdance = new SelectionItem<>(4, getX(), getDefaultItemY(4), width - getX() * 2, "TOGGLE FLOSS DANCE", i -> {
            ((SelectionItem) i).nextItem();
            flossDanceToggle = ((SelectionItem) i).getSelectedItem().equals("ON");
        }));
        toggleflossdance.addItems(Arrays.asList("ON", "OFF"));
        toggleflossdance.setSelectedItem(flossDanceToggle ? "ON" : "OFF");

        SelectionItem<Object> showCosmeticsEveryWhere = new SelectionItem<>(5, getX(), getDefaultItemY(5), width - getX() * 2, "SHOW COSMETICS EVERYWHERE", i -> {
            ((SelectionItem) i).nextItem();
            Hyperium.INSTANCE.getHandlers().getConfigOptions().showCosmeticsEveryWhere = ((SelectionItem) i).getSelectedItem().equals("YES");
        });
        settingItems.add(showCosmeticsEveryWhere);
        showCosmeticsEveryWhere.addItems(Arrays.asList("YES", "NO"));
        showCosmeticsEveryWhere.setSelectedItem(Hyperium.INSTANCE.getHandlers().getConfigOptions().showCosmeticsEveryWhere ? "YES" : "NO");


        SelectionItem<String> flip_state = new SelectionItem<>(6, getX(), getDefaultItemY(6), width - getX() * 2, "INVERT TYPE", i -> {
            ((SelectionItem) i).nextItem();
            flip_type = ((SelectionItem) i).getSelectedItem().equals("ROTATE") ? 2 : 1;
        });
        settingItems.add(flip_state);
        flip_state.addItems(Arrays.asList("INVERT", "ROTATE"));
        flip_state.setSelectedItem(flip_type == 1 ? "INVERT" : "ROTATE");
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
