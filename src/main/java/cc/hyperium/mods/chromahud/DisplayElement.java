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

package cc.hyperium.mods.chromahud;

import cc.hyperium.mods.chromahud.api.Dimension;
import cc.hyperium.mods.chromahud.api.DisplayItem;
import cc.hyperium.utils.JsonHolder;
import com.google.gson.JsonArray;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;

/**
 * Created by Mitchell Katz on 5/25/2017.
 */
public class DisplayElement extends Dimension {
    private final JsonHolder data;
    private double xloc, yloc;
    private List<DisplayItem> displayItems = new ArrayList<>();
    private double scale = 1;
    private int color;
    private boolean shadow;
    private boolean highlighted;
    private boolean rightSided = false;
    // Used for rainbox rendering
    private boolean selected;
    private boolean chroma;
    private boolean rgb;
    private boolean color_pallet;
    private boolean static_chroma;

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
        double brightness = data.optDouble("brightness");
        this.color = data.optInt("color");
        this.chroma = data.optBoolean("chroma");
        this.rightSided = data.optBoolean("right_side");
        this.rgb = data.optBoolean("rgb");
        this.color_pallet = data.optBoolean("color_pallet");
        this.static_chroma = data.optBoolean("static_chroma");
        recalculateColor();
    }

    public static DisplayElement blank() {
        return new DisplayElement(new JsonHolder().put("x", .5).put("y", .5).put("scale", 1).put("color", Color.WHITE.getRGB()).put("color_pallet", true));
    }

    public boolean isRightSided() {
        return rightSided;
    }

    public void setRightSided(boolean newState) {
        this.rightSided = newState;
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
        return this.chroma;
    }

    public void setChroma(boolean chroma) {
        this.chroma = chroma;
        this.data.put("chroma", chroma);
    }

    public void recalculateColor() {
        if (isChroma()) {
            color = 0;
        } else if (isRGB()) {
            this.color = new Color(data.optInt("red"), data.optInt("green"), data.optInt("blue")).getRGB();
        }
    }

    public void draw() {
        ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());
        int x = (int) (xloc * resolution.getScaledWidth_double());
        double y = (int) (yloc * resolution.getScaledHeight_double());
//        if (this.isHighlighted()) {
//            Gui.drawRect(
//                    x - 2,
//                    (int) y - 2,
//                    (int) (x + getDimensions().getWidth()) + 2,
//                    (int) (y + getDimensions().getHeight()),
//                    new Color(0, 0, 0, 120).getRGB());
//        }

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
        return this;
    }

    public void drawForConfig() {
        recalculateColor();
        this.width = 0;
        this.height = 0;
        ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());
        double addy = 0;
        int x = (int) (xloc * resolution.getScaledWidth_double());
        double y = (int) (yloc * resolution.getScaledHeight_double());
        for (DisplayItem iDisplayItem : displayItems) {
            iDisplayItem.draw(x, y, true);
            y += iDisplayItem.getHeight() * ElementRenderer.getCurrentScale();
            addy += iDisplayItem.getHeight() * ElementRenderer.getCurrentScale();
            this.width = (int) Math.max(iDisplayItem.getWidth() * ElementRenderer.getCurrentScale(), this.width);
        }
        this.height = addy;

    }

    public void renderEditView() {
        ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());
        int x = (int) (.8 * resolution.getScaledWidth_double());
        if (isRightSided()) {
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
        for (int ord = 0; ord < displayItems.size(); ord++) {
            displayItems.get(ord).setOrdinal(ord);
        }
    }

    public void setRgb(boolean state) {
        this.rgb = state;
        this.data.put("rgb", state);
    }

    public boolean isRGB() {
        return rgb;
    }

    public boolean isColorPallet() {
        return color_pallet;
    }

    public void setColorPallet(boolean state) {
        this.color_pallet = state;
        this.data.put("color_pallet", state);
    }

    public boolean isStaticChroma() {
        return static_chroma;
    }

    public void setStaticChroma(boolean state) {
        this.static_chroma = state;
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

    public boolean isSelected() {
        return this.selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
