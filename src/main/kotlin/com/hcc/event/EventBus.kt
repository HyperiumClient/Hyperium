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

package com.hcc.event

import com.esotericsoftware.reflectasm.MethodAccess
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
    private val firstSubscriptions = HashMap<Class<*>, CopyOnWriteArraySet<EventSubscriber>>()
    private val lastSubscriptions = HashMap<Class<*>, CopyOnWriteArraySet<EventSubscriber>>()

    fun register(obj: Any) {
        val clazz = obj.javaClass
        for(method in clazz.declaredMethods) {
            method.getAnnotation(InvokeEvent::class.java) ?: continue
            val event = method.parameters.first().type ?: throw
            IllegalArgumentException("Couldn't find parameter inside of ${method.name}!")
            val access = MethodAccess.get(clazz)
            val subscriber = EventSubscriber(obj, access, access.getIndex(method.name))
            //subscriptions.putIfAbsent(event, CopyOnWriteArraySet())
            println("PRIORITY: " + method.getAnnotation(InvokeEvent::class.java).priority)
            when(method.getAnnotation(InvokeEvent::class.java).priority){
                Priority.FIRST -> {
                    println("Registered first subscription!")
                    firstSubscriptions.putIfAbsent(event, CopyOnWriteArraySet())
                    firstSubscriptions[event]!!.add(subscriber)}
                Priority.LAST -> {
                    println("Registered last subscription!")
                    lastSubscriptions.putIfAbsent(event, CopyOnWriteArraySet())
                    lastSubscriptions[event]!!.add(subscriber)}
                Priority.IRRELEVANT -> {
                    println("Registered irrelevant subscription!")
                    subscriptions.putIfAbsent(event, CopyOnWriteArraySet())
                    subscriptions[event]!!.add(subscriber)}
            }
            //subscriptions[event]!!.add(subscriber)
        }
    }

    fun register(vararg obj: Any) =
            obj.forEach(this::register)

    fun unregister(obj: Any) =
            subscriptions.remove(obj.javaClass)

    fun unregister(clazz: Class<*>) =
            subscriptions.remove(clazz)

    fun post(event: Any) {
        // Notifies all classes subscribed to the event with "FIRST" priority first.
        firstSubscriptions[event.javaClass]?.forEach { sub ->
            sub.methodAccess.invoke(sub.instance, sub.mIndex, event)
            //println("Notified first subscriber!")
        }

        subscriptions[event.javaClass]?.forEach { sub ->
            sub.methodAccess.invoke(sub.instance, sub.mIndex, event)
            //println("Notified irrelevant subscriber!")
        }

        lastSubscriptions[event.javaClass]?.forEach { sub ->
            sub.methodAccess.invoke(sub.instance, sub.mIndex, event)
        }
    }
}