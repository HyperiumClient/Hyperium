/*
 *  Hypixel Community Client, Client optimized for Hypixel Network
 *     Copyright (C) 2018  Hyperium Dev Team
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.gui.settings.components;

import cc.hyperium.gui.settings.SettingItem;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public class SelectionItem<T> extends SettingItem {

    private List<T> items = new ArrayList<>();
    private T selectedItem = null;

    public SelectionItem(int id, int x, int y, int width, String displayString, Consumer<SettingItem> callback) {
        super(id, x, y, width, displayString, callback);
    }

    public void setSelectedItem(T selectedItem) {
        this.selectedItem = selectedItem;
    }

    public T getSelectedItem() {
        return selectedItem;
    }

    public void addItem(T item){
        items.add(item);
    }

    public void addItems(Collection<T> item){
        items.addAll(item);
    }

    public void addDefaultOnOff(){
        ((List<String>) items).add("ON");
        ((List<String>) items).add("OFF");
    }

    public void nextItem(){
        if(items.isEmpty())
            return;
        if(!items.contains(selectedItem)) {
            selectedItem = items.get(0);
            return;
        }
        int i = items.indexOf(selectedItem);
        if(i == items.size() - 1)
            i =0;
        else i++;
        selectedItem = items.get(i);
    }

    @Override
    public void drawItem(Minecraft mc, int mouseX, int mouseY, int x, int y) {
        this.xPosition = x;
        this.yPosition = y;
        if (this.visible) {
            this.hovered = mouseX >= x && mouseY >= y && mouseX < this.xPosition + this.width && mouseY < y + this.height;
            this.mouseDragged(mc, mouseX, mouseY);

            int j = textColor;

            if (this.hovered) {
                drawRect(x, y,
                        x + this.width, y + this.height,
                        color.darker().darker().darker().darker().getRGB());
                // professional
            } else {
                drawRect(x, y,
                        x + this.width, y + this.height,
                        color.getRGB());
            }

            if (!this.enabled) {
                j = 10526880;
            } else if (this.hovered) {
                j = textHoverColor;
            }

            fontRenderer.drawString(this.displayString, x + 4, y + (this.height - 8) / 2, j);
            fontRenderer.drawString(String.valueOf(selectedItem), x+width - (fontRenderer.getWidth(String.valueOf(selectedItem)) + 10), y + (this.height - 8) / 2, j);
        }
    }
}
