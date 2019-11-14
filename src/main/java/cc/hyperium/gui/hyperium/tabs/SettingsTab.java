/*
 *       Copyright (C) 2018-present Hyperium <https://hyperium.cc/>
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU Lesser General Public License as published
 *       by the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU Lesser General Public License for more details.
 *
 *       You should have received a copy of the GNU Lesser General Public License
 *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.gui.hyperium.tabs;

import cc.hyperium.Hyperium;
import cc.hyperium.addons.customcrosshair.gui.GuiCustomCrosshairEditCrosshair;
import cc.hyperium.addons.sidebar.gui.screen.GuiScreenSettings;
import cc.hyperium.config.*;
import cc.hyperium.gui.CapesGui;
import cc.hyperium.gui.hyperium.HyperiumMainGui;
import cc.hyperium.gui.hyperium.RGBFieldSet;
import cc.hyperium.gui.hyperium.components.*;
import cc.hyperium.gui.keybinds.GuiKeybinds;
import cc.hyperium.mods.keystrokes.screen.GuiScreenKeystrokes;
import cc.hyperium.mods.togglechat.gui.ToggleChatMainGui;
import net.minecraft.client.resources.I18n;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

/*
 * Created by Cubxity on 30/08/2018
 */
public class SettingsTab extends AbstractTab {

    public SettingsTab(HyperiumMainGui gui) {
        super(gui, "tab.settings.name");

        //TODO maybe readd separate thing for addons

        HashMap<Category, CollapsibleTabComponent> items = new HashMap<>();
        for (Object o : gui.getSettingsObjects()) {
            for (Field f : o.getClass().getDeclaredFields()) {
                ToggleSetting ts = f.getAnnotation(ToggleSetting.class);
                SelectorSetting ss = f.getAnnotation(SelectorSetting.class);
                SliderSetting sliderSetting = f.getAnnotation(SliderSetting.class);
                List<Consumer<Object>> objectConsumer = gui.getCallbacks().get(f);
                AbstractTabComponent tabComponent = null;
                Category category = null;
                boolean mods = false;
                if (ts != null) {
                    tabComponent = new ToggleComponent(this, Collections.emptyList(), I18n.format(ts.name()), f, o);
                    category = ts.category();
                    mods = ts.mods();
                } else if (ss != null) {
                    Supplier<String[]> supplier = gui.getCustomStates().getOrDefault(f, ss::items);
                    tabComponent = new SelectorComponent(this, Collections.emptyList(), I18n.format(ss.name()), f, o, supplier);
                    category = ss.category();
                    mods = ss.mods();
                } else if (sliderSetting != null) {
                    tabComponent = new SliderComponent(this, Collections.emptyList(), I18n.format(sliderSetting.name()), f, o, sliderSetting.min(),
                        sliderSetting.max(), sliderSetting.isInt(), sliderSetting.round());
                    category = sliderSetting.category();
                    mods = sliderSetting.mods();
                }
                if (category == null) continue;
                apply(tabComponent, mods, category, items);
                if (objectConsumer != null) {
                    for (Consumer<Object> consumer : objectConsumer) {
                        tabComponent.registerStateChange(consumer);
                    }
                }
            }
        }

        // Link to capes GUI.
        apply(new LinkComponent(this, Collections.emptyList(), "Youtuber Capes", new CapesGui()), false, Category.COSMETICS, items);

        // Link to sidebar mod's gui
        apply(new LinkComponent(this, Collections.emptyList(), "Sidebar Mod",
            new GuiScreenSettings(Hyperium.INSTANCE.getInternalAddons().getSidebarAddon())), true, Category.OTHER, items);

        // Link to keystrokes gui
        apply(new LinkComponent(this, Collections.emptyList(), "Keystrokes",
            new GuiScreenKeystrokes(Hyperium.INSTANCE.getModIntegration().getKeystrokesMod())), true, Category.OTHER, items);

        // Link to togglechat gui
        apply(new LinkComponent(this, Collections.emptyList(), "Togglechat",
            new ToggleChatMainGui(Hyperium.INSTANCE.getModIntegration().getToggleChat(), 0)), true, Category.OTHER, items);

        // Link to keybinds gui
        apply(new LinkComponent(this, Collections.emptyList(), "Keybinds",
            new GuiKeybinds()), false, Category.GENERAL, items);

        // Link to custom crosshair gui
        apply(new LinkComponent(this, Collections.emptyList(), "Custom Crosshair",
            new GuiCustomCrosshairEditCrosshair(Hyperium.INSTANCE.getInternalAddons().getCustomCrosshairAddon())), true, Category.OTHER, items);

        apply(new ButtonComponent(this, new ArrayList<>(), "Reset Modifiers", () -> {
            Settings.BOW_FOV_MODIFIER = 1;
            Settings.SPRINTING_FOV_MODIFIER = 1;
            Settings.SLOWNESS_FOV_MODIFIER = 1;
            Settings.SPEED_FOV_MODIFIER = 1;
        }), true, Category.FOV_MODIFIER, items);

        for (RGBFieldSet field : gui.getRgbFields()) {
            apply(new RGBComponent(this, field), field.isMods(), field.getCategory(), items);
        }

        Collection<CollapsibleTabComponent> values = items.values();
        List<CollapsibleTabComponent> c = new ArrayList<>(values);

        for (CollapsibleTabComponent value : values) {
            value.sortSelf();
        }

        c.sort(Comparator.comparing(CollapsibleTabComponent::getLabel));
        components.addAll(c);
    }

    private void apply(AbstractTabComponent component, boolean mods, Category category, HashMap<Category, CollapsibleTabComponent> items) {

        CollapsibleTabComponent collapsibleTabComponent = items.computeIfAbsent(
            (mods ? Category.MODS : category),
            category1 ->
                new CollapsibleTabComponent(SettingsTab.this,
                    Collections.singletonList(category1.name()),
                    category1.getDisplay()));
        if (mods) {
            boolean b = false;
            for (AbstractTabComponent abs : collapsibleTabComponent.getChildren()) {
                if (((CollapsibleTabComponent) abs).getLabel().equalsIgnoreCase(category.getDisplay())) {
                    ((CollapsibleTabComponent) abs).addChild(component);
                    b = true;
                }
            }

            if (!b) {
                CollapsibleTabComponent c = new CollapsibleTabComponent(this, Collections.emptyList(), category.getDisplay());
                collapsibleTabComponent.addChild(c);
                c.addChild(component);
                c.setParent(collapsibleTabComponent);
            }
        } else {
            collapsibleTabComponent.addChild(component);
        }
    }
}
