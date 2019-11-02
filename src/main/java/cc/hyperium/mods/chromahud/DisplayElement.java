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

package cc.hyperium.mods.chromahud;

import cc.hyperium.mods.chromahud.api.Dimension;
import cc.hyperium.mods.chromahud.api.DisplayItem;
import cc.hyperium.utils.JsonHolder;
import com.google.gson.JsonArray;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mitchell Katz on 5/25/2017.
 */
public class DisplayElement extends Dimension {
    private final JsonHolder data;
    private double xloc, yloc;
    private List<DisplayItem> displayItems;
    private double scale;
    private int color;
    private boolean shadow;
    private boolean highlighted;
    private boolean rightSided;
    // Used for rainbow rendering
    private boolean selected;
    private boolean chroma;
    private boolean rgb;
    private boolean color_pallet;
    private boolean static_chroma;

    DisplayElement(JsonHolder object) {
        data = object;
        xloc = object.optDouble("x");
        yloc = object.optDouble("y");
        scale = object.optDouble("scale");
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

        displayItems = items;
        shadow = object.optBoolean("shadow");
        highlighted = object.optBoolean("highlighted");
        color = data.optInt("color");
        chroma = data.optBoolean("chroma");
        rightSided = data.optBoolean("right_side");
        rgb = data.optBoolean("rgb");
        color_pallet = data.optBoolean("color_pallet");
        static_chroma = data.optBoolean("static_chroma");
        recalculateColor();
    }

    public static DisplayElement blank() {
        return new DisplayElement(new JsonHolder().put("x", .5).put("y", .5).put("scale", 1).put("color", Color.WHITE.getRGB()).put("color_pallet", true));
    }

    public boolean isRightSided() {
        return rightSided;
    }

    public void setRightSided(boolean newState) {
        rightSided = newState;
        data.put("right_side", newState);
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
        return chroma;
    }

    public void setChroma(boolean chroma) {
        this.chroma = chroma;
        data.put("chroma", chroma);
    }

    public void recalculateColor() {
        if (chroma) {
            color = 0;
        } else if (rgb) {
            color = new Color(data.optInt("red"), data.optInt("green"), data.optInt("blue")).getRGB();
        }
    }

    public void draw() {
        ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());
        int x = (int) (xloc * resolution.getScaledWidth_double());
        double y = (int) (yloc * resolution.getScaledHeight_double());

        for (DisplayItem iDisplayItem : displayItems) {
            try {
                iDisplayItem.draw(x, y, false);
                y += iDisplayItem.getHeight() * ElementRenderer.getCurrentScale();
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
        data.put("color", color);
    }

    public double getXloc() {
        return xloc;
    }

    public void setXloc(double xloc) {
        data.put("x", xloc);
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
        data.put("y", yloc);
        this.yloc = yloc;
    }

    public List<DisplayItem> getDisplayItems() {
        return displayItems;
    }

    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        data.put("scale", scale);
        this.scale = scale;
    }

    public Dimension getDimensions() {
        return this;
    }

    public void drawForConfig() {
        recalculateColor();
        width = 0;
        height = 0;
        ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());
        double addy = 0;
        int x = (int) (xloc * resolution.getScaledWidth_double());
        double y = (int) (yloc * resolution.getScaledHeight_double());

        for (DisplayItem iDisplayItem : displayItems) {
            iDisplayItem.draw(x, y, true);
            y += iDisplayItem.getHeight() * ElementRenderer.getCurrentScale();
            addy += iDisplayItem.getHeight() * ElementRenderer.getCurrentScale();
            width = (int) Math.max(iDisplayItem.getWidth() * ElementRenderer.getCurrentScale(), width);

        }
        height = addy;

    }

    public void renderEditView() {
        ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());
        int x = (int) (.8 * resolution.getScaledWidth_double());
        if (rightSided) {
            x += getDimensions().getWidth();
            if (x > resolution.getScaledWidth())
                x = resolution.getScaledWidth();
        } else {
            if (x + getDimensions().getWidth() > resolution.getScaledWidth()) {
                x = (int) (resolution.getScaledWidth() - getDimensions().getWidth());
            }
        }

        double y = (int) (.2 * resolution.getScaledHeight_double());

        for (DisplayItem iDisplayItem : displayItems) {
            iDisplayItem.draw(x, y, false);
            y += iDisplayItem.getHeight() * ElementRenderer.getCurrentScale();
        }
    }

    public void adjustOrdinal() {
        int bound = displayItems.size();
        for (int ord = 0; ord < bound; ord++) {
            displayItems.get(ord).setOrdinal(ord);
        }
    }

    public void setRgb(boolean state) {
        rgb = state;
        data.put("rgb", state);
    }

    public boolean isRGB() {
        return rgb;
    }

    public boolean isColorPallet() {
        return color_pallet;
    }

    public void setColorPallet(boolean state) {
        color_pallet = state;
        data.put("color_pallet", state);
    }

    public boolean isStaticChroma() {
        return static_chroma;
    }

    public void setStaticChroma(boolean state) {
        static_chroma = state;
        data.put("static_chroma", state);
    }

    public boolean isShadow() {
        return shadow;
    }

    public void setShadow(boolean shadow) {
        data.put("shadow", shadow);
        this.shadow = shadow;
    }

    public boolean isHighlighted() {
        return highlighted;
    }

    public void setHighlighted(boolean highlighted) {
        data.put("highlighted", highlighted);
        this.highlighted = highlighted;
    }

    public JsonHolder getData() {
        return data;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
