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

package cc.hyperium.gui.hyperium.components;

import cc.hyperium.gui.Icons;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/*
 * Created by Cubxity on 27/08/2018
 */
public class CollapsibleTabComponent extends AbstractTabComponent {

    private List<AbstractTabComponent> children = new ArrayList<>();
    private boolean collapsed = true;
    private String label;
    private CollapsibleTabComponent parent;
    private String tmpf;

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

    public void setCollapsed(boolean collapsed) {
        this.collapsed = collapsed;
    }

    public List<AbstractTabComponent> getChildren() {
        return children;
    }

    @Override
    public void render(int x, int y, int width, int mouseX, int mouseY) {
        super.render(x, y, width, mouseX, mouseY);

        tab.gui.getFont().drawString(label.replaceAll("_", " ").toUpperCase(), x + 3, y + 5, 0xffffff);

        GlStateManager.bindTexture(0);

        if (collapsed) {
            Icons.ARROW_UP_ALT.bind();
        } else {
            Icons.ARROW_DOWN_ALT.bind();
        }

        Gui.drawScaledCustomSizeModalRect(x + width - 20, y, 0, 0, 144, 144, 20, 20, 144, 144);

        if (collapsed) return;

        y += 18;
        x += 10;
        width -= 10;

        boolean right = false; // left right column stuff
        int prevH = 0;

        for (AbstractTabComponent comp : tmpf == null ? children : children.stream().filter(c -> c.filter(tmpf)).collect(Collectors.toList())) {
            if (parent != null) right = false;

            int x1 = right ? x + width / 2 : x;
            comp.render(x1, y, parent != null ? width : width / 2, mouseX, mouseY);

            if (mouseX >= (x1) && mouseX <= (x1) + (parent != null ? width : width / 2) && mouseY >= y && mouseY <= y + comp.getHeight() - 2) {
                comp.hover = true;
                comp.mouseEvent(right ? mouseX - width / 2 - x : mouseX - x, mouseY - y /* Make the Y relevant to the component */);

                if (Mouse.isButtonDown(0)) {
                    if (!tab.clickStates.computeIfAbsent(comp, ignored -> false)) {
                        comp.onClick(right ? mouseX - width / 2 : mouseX,
                            mouseY - y /* Make the Y relevant to the component */);
                        tab.clickStates.put(comp, true);
                    }
                } else if (tab.clickStates.computeIfAbsent(comp, ignored -> false)) {
                    tab.clickStates.put(comp, false);
                }
            } else {
                comp.hover = false;
            }

            boolean b = right || parent != null;
            if (b) y += Math.max(comp.getHeight(), prevH);
            right = !right;
            prevH = comp.getHeight();
        }
    }

    @Override
    public int getHeight() {
        if (collapsed) {
            return 18;
        } else {
            List<AbstractTabComponent> children = tmpf == null ? this.children : this.children.stream().filter(c -> c.filter(tmpf)).collect(Collectors.toList());
            if (parent != null) {
                int h = 18;
                h += children.stream().mapToInt(AbstractTabComponent::getHeight).sum();
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
                } else {
                    leftHeight = height;
                }
            }
            compH += leftHeight;
            tmpf = null;
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
        if (y < 18) {
            collapsed = !collapsed;
        }
    }

    @Override
    public boolean filter(String s) {
        boolean b = super.filter(s);
        if (b)
            tmpf = s;
        return b;
    }

    public void sortSelf() {
        children.sort(Comparator.comparing(this::getLabel));
    }

    private String getLabel(AbstractTabComponent component) {
        if (component instanceof CollapsibleTabComponent) return ((CollapsibleTabComponent) component).label;
        if (component instanceof SliderComponent) return ((SliderComponent) component).getLabel();
        if (component instanceof LabelComponent) return ((LabelComponent) component).getLabel();
        if (component instanceof ToggleComponent) return ((ToggleComponent) component).getLabel();
        if (component instanceof SelectorComponent) return ((SelectorComponent) component).getLabel();
        return "ZZZ";
    }
}
