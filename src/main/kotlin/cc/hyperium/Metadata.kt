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
    get() = "1.2.3"

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
