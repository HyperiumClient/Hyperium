package cc.hyperium.gui.hyperium.components;

import cc.hyperium.gui.Icons;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/*
 * Created by Cubxity on 27/08/2018
 */
public class CollapsibleTabComponent extends AbstractTabComponent {
    private List<AbstractTabComponent> childs = new ArrayList<>();
    private boolean collapsed = true;
    private String label;

    public CollapsibleTabComponent(AbstractTab tab, List<String> tags, String label) {
        super(tab, tags);
        this.label = label;
    }

    @Override
    public void render(int x, int y, int width, int mouseX, int mouseY) {
        super.render(x, y, width, mouseX, mouseY);
        tab.gui.getFont().drawString(label.toUpperCase(), x + 3, y + 4, 0xffffff);
        GlStateManager.bindTexture(0);
        if (collapsed)
            Icons.ARROW_RIGHT.bind();
        else
            Icons.ARROW_DOWN.bind();
        Gui.drawScaledCustomSizeModalRect(x + width - 20, y, 0, 0, 144, 144, 20, 20, 144, 144);

        if (collapsed) return;
        y += 20;
        x += 10;
        width -= 10;
        boolean r = false; // left right column stuff
        for (AbstractTabComponent comp : childs) {
            comp.render(r ? x + width / 2 : x, y, width / 2, mouseX, mouseY);

            if (mouseX >= (r ? x + width / 2 : x) && mouseX <= (r ? x + width / 2 : x) + width / 2 && mouseY >= y && mouseY <= y + comp.getHeight()) {
                comp.hover = true;
                if (Mouse.isButtonDown(0)) {
                    if (!tab.clickStates.computeIfAbsent(comp, ignored -> false)) {
                        comp.onClick(r ? mouseX - width / 2 : mouseX, mouseY - y /* Make the Y relevant to the component */);
                        tab.clickStates.put(comp, true);
                    }
                } else if (tab.clickStates.computeIfAbsent(comp, ignored -> false))
                    tab.clickStates.put(comp, false);
            } else
                comp.hover = false;

            if (r)
                y += comp.getHeight();
            r = !r;
        }
    }

    @Override
    public int getHeight() {
        if (collapsed)
            return 20;
        else
            return 20 + childs.stream().flatMapToInt(c -> IntStream.of(c.getHeight())).sum();
    }

    public CollapsibleTabComponent addChild(AbstractTabComponent component) {
        childs.add(component);
        tags.addAll(component.tags);
        return this;
    }

    @Override
    public void onClick(int x, int y) {
        if (y < 20)
            collapsed = !collapsed;
    }
}
