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

package cc.hyperium.config;

public enum Category {

    // Settings Tab
    GENERAL("General"),
    IMPROVEMENTS("Improvements"),
    INTEGRATIONS("Integrations"),
    COSMETICS("Cosmetics"),
    ANIMATIONS("Animations"),
    MISC("Misc"),
    MODS("Mods"),
    HYPIXEL("Hypixel"),
    BUTTONS("Buttons"),
    MENUS("Menus"),
    ADDONS("Addons"),

    // Mods
    AUTOTIP("Autotip"),
    AUTO_GG("Auto GG"),
    LEVEL_HEAD("Levelhead"),
    REACH("Reach Display"),
    VANILLA_ENHANCEMENTS("Vanilla Enhancements"),
    CHROMAHUD("ChromaHUD"),
    KEYSTROKES("Keystrokes"),
    MOTION_BLUR("Motion Blur"),
    AUTOFRIEND("Auto Friend"),
    GLINTCOLORIZER("Glint Colorizer"),
    FNCOMPASS("Fortnite Compass"),
    TAB_TOGGLE("Tab Toggle"),
    ITEM_PHYSIC("Item Physics"),
    VICTORYROYALE("Victory Royale"),
    TOGGLESPRINT("Toggle Sprint"),
    CHUNK_ANIMATOR("Chunk Animator"),
    LEVELHEAD("Levelhead"),
    AUTOMYPOSITION("Auto MyPosition"),
    OTHER("Other");

    private String display;

    Category(String display) {
        this.display = display;
    }

    public String getDisplay() {
        return display;
    }
}
