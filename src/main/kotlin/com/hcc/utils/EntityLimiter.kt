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

package com.hcc.utils

import net.minecraft.client.Minecraft
import net.minecraft.entity.Entity

class EntityLimiter(val method: EntityLimiterMethods, var range: Int = -1) {

    val mc = Minecraft.getMinecraft()

    /**
     * Gets Entities that are allowed to render
     */
    fun getRenderingEntities(entities: List<Entity>) = when (method) {
        EntityLimiterMethods.RANGE -> entities
                .filter { mc.thePlayer.getDistanceToEntity(it) <= range }
        EntityLimiterMethods.ENTITY_COUNT -> entities
                .sortedBy { mc.thePlayer.getDistanceSqToEntity(it) }
                .subList(0, if (range < entities.size) range else entities.size)
        else -> entities
    }

    enum class EntityLimiterMethods {
        NONE,
        RANGE,
        ENTITY_COUNT,
    }
}