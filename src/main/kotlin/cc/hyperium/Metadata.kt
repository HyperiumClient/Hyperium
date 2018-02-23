/*
 *     Hypixel Community Client, Client optimized for Hypixel Network
 *     Copyright (C) 2018  Hyperium Dev Team
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

/**
 * Contains String Contants of the mod
 */
@file:JvmName("Metadata")

package cc.hyperium

val MODID
    @JvmName("getModid")
    get() = "Hyperium"

val VERSION
    @JvmName("getVersion")
    get() = "1.0 - Dev"

val AUTHORS
    @JvmName("getAuthors")
    get() = arrayOf("Kevin", "CoalOres", "Sk1er", "VRCube")

var FORGE = false
    @JvmName("isUsingForge") get

var OPTIFINE = false
    @JvmName("isUsingOptifine") get