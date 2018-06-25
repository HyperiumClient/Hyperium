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

package cc.hyperium.event

import com.google.common.reflect.TypeToken
import java.lang.reflect.InvocationTargetException
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Contains utilities used to subscribe and invoke events
 *
 * @author Kevin
 * @since 10/02/2018 3:42 PM
 */
object EventBus {

    private val subscriptions = HashMap<Class<*>, CopyOnWriteArrayList<EventSubscriber>>()

    /**
     * Registers all methods of a class into the event system with
     * the {@link package me.kbrewster.blazeapi.api.event.InvokeEvent} annotation
     *
     * @param obj An instance of the class which you would like to register as an event
     */
    fun register(obj: Any) {
        // also contains the class itself
        val superClasses = TypeToken.of(obj.javaClass).types.rawTypes()

        // we also want to loop over the super classes, since declaredMethods only gets method in the class itself
        for (clazz in superClasses) {
            // iterates though all the methods in the class
            for (method in clazz.declaredMethods) {
                // all the informaton and error checking before the method is added such
                // as if it even is an event before the element even touches the hashmap
                method.getAnnotation(InvokeEvent::class.java) ?: continue
                val event = method.parameters.first().type ?: throw
                IllegalArgumentException("Couldn't find parameter inside of ${method.name}!")
                val priority = method.getAnnotation(InvokeEvent::class.java).priority
                method.isAccessible = true

                // where the method gets added to the event key inside of the subscription hashmap
                // the arraylist is either sorted or created before the element is added
                this.subscriptions.let { subs ->
                    if (subs.containsKey(event)) {
                        // sorts array on insertion
                        subs[event]?.add(EventSubscriber(obj, method, priority))
                        subs[event] = CopyOnWriteArrayList(subs[event]?.sortedByDescending { it.priority.value })
                    } else {
                        // event hasn't been added before so it creates a new instance
                        // sorting does not matter here since there is no other elements to compete against
                        subs[event] = CopyOnWriteArrayList()
                        subs[event]?.add(EventSubscriber(obj, method, priority))
                    }
                }
            }
        }
    }

    /**
     * Registers all methods of each class in the array into the event system with
     * the {@link package me.kbrewster.blazeapi.api.event.InvokeEvent} annotation
     *
     * @param obj An instance of the class which you would like to register as an event
     */
    fun register(vararg obj: Any) =
            obj.forEach(this::register)

    /**
     * Unregisters all methods of the class instance from the event system
     * inside of {@link #subscriptions}
     *
     * @param obj An instance of the class which you would like to register as an event
     */
    fun unregister(obj: Any) =
            this.subscriptions.values.forEach { map ->
                map.removeIf { it.instance == obj }
            }

    /**
     * Unregisters all methods of the class from the event system
     * inside of {@link #subscriptions}
     *
     * @param clazz An instance of the class which you would like to register as an event
     */
    fun unregister(clazz: Class<*>) =
            this.subscriptions.values.forEach { map ->
                map.removeIf { it.instance.javaClass == clazz }
            }

    /**
     * Invokes all of the methods which are inside of the classes
     * registered to the event
     *
     * @param event Event that is being posted
     */
    fun post(event: Any) {
        this.subscriptions[event.javaClass]
                ?.forEach { sub ->
                    try {
                        sub.method.invoke(sub.instance, event)
                    } catch (e: Exception) {
                        if (e is InvocationTargetException) {
                            e.targetException.printStackTrace()
                        }
                        e.printStackTrace()
                    }
                }
    }

}