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

package cc.hyperium.mods.levelhead.config;

import cc.hyperium.config.ConfigOpt;

public class MasterConfig {

    @ConfigOpt private boolean enabled = true;
    @ConfigOpt private double fontSize = 1.0;
    @ConfigOpt private double offset;

    public boolean isEnabled() {
        return enabled;
    }
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    public int getRenderDistance() {
        return 64;
    }
    public int getPurgeSize() {
        return 500;
    }
    public double getFontSize() {
        return fontSize;
    }
    public void setFontSize(double fontSize) {
        this.fontSize = fontSize;
    }
    public double getOffset() {
        return offset;
    }
    public void setOffset(double offset) {
        this.offset = offset;
    }
}
