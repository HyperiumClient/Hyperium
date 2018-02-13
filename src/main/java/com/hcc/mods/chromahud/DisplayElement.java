package com.hcc.mods.chromahud;

import com.google.gson.JsonArray;
import com.hcc.mods.chromahud.api.Dimension;
import com.hcc.mods.chromahud.api.DisplayItem;
import com.hcc.utils.JsonHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mitchell Katz on 5/25/2017.
 */
public class DisplayElement {
    private double xloc, yloc;
    private List<DisplayItem> displayItems = new ArrayList<>();
    private double scale = 1;
    private int color;
    private double prevX, prevY;
    private boolean shadow;
    private boolean highlighted;
    private JsonHolder data;
    private double brightness;

    public DisplayElement(JsonHolder object) {
        this.data = object;
        xloc = object.optDouble("x");
        this.yloc = object.optDouble("y");
        this.scale = object.optDouble("scale");
        List<DisplayItem> items = new ArrayList<>();
        JsonArray itemss = object.optJSONArray("items");
        int ord = 0;
        for (int i1 = 0; i1 < itemss.size(); i1++) {
            JsonHolder item = new JsonHolder(itemss.get(i1).getAsJsonObject());
            DisplayItem type = ChromaHUDApi.getInstance().parse(item.optString("type"), ord, item);
            if (type != null) {
                items.add(type);
                ord++;
            }
        }
        this.displayItems = items;
        this.shadow = object.optBoolean("shadow");
        this.highlighted = object.optBoolean("highlighted");
        this.brightness = data.optDouble("brightness");
        this.color = data.optInt("color");
        recalculateColor();

    }

    public static DisplayElement blank() {
        return new DisplayElement(new JsonHolder().put("x", .5).put("y", .5).put("scale", 1).put("color", Color.WHITE.getRGB()).put("color_pallet", true));
    }

    public double getBrightness() {
        return data.optDouble("brightness");
    }

    public void setBrightness(double brightness) {
        this.data.put("brightness", brightness);
    }

    @Override
    public String toString() {
        return "DisplayElement{" +
                "xloc=" + xloc +
                ", yloc=" + yloc +
                ", displayItems=" + displayItems +
                ", scale=" + scale +
                ", color=" + color +
                '}';
    }

    public boolean isChroma() {
        return data.optBoolean("chroma");
    }

    public void setChroma(boolean chroma) {
        this.data.put("chroma", chroma);
    }

    public void recalculateColor() {
        if (isChroma()) {
            color = 0;
        } else if (isRGB()) {
            this.color = new Color(data.optInt("red"), data.optInt("green"), data.optInt("blue")).getRGB();
        }
    }

    public void reformatColor() {
        if (isChroma()) {

        }
    }

    public void draw() {
        ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());
        int x = (int) (xloc * resolution.getScaledWidth_double());
        double y = (int) (yloc * resolution.getScaledHeight_double());
        for (DisplayItem iDisplayItem : displayItems) {
            try {
                Dimension d = iDisplayItem.draw(x, y, false);
                y += d.getHeight() * ElementRenderer.getCurrentScale();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
        this.data.put("color", color);
    }

    public double getXloc() {
        return xloc;
    }

    public void setXloc(double xloc) {
        this.data.put("x", xloc);
        this.xloc = xloc;
    }

    public void removeDisplayItem(int ordinal) {
        displayItems.remove(ordinal);
        adjustOrdinal();
    }

    public double getYloc() {
        return yloc;
    }

    public void setYloc(double yloc) {
        this.data.put("y", yloc);
        this.yloc = yloc;
    }

    public List<DisplayItem> getDisplayItems() {
        return displayItems;
    }

    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        this.data.put("scale", scale);
        this.scale = scale;
    }

    public Dimension getDimensions() {
        return new Dimension((int) prevX, (int) prevY);
    }

    public void drawForConfig() {
        recalculateColor();
        this.prevX = 0;
        this.prevY = 0;
        ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());
        double addy = 0;
        int x = (int) (xloc * resolution.getScaledWidth_double());
        double y = (int) (yloc * resolution.getScaledHeight_double());
        for (DisplayItem iDisplayItem : displayItems) {
            Dimension d = iDisplayItem.draw(x, y, true);
            y += d.getHeight() * ElementRenderer.getCurrentScale();
            addy += d.getHeight() * ElementRenderer.getCurrentScale();
            prevX = (int) Math.max(d.getWidth() * ElementRenderer.getCurrentScale(), prevX);
        }
        this.prevY = addy;

    }

    public void renderEditView() {
        ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());
        int x = (int) (.8 * resolution.getScaledWidth_double());
        double y = (int) (.2 * resolution.getScaledHeight_double());
        for (DisplayItem iDisplayItem : displayItems) {
            Dimension d = iDisplayItem.draw(x, y, false);
            y += d.getHeight() * ElementRenderer.getCurrentScale();
        }

    }

    public void adjustOrdinal() {
        for (int ord = 0; ord < displayItems.size(); ord++) {
            displayItems.get(ord).setOrdinal(ord);
        }
    }

    public void setRgb(boolean state) {
        this.data.put("rgb", state);
    }

    public boolean isRGB() {
        return data.optBoolean("rgb");
    }

    public boolean isColorPallet() {
        return data.optBoolean("color_pallet");
    }


    public void setColorPallet(boolean state) {
        this.data.put("color_pallet", state);
    }

    public boolean isStaticChroma() {
        return data.optBoolean("static_chroma");
    }

    public void setStaticChroma(boolean state) {
        this.data.put("static_chroma", state);
    }

    public boolean isShadow() {
        return shadow;
    }

    public void setShadow(boolean shadow) {
        this.data.put("shadow", shadow);
        this.shadow = shadow;
    }

    public boolean isHighlighted() {
        return highlighted;
    }

    public void setHighlighted(boolean highlighted) {
        this.data.put("highlighted", highlighted);
        this.highlighted = highlighted;
    }


    public JsonHolder getData() {
        return data;
    }
}
