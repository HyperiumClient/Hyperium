/*
 *     Copyright (C) 2018  Hyperium <https://hyperium.cc/>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.mods.levelhead.renderer;


import cc.hyperium.utils.JsonHolder;

/**
 * @author Sk1er
 */
public class LevelheadTag {
    private LevelheadComponent header;
    private LevelheadComponent footer;

    public LevelheadTag() {
    }


    public LevelheadComponent getHeader() {
        return header;
    }

    public LevelheadComponent getFooter() {
        return footer;
    }


    public void construct(JsonHolder holder) {
        if (header == null) {
            this.header = build(holder, true);
        }
        if (footer == null) {
            this.footer = build(holder, false);
        }
    }

    public void reApply(LevelheadTag holder) {
        if (!this.header.isCustom()) {
            this.header = holder.header;
        }
        if (!this.footer.isCustom()) {
            this.footer = holder.footer;

        }

    }

    private LevelheadComponent build(JsonHolder holder, boolean isHeader) {
        String seek = isHeader ? "header" : "footer";
        JsonHolder json = holder.optJSONObject(seek);

        LevelheadComponent component = new LevelheadComponent(json.optString(seek, "UMM BIG ERROR REPORT TO SK1ER"));
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
            component.setBlue(json.optInt("blue"));
            component.setGreen(json.optInt("green"));
        } else {
            component.setColor(json.optString("color"));
        }

        return component;
    }

    public String getString() {
        return header.getValue() + footer.getValue();
    }

    public boolean isCustom() {
        return footer.isCustom() || header.isCustom();
    }
}
