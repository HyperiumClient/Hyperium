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
package cc.hyperium.config

import net.minecraft.client.resources.I18n

enum class Category(display: String) {
    // Settings Tab
    GENERAL("gui.category.general"),
    PERFORMANCE("gui.category.performance"),
    QOL("gui.category.qol"),
    COSMETICS("gui.category.cosmetics"),
    MISC("gui.category.misc"),
    MODS("gui.category.mods"),
    HYPIXEL("gui.category.hypixel"),

    // Mods
    ANIMATIONS("gui.subcategory.animations"),
    AUTO_GG("gui.subcategory.autogg"),
    VANILLA_ENHANCEMENTS("gui.subcategory.vanillaenhancements"),
    MOTION_BLUR("gui.subcategory.motionblur"),
    GLINTCOLORIZER("gui.subcategory.glintcolorizer"),
    ITEM_PHYSIC("gui.subcategory.itemphysics"),
    CHUNK_ANIMATOR("gui.subcategory.chunkanimator"),
    POPUP_EVENTS("gui.subcategory.popupevents"),
    BOSSBARMOD("gui.subcategory.bossbarmod"),
    ENTITYRADIUS("gui.subcategory.entityradius"),
    FOV_MODIFIER("gui.subcategory.fovmodifier"),
    DISCORD("gui.subcategory.discord"),
    OTHER("gui.subcategory.other");

    private val display = I18n.format(display)
    fun getDisplay(): String = I18n.format(display)
}
