package cc.hyperium.gui.main;

import cc.hyperium.config.Settings;
import cc.hyperium.gui.HyperiumGui;
import cc.hyperium.gui.main.components.OverlayComponent;
import cc.hyperium.gui.main.components.OverlayToggle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/*
 * Created by Cubxity on 01/06/2018
 */
public class HyperiumOverlay {
    private List<OverlayComponent> components = new ArrayList<>();
    private int offsetY = 0;

    public void render(int mouseX, int mouseY, int w, int h) {
        HyperiumGui.drawChromaBox(0, 0, w, h, 0.2F); // bg
        Gui.drawRect(w / 6 * 2, h / 4, w / 6 * 4, h / 4 * 3, new Color(30, 30, 30).getRGB());
        components.forEach(c -> c.render(mouseX, mouseY, w / 6 * 2, h / 4 + 20 * components.indexOf(c) + offsetY, w / 6 * 2, 20));
    }

    public void handleMouseInput() {
        final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        int sw = sr.getScaledWidth();
        int sh = sr.getScaledHeight();
        final int mx = Mouse.getX() * sw / Minecraft.getMinecraft().displayWidth;
        final int my = sh - Mouse.getY() * sh / Minecraft.getMinecraft().displayHeight - 1;
        components.forEach(c -> c.handleMouseInput(mx, my, sr.getScaledWidth() / 6 * 2, sr.getScaledHeight() / 4 + 20 * components.indexOf(c) + offsetY, sr.getScaledWidth() / 6 * 2, 20));
        int i = Mouse.getEventDWheel();
        if (i > 0)
            offsetY += 5;
        else if (i < 0)
            offsetY -= 5;
    }

    public List<OverlayComponent> getComponents() {
        return components;
    }

    public void addToggle(String label, Field f) {
        try {
            components.add(new OverlayToggle(label, f.getBoolean(null), b -> {
                try {
                    f.setBoolean(null, b);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                Settings.save();
            }));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
