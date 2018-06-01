package cc.hyperium.gui.main;

import cc.hyperium.gui.main.components.OverlayComponent;
import net.minecraft.client.gui.Gui;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/*
 * Created by Cubxity on 01/06/2018
 */
public class HyperiumOverlay {
    private List<OverlayComponent> components = new ArrayList<>();
    private int offsetY = 0;

    public void render(int mouseX, int mouseY, int w, int h) {
        Gui.drawRect(0, 0, w, h, new Color(0, 0, 0, 100).getRGB()); // bg
        Gui.drawRect(w / 6 * 2, h / 4, w / 6 * 4, h / 4 * 3, new Color(30, 30, 30).getRGB());
        components.forEach(c -> c.render(mouseX, mouseY, w / 6 * 2, h / 4 + 20 * components.indexOf(c) + offsetY, w / 6 * 2, 20));
    }

    public void handleMouseInput() {
        components.forEach(OverlayComponent::handleMouseInput);
    }
}
