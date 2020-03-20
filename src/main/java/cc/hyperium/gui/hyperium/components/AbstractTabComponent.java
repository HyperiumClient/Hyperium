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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

/*
 * Created by Cubxity on 27/08/2018
 */
public abstract class AbstractTabComponent {

  public boolean hover;
  protected List<String> tags = new ArrayList<>();
  protected AbstractTab tab;
  private boolean filterCache; // search query cache
  private String searchCache = "";  // filter cache
  private final List<Consumer<Object>> stateChanges = new ArrayList<>();
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
    if (hover) {
      Gui.drawRect(x, y, x + width, y + 18, 0xa0000000);
    }
    GlStateManager.popMatrix();
  }

  public boolean filter(String string) {
    String filteredString = string.toLowerCase(Locale.ENGLISH);
    boolean access;

    if (string.equals(searchCache)) {
      access = filterCache;
    } else {
      boolean cache = false;

      for (String tag : tags) {
        if (tag.contains(filteredString)) {
          cache = true;
          break;
        }
      }

      access = filterCache = cache;
    }

    searchCache = string;
    return access;
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
    for (Consumer<Object> tmp : stateChanges) {
      tmp.accept(o);
    }
  }

  public void mouseEvent(int x, int y) {

  }

  public AbstractTabComponent tag(String... ts) {
    List<String> list = new ArrayList<>();

    for (String s : ts) {
      String toLowerCase = s.toLowerCase(Locale.ENGLISH);
      list.add(toLowerCase);
    }

    tags.addAll(list);
    return this;
  }

  public AbstractTabComponent tag(List<String> ts) {
    List<String> list = new ArrayList<>();

    for (String s : ts) {
      String toLowerCase = s.toLowerCase(Locale.ENGLISH);
      list.add(toLowerCase);
    }

    tags.addAll(list);
    return this;
  }
}
