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

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/*
 * Created by Cubxity on 27/08/2018
 */
public abstract class AbstractTabComponent {

    public boolean hover;
    protected List<String> tags = new ArrayList<>();
    protected AbstractTab tab;
    private boolean fc; // search query cache
    private String sc = "";  // filter cache
    private List<Consumer<Object>> stateChanges = new ArrayList<>();
    private boolean enabled = true;

    /**
     * @param tab  the tab that this component will be added on
     * @param tags tags that are used for search function
     */
    public AbstractTabComponent(AbstractTab tab, List<String> tags) {
        this.tab = tab;
        tag(tags); // prevent unsupported operation on AbstractList
    }

    public int getHeight() {
        return (tab.getFilter() != null && !filter(tab.getFilter())) ? 0 : 18;
    }

    public void render(int x, int y, int width, int mouseX, int mouseY) {
        GlStateManager.pushMatrix();
        if (hover) Gui.drawRect(x, y, x + width, y + 18, 0xa0000000);
        GlStateManager.popMatrix();
    }

    public boolean filter(String s) {
        String fs = s.toLowerCase();
        boolean a = (s.equals(sc) ? fc : (fc = tags.stream().anyMatch(t -> t.contains(fs))));
        sc = s;
        return a;
    }

    public void onClick(int x, int y) {

    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void registerStateChange(Consumer<Object> objectConsumer) {
        stateChanges.add(objectConsumer);
    }

    protected void stateChange(Object o) {
        stateChanges.forEach(tmp -> tmp.accept(o));
    }

    public void mouseEvent(int x, int y) {

    }

    public AbstractTabComponent tag(String... ts) {
        tags.addAll(Arrays.stream(ts).map(String::toLowerCase).collect(Collectors.toList()));
        return this;
    }

    public AbstractTabComponent tag(List<String> ts) {
        tags.addAll(ts.stream().map(String::toLowerCase).collect(Collectors.toList()));
        return this;
    }
}
