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

package cc.hyperium.mods.levelhead.renderer;

import cc.hyperium.mods.levelhead.util.LevelheadJsonHolder;

import java.util.UUID;

public class LevelheadTag {

    private LevelheadComponent header;
    private LevelheadComponent footer;

    private UUID owner;

    public LevelheadTag(UUID owner) {
        this.owner = owner;
    }

    public void construct(LevelheadJsonHolder holder) {
        if (header == null) header = build(holder, true);
        if (footer == null) footer = build(holder, false);
    }

    public void reapply(LevelheadTag holder) {
        if (!header.isCustom()) header = holder.header;
        if (!footer.isCustom()) footer = holder.footer;
    }

    private LevelheadComponent build(LevelheadJsonHolder holder, boolean isHeader) {
        String seek = isHeader ? "header" : "footer";
        LevelheadJsonHolder json = holder.optJsonObject(seek);

        LevelheadComponent component = new LevelheadComponent(json.optString(seek, "Big problem, report to Sk1er."));
        boolean custom = json.optBoolean("custom");

        component.setCustom(custom);

        if (custom && isHeader && !holder.optBoolean("exclude")) {
            component.setValue(component.getValue() + ": ");
        }

        if (json.optBoolean("chroma")) {
            component.setChroma(true);
        } else if (json.optBoolean("rgb")) {
            component.setRgb(true);
            component.setAlpha(json.optInt("alpha"));
            component.setRed(json.optInt("red"));
            component.setGreen(json.optInt("green"));
            component.setBlue(json.optInt("blue"));
        } else {
            component.setColor(json.optString("color"));
        }

        return component;
    }

    public LevelheadComponent getHeader() {
        return header;
    }

    public LevelheadComponent getFooter() {
        return footer;
    }

    public UUID getOwner() {
        return owner;
    }

    public String getString() {
        return header.getValue() + footer.getValue();
    }

    public boolean isCustom() {
        return header.isCustom() || footer.isCustom();
    }
}
