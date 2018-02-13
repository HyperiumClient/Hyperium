/*
 *     Hypixel Community Client, Client optimized for Hypixel Network
 *     Copyright (C) 2018  HCC Dev Team
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

package com.hcc.gui.font;

import com.hcc.utils.TrueTypeFont;

import java.awt.*;

public enum Fonts {
    ARIAL(new Font("Monsterrat", Font.BOLD, 20));

    private Font font;

    private TrueTypeFont ttf;

    Fonts(Font font){
        this.font = font;
        this.ttf = new TrueTypeFont(font, true);
    }

    public Font getFont() {
        return font;
    }

    public TrueTypeFont getTrueTypeFont() {
        return ttf;
    }
}
