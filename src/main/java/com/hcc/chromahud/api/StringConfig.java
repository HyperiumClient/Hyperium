package com.hcc.chromahud.api;

import java.util.function.Consumer;

public class StringConfig {

    private String string;
    private Consumer<DisplayItem> load;
    private Consumer<DisplayItem> draw;
    public StringConfig(String string, Consumer<DisplayItem> load, Consumer<DisplayItem> draw) {
        this.string = string;
        this.load = load;
        this.draw = draw;
    }

    public StringConfig(String string) {
        this.string = string;
        load = (displayItem) -> {
        };
        draw = (displayItem) -> {
        };


    }

    public Consumer<DisplayItem> getLoad() {
        return load;
    }

    public Consumer<DisplayItem> getDraw() {
        return draw;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }
}
