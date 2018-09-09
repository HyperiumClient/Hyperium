package com.chattriggers.ctjs.minecraft.wrappers.objects

import com.chattriggers.ctjs.utils.kotlin.MCChunk
import java.util.stream.Collectors

class Chunk(val chunk: MCChunk) {
    /**
     * Gets every entity in this chunk
     *
     * @return the entity list
     */
    fun getAllEntities() = this.chunk.entityLists.toList().flatten().map {
        Entity(it)
    }

    /**
     * Gets every entity in this chunk of a certain class
     *
     * @param clazz the class to filter for (Use `Java.type().class` to get this)
     * @return the entity list
     */
    fun getAllEntitiesOfType(clazz: Class<*>) = getAllEntities().filter {
        it.entity.javaClass == clazz
    }
}