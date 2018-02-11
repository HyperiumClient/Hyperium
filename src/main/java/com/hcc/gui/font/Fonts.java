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

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;

public enum Fonts {
    MONTSERRAT_BOLD(load("Montserrat-Bold")),
    MONTSERRAT_LIGHT(load("Montserrat-Light")),
    MONTSERRAT_REGULAR(load("Montserrat-Regular"));

    private FontRenderer renderer;
    Fonts(FontRenderer renderer){
        this.renderer = renderer;
    }
    private static FontRenderer load(String font){
       return new FontRenderer(
               Minecraft.getMinecraft().gameSettings,
               new ResourceLocation("textures/font/"+font+".png"),
               Minecraft.getMinecraft().getTextureManager(),
               false);
    }

    public FontRenderer get() {
        return renderer;
    }
}
