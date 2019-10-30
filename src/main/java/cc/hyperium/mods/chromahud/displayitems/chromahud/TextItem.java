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

package cc.hyperium.mods.chromahud.displayitems.chromahud;

import cc.hyperium.mods.chromahud.ElementRenderer;
import cc.hyperium.mods.chromahud.api.DisplayItem;
import cc.hyperium.utils.ChatColor;
import cc.hyperium.utils.JsonHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mitchell Katz on 5/29/2017.
 */
public class TextItem extends DisplayItem {

    private String text;


    public TextItem(JsonHolder object, int ordinal) {
        super(object, ordinal);
        text = object.optString("text");
        height = 10;
    }


    public void draw(int x, double y, boolean isConfig) {
        List<String> list = new ArrayList<>();
        if (text.isEmpty()) list.add("Text is empty??");
        else list.add(text);
        ElementRenderer.draw(x, y, ChatColor.translateAlternateColorCodes('%', text));
        width = ElementRenderer.maxWidth(list);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        data.put("text", text);
    }
}
