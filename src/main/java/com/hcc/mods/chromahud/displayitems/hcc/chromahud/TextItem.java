package com.hcc.mods.chromahud.displayitems.hcc.chromahud;


import com.hcc.mods.chromahud.ElementRenderer;
import com.hcc.mods.chromahud.api.Dimension;
import com.hcc.mods.chromahud.api.DisplayItem;
import com.hcc.utils.JsonHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mitchell Katz on 5/29/2017.
 */
public class TextItem extends DisplayItem {

    private String text;


    public TextItem(JsonHolder object, int ordinal) {
        super(object, ordinal);
        text = object.optString("text");
    }


    public Dimension draw(int x, double y, boolean isConfig) {
        List<String> list = new ArrayList<>();
        if (text.isEmpty())
            list.add("Text is empty??");
        else list.add(text);
        ElementRenderer.draw(x, y, list);
        return new Dimension(ElementRenderer.maxWidth(list), 10);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        data.put("text", text);
    }
}
