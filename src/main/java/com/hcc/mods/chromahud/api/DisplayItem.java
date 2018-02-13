package com.hcc.mods.chromahud.api;


import com.hcc.mods.chromahud.ChromaHUDApi;
import com.hcc.utils.JsonHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mitchellkatz on 1/8/18. Designed for production use on Sk1er.club
 */
public abstract class DisplayItem {

    private int ordinal;

    public JsonHolder getData() {
        save();
        return data;
    }
    public void save() {

    }
    protected JsonHolder data;

    public DisplayItem(JsonHolder data, int ordinal) {
        this.data = data;
        this.ordinal = ordinal;

    }

    public String name() {
        return ChromaHUDApi.getInstance().getName(data.optString("type"));
    }

    public int getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(int ordinal) {
        this.ordinal = ordinal;
    }

    public abstract Dimension draw(int x, double y, boolean config);

    public List<ButtonConfig> configOptions() {
        return new ArrayList<>();
    }

    public String getType() {
        return data.optString("type");
    }
}
