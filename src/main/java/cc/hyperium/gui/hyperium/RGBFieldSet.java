package cc.hyperium.gui.hyperium;

import cc.hyperium.config.Category;

import java.lang.reflect.Field;

public class RGBFieldSet {
    private Field red;
    private Field green;
    private Field blue;
    private Category category;
    private boolean mods;
    private Object parentObj;

    public Object getParentObj() {
        return parentObj;
    }

    public RGBFieldSet(Field red, Field green, Field blue, Category category, boolean mods, Object parentObj) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.category = category;
        this.mods = mods;
        this.parentObj = parentObj;

    }

    public boolean isMods() {
        return mods;
    }

    public Category getCategory() {
        return category;
    }

    public Field getRed() {
        return red;
    }

    public Field getGreen() {
        return green;
    }

    public Field getBlue() {
        return blue;
    }


}
