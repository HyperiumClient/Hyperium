package cc.hyperium.gui.hyperium.components;

import cc.hyperium.gui.Icons;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/*
 * Created by Cubxity on 27/08/2018
 */
public class CollapsibleTabComponent extends AbstractTabComponent {
    private List<AbstractTabComponent> children = new ArrayList<>();
    private boolean collapsed = true;
    private String label;
    private CollapsibleTabComponent parent;

    public CollapsibleTabComponent(AbstractTab tab, List<String> tags, String label) {
        super(tab, tags);
        this.label = label;
    }

    public CollapsibleTabComponent getParent() {
        return parent;
    }

    public void setParent(CollapsibleTabComponent parent) {
        this.parent = parent;
    }

    public String getLabel() {
        return label;
    }

    public boolean isCollapsed() {

        return collapsed;
    }

    public List<AbstractTabComponent> getChildren() {
        return children;
    }

    @Override
    public void render(int x, int y, int width, int mouseX, int mouseY) {
        super.render(x, y, width, mouseX, mouseY);
        tab.gui.getFont().drawString(label.toUpperCase(), x + 3, y + 5, 0xffffff);
        GlStateManager.bindTexture(0);
        if (collapsed)
            Icons.ARROW_RIGHT.bind();
        else
            Icons.ARROW_DOWN.bind();
        Gui.drawScaledCustomSizeModalRect(x + width - 20, y, 0, 0, 144, 144, 20, 20, 144, 144);

        if (collapsed) return;
        y += 18;
        x += 10;
        width -= 10;
        boolean r = false; // left right column stuff
        int prevH = 0;
        for (AbstractTabComponent comp : children) {

            if (parent != null)
                r = false;
            comp.render(r ? x + width / 2 : x, y, parent != null ? width : width / 2, mouseX, mouseY);

            if (mouseX >= (r ? x + width / 2 : x) && mouseX <= (r ? x + width / 2 : x) + (parent !=null ? width :width / 2) && mouseY >= y && mouseY <= y + comp.getHeight()) {
                comp.hover = true;
                comp.mouseEvent(r ? mouseX - width / 2 - x : mouseX - x, mouseY - y /* Make the Y relevant to the component */);

                if (Mouse.isButtonDown(0)) {
                    if (!tab.clickStates.computeIfAbsent(comp, ignored -> false)) {
                        comp.onClick(r ? mouseX - width / 2 : mouseX, mouseY - y /* Make the Y relevant to the component */);
                        tab.clickStates.put(comp, true);
                    }
                } else if (tab.clickStates.computeIfAbsent(comp, ignored -> false))
                    tab.clickStates.put(comp, false);
            } else
                comp.hover = false;

            boolean b = r || parent != null;
            if (b) {
                y += Math.max(comp.getHeight(), prevH);
                prevH = 0;
            }
            r = !r;
//            if (b)
            prevH = comp.getHeight();
        }
    }

    @Override
    public int getHeight() {
        if (collapsed)
            return 18;
        else {
            if (parent != null) {
                int h = 18;
                for (AbstractTabComponent child : children) {
                    h += child.getHeight();
                }
                return h;
            }


            Iterator<AbstractTabComponent> iterator = children.iterator();
            boolean right = true;
            int leftHeight = 0;
            int compH = 18;
            while (iterator.hasNext()) {
                right = !right;
                AbstractTabComponent next = iterator.next();
                int height = next.getHeight();
                if (right) {
                    compH += Math.max(leftHeight, height);
                    leftHeight = 0;
                } else leftHeight = height;
            }
            compH+=leftHeight;
            return compH;
        }
    }

    public CollapsibleTabComponent addChild(AbstractTabComponent component) {
        children.add(component);
        tags.addAll(component.tags);
        return this;
    }

    @Override
    public void onClick(int x, int y) {
        if (y < 18)
            collapsed = !collapsed;
    }


}
