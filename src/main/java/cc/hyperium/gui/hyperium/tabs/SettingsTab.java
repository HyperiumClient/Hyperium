package cc.hyperium.gui.hyperium.tabs;

import cc.hyperium.config.*;
import cc.hyperium.gui.hyperium.HyperiumMainGui;
import cc.hyperium.gui.hyperium.components.*;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Supplier;

/*
 * Created by Cubxity on 30/08/2018
 */
public class SettingsTab extends AbstractTab {
    private HashMap<Field, Supplier<String[]>> customStates = new HashMap<>();
    private HashMap<Field, Consumer<Object>> callbacks = new HashMap<>();

    public SettingsTab(HyperiumMainGui gui) {
        super(gui, "Settings");

        //TODO add other settings objects

        //TODO maybe readd separate thing for mods
        //TODO enabled / disabled status
        HashMap<Category, CollapsibleTabComponent> items = new HashMap<>();
        for (Field f : Settings.class.getDeclaredFields()) {
            ToggleSetting ts = f.getAnnotation(ToggleSetting.class);
            SelectorSetting ss = f.getAnnotation(SelectorSetting.class);
            SliderSetting sliderSetting = f.getAnnotation(SliderSetting.class);
            Consumer<Object> objectConsumer = callbacks.get(f);
            AbstractTabComponent tabComponent;
            if (ts != null) {
                //TODO tags, prob from annotation
                tabComponent = new ToggleComponent(this, Collections.emptyList(), ts.name(), f, null);
                items.computeIfAbsent(ts.category(), category -> new CollapsibleTabComponent(this, Arrays.asList(category.name()), category.name())).addChild(
                        tabComponent);
            } else if (ss != null) {
                Supplier<String[]> supplier = customStates.getOrDefault(f, ss::items);
                tabComponent = new SelectorComponent(this, Collections.emptyList(), ss.name(), f, null, supplier);
                items.computeIfAbsent(ss.category(), category -> new CollapsibleTabComponent(this, Arrays.asList(category.name()), category.name())).addChild(
                        tabComponent);
            } else if (sliderSetting != null) {
//
                tabComponent = new SliderComponent(this, Collections.emptyList(), sliderSetting.name(), f, null, sliderSetting.min(), sliderSetting.max(), sliderSetting.isInt(), sliderSetting.round());
                items.computeIfAbsent(sliderSetting.category(), category -> new CollapsibleTabComponent(this, Arrays.asList(category.name()), category.name())).addChild(
                        tabComponent);
//                try {
//                    Double value = Double.valueOf(f.get(o).toString());
//                    getCategory(sliderSetting.category()).getComponents().add(new OverlaySlider(sliderSetting.name(), sliderSetting.min(), sliderSetting.max(),
//                            value.floatValue(), aFloat -> {
//                        if (objectConsumer != null)
//                            objectConsumer.accept(aFloat);
//                        try {
//                            f.set(o, aFloat);
//                        } catch (IllegalAccessException e) {
//                            e.printStackTrace();
//                        }
//                    }, sliderSetting.round(), sliderSetting.enabled()));
//
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                }
            }
        }
        components.addAll(items.values());
//        try {
//            components.add(new CollapsibleTabComponent(this, Collections.emptyList(), "General")
//                    .addChild(new CollapsibleTabComponent(this, Collections.emptyList(), "Test child"))
//                    .addChild(new LabelComponent(this, Collections.emptyList(), "Label 1"))
//                    .addChild(new ToggleComponent(this, Collections.emptyList(), "Really really long label that will help me test and debug the wrapping when the GUI is too narrow and it would otherwise overflow", Settings.class.getField("FAST_CONTAINER"), null))
//                    .addChild(new SelectorComponent(this, Collections.emptyList(), "Field name 4", Settings.class.getDeclaredField("PARTICLE_MODE"), null, () -> new String[]{
//                            "OFF",
//                            "PLAIN 1",
//                            "PLAIN 2",
//                            "CHROMA 1",
//                            "CHROMA 2"
//                    })));
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        }
//        components.add(new CollapsibleTabComponent(this, Collections.emptyList(), "General 2")
//                .addChild(new CollapsibleTabComponent(this, Collections.emptyList(), "Test child")));
    }
}
