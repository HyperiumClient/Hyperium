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

package cc.hyperium.mods.chromahud.displayitems.hyperium;

import cc.hyperium.mods.chromahud.ElementRenderer;
import cc.hyperium.mods.chromahud.api.DisplayItem;
import cc.hyperium.mods.common.ToggleSprintContainer;
import cc.hyperium.utils.JsonHolder;
import net.minecraft.util.StringUtils;

public class ToggleSprintStatus extends DisplayItem {
    private String sprintEnabledText;

    public ToggleSprintStatus(JsonHolder data, int ordinal) {
        super(data, ordinal);
        sprintEnabledText = data.optString("sprintEnabledText");
        if (StringUtils.isNullOrEmpty(sprintEnabledText)) sprintEnabledText = "ToggleSprint Enabled";
        height = 10;
    }

    @Override
    public void draw(int x, double y, boolean config) {
        if (ToggleSprintContainer.toggleSprintActive) {
            width = ElementRenderer.getFontRenderer().getStringWidth(sprintEnabledText);
            ElementRenderer.draw(x, y, sprintEnabledText);
        }
    }

    public String getStatusText() {
        return sprintEnabledText;
    }

    public void setSprintEnabledText(String text) {
        sprintEnabledText = text;
        data.put("sprintEnabledText", text);
    }
}
