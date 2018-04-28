package cc.hyperium.gui.settings.components;

import java.util.function.Consumer;

public class OnOffSetting extends SelectionItem<String> {
    private Consumer<Boolean> consumer;

    public OnOffSetting(int id, int x, int y, int width, String name) {
        super(id, x, y, width, name, null);

        this.callback = settingItem -> {
            OnOffSetting thiz = (OnOffSetting) settingItem;

            thiz.nextItem();

            if (OnOffSetting.this.consumer != null) {
                OnOffSetting.this.consumer.accept(isOn());
            }

        };

        items.add("ON");
        items.add("OFF");
    }

    public OnOffSetting setEnabled(boolean on) {
        setSelectedItem(on ? "ON" : "OFF");
        return this;
    }

    public OnOffSetting setConsumer(Consumer<Boolean> consumer) {
        this.consumer = consumer;
        return this;
    }

    public boolean isOn() {
        return "ON".equals(selectedItem);
    }
}
