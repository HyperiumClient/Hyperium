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

package cc.hyperium.handlers.handlers.hud;


import java.util.HashMap;
import java.util.Map;

public class Maps {
    public static final Map<Integer, String> ENCHANTMENT_SHORT_NAME;
    public static final Map<Integer, String> POTION_SHORT_NAME;

    static {
        ENCHANTMENT_SHORT_NAME = new HashMap<Integer, String>() {
            {
                put(0, "P");
                put(1, "FP");
                put(2, "FF");
                put(3, "BP");
                put(4, "PP");
                put(5, "R");
                put(6, "AA");
                put(7, "T");
                put(8, "DS");
                put(9, "FW");
                put(16, "SH");
                put(17, "SM");
                put(18, "BoA");
                put(19, "KB");
                put(20, "FA");
                put(21, "L");
                put(32, "EFF");
                put(33, "ST");
                put(34, "UNB");
                put(35, "F");
                put(48, "POW");
                put(49, "PUN");
                put(50, "FLA");
                put(51, "INF");
                put(61, "LoS");
                put(62, "LU");
                put(70, "MEN");
            }
        };
        POTION_SHORT_NAME = new HashMap<Integer, String>() {
            {
                put(1, "Spe");
                put(2, "Slo");
                put(3, "Has");
                put(4, "Fat");
                put(5, "Str");
                put(6, "Hea");
                put(7, "Dmg");
                put(8, "Jum");
                put(9, "Nau");
                put(10, "Reg");
                put(11, "Res");
                put(12, "Fir");
                put(13, "Wat");
                put(14, "Inv");
                put(15, "Bli");
                put(16, "NV");
                put(17, "Hun");
                put(18, "Wea");
                put(19, "Poi");
                put(20, "Wit");
                put(21, "HBo");
                put(22, "Abs");
                put(23, "Sat");
                put(24, "Glo");
                put(25, "Lev");
                put(26, "Luc");
                put(27, "BLu");
            }
        };
    }
}
