/*
 *  Hypixel Community Client, Client optimized for Hypixel Network
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

package cc.hyperium.event

import java.util.*
import java.util.concurrent.CopyOnWriteArraySet

/**
 * Contains utilities used to subscribe and invoke events
 *
 * @author Kevin
 * @since 10/02/2018 3:42 PM
 */
object EventBus {

    private val subscriptions = HashMap<Class<*>, CopyOnWriteArraySet<EventSubscriber>>()
    var disabled = false
    fun register(obj: Any) {
        val clazz = obj.javaClass
        for (method in clazz.declaredMethods) {
            method.getAnnotation(InvokeEvent::class.java) ?: continue
            val event = method.parameters.first().type ?: throw
            IllegalArgumentException("Couldn't find parameter inside of ${method.name}!")
            val priority = method.getAnnotation(InvokeEvent::class.java).priority
            method.isAccessible = true
            subscriptions.putIfAbsent(event, CopyOnWriteArraySet())
            subscriptions[event]!!.add(EventSubscriber(obj, method, priority))
        }
    }

    fun disable() {
        disabled = true
    };
    fun register(vararg obj: Any) =
            obj.forEach(this::register)

    fun unregister(obj: Any) =
            subscriptions.values.forEach { map ->
                map.removeIf { it.instance == obj }
            }

    fun unregister(clazz: Class<*>) =
            subscriptions.values.forEach { map ->
                map.removeIf { it.instance.javaClass == clazz }
            }

    fun post(event: Any) {
        if(disabled)
            return
        subscriptions[event.javaClass]
                ?.sortedByDescending { it.priority.value }
                ?.forEach { sub ->
                    try {
                        sub.method.invoke(sub.instance, event)
                    } catch (e: Exception) {
                        println("Failed to call " + event.javaClass.canonicalName + " in " + sub.instance.javaClass.canonicalName + " class, check below for errors!")
                        e.printStackTrace()
                    }
                }

    }
}