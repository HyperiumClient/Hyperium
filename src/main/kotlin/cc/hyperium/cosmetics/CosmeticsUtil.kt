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
package cc.hyperium.cosmetics

import cc.hyperium.Hyperium
import cc.hyperium.config.Settings
import cc.hyperium.purchases.EnumPurchaseType

object CosmeticsUtil {
    @JvmStatic
    fun shouldHide(type: EnumPurchaseType): Boolean {
        return if (Settings.SHOW_COSMETICS_EVERYWHERE) {
            false
        } else !Hyperium.INSTANCE.handlers.locationHandler.isLobbyOrHousing
    }

    @JvmStatic
    fun interpolate(yaw1: Float, yaw2: Float, percent: Float): Float {
        var rotation = (yaw1 + (yaw2 - yaw1) * percent) % 360.0f
        if (rotation < 0.0f) rotation += 360.0f
        return rotation
    }
}
