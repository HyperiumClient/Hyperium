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


/**
 * Contains String Constants of the mod
 */
@file:JvmName("Metadata")

package cc.hyperium

val MODID
    @JvmName("getModid")
    get() = "Hyperium"

val VERSION
    @JvmName("getVersion")
    get() = "1.1"

/**
 * @since 12 (Hyperium Build 12)
 */
val VERSION_ID
    @JvmName("getVersionID")
    get() = Hyperium.BUILD_ID

/**
 * @since 15 (Hyperium Build 15)
 */
val DEVELOPMENT
    @JvmName("isDevelopment")
    get() = false

/**
 * List of addons that are ported internally, addons in this list will not be loaded
 * Names in this list needs to match the addon's name in addon.json (CASE SENSITIVE)
 *
 * @since 20 (Hyperium Build 20)
 */
val BLACKLISTED
    @JvmName("getBlacklisted")
    get() = arrayOf(
        "AutoFriend",
        "Custom Crosshair Addon",
        "PlayTime addon",
        "Tab Toggle",
        "SidebarAddon",
        "BossbarAddon",
        "Auto Cheating",
        "Auto Dab",
        "FortniteCompassMod",
        "Item Physic"
    )
