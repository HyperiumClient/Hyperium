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