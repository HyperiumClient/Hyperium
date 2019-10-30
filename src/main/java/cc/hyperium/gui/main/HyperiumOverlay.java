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

package cc.hyperium.gui.main;

import cc.hyperium.gui.main.components.OverlayComponent;
import cc.hyperium.gui.main.components.OverlayToggle;
import cc.hyperium.mods.sk1ercommon.ResolutionUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Consumer;

/*
 * Created by Cubxity on 01/06/2018
 */
public class HyperiumOverlay {
    private List<OverlayComponent> components;
    private int offsetY;
    private String name;

    public HyperiumOverlay(String name) {
        this(name, true);
    }

    public HyperiumOverlay() {
        this("");
    }

    public HyperiumOverlay(String name, boolean alphabetic) {
        this.name = name;

        components = new ListCompatibilityHack(alphabetic);
    }

    public String getName() {
        return name;
    }

    public void render(int mouseX, int mouseY, int w, int h) {
        // HyperiumGui.drawChromaBox(0, 0, w, h, 0.2F); // bg
        Gui.drawRect(w / 6 * 2, h / 4, w / 6 * 4, h / 4 * 3, new Color(30, 30, 30).getRGB());

        Integer[] counter = new Integer[]{0};

        components.forEach(c -> c.render(mouseX, mouseY, w / 6 * 2, h / 4 + 20 * counter[0]++ + offsetY, w / 6 * 2, 20, h));
    }

    public void handleMouseInput() {
        ScaledResolution sr = ResolutionUtil.current();
        int sw = sr.getScaledWidth();
        int sh = sr.getScaledHeight();
        int mx = Mouse.getX() * sw / Minecraft.getMinecraft().displayWidth;
        int my = sh - Mouse.getY() * sh / Minecraft.getMinecraft().displayHeight - 1;

        Integer[] counter = new Integer[]{0};

        components.forEach(c -> c.handleMouseInput(mx, my, sr.getScaledWidth() / 6 * 2,
            sr.getScaledHeight() / 4 + 20 * counter[0]++ + offsetY, sr.getScaledWidth() / 6 * 2, 20));

        int i = Mouse.getEventDWheel();
        if (i > 0 && offsetY != 0) offsetY += 5;
        else if (i < 0) offsetY -= 5;
    }

    public void mouseClicked() {
        ScaledResolution sr = ResolutionUtil.current();
        int sw = sr.getScaledWidth();
        int sh = sr.getScaledHeight();
        int mx = Mouse.getX() * sw / Minecraft.getMinecraft().displayWidth;
        int my = sh - Mouse.getY() * sh / Minecraft.getMinecraft().displayHeight - 1;

        Integer[] counter = new Integer[]{0};

        components.forEach(c -> c.mouseClicked(mx, my, sr.getScaledWidth() / 6 * 2,
            sr.getScaledHeight() / 4 + 20 * counter[0]++ + offsetY, sr.getScaledWidth() / 6 * 2, 20));
    }

    public List<OverlayComponent> getComponents() {
        return components;
    }

    public void addToggle(String label, Field f, Consumer<Object> objectConsumer) {
        addToggle(label, f, objectConsumer, true);
    }

    public void addToggle(String label, Field f, Consumer<Object> objectConsumer, boolean enabled) {
        addToggle(label, f, objectConsumer, enabled, null);
    }

    public void addToggle(String label, Field f, Consumer<Object> objectConsumer, boolean enabled, Object object) {
        try {
            Object o = f.get(object);
            if (o == null) return;

            if (o instanceof Boolean) {
                components.add(new OverlayToggle(label, (boolean) o, b -> {
                    if (objectConsumer != null)
                        objectConsumer.accept(b);
                    try {
                        f.setBoolean(object, b);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }, enabled));
            }
        } catch (IllegalAccessException | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public void reset() {
        components.forEach(c -> {
            if (c instanceof OverlayToggle) ((OverlayToggle) c).resetStep();
        });
    }
}
