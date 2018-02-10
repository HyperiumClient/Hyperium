package com.hcc.event

import com.esotericsoftware.reflectasm.MethodAccess
import java.util.concurrent.CopyOnWriteArraySet

/**
 * Contains utilities used to subscribe and invoke events
 *
 * @author Kevin
 * @since 10/02/2018 3:42 PM
 */
object EventBus {

    val subscriptions = HashMap<Class<*>, CopyOnWriteArraySet<EventSubscriber>>()

    fun register(obj: Any) {
        val clazz = obj.javaClass
        for(method in clazz.declaredMethods) {
            method.getAnnotation(InvokeEvent::class.java) ?: continue
            val event = method.parameters.first().type ?: throw
            IllegalArgumentException("Coundn't find parameter inside of ${method.name}!")
            val access = MethodAccess.get(clazz)
            val subscriber = EventSubscriber(obj, access, access.getIndex(method.name))
            subscriptions.putIfAbsent(event, CopyOnWriteArraySet())
            subscriptions[event]!!.add(subscriber)
        }
    }

    fun register(vararg obj: Any) =
            obj.forEach(this::register)

    fun unregister(obj: Any) =
            subscriptions.remove(obj.javaClass)

    fun unregister(clazz: Class<*>) =
            subscriptions.remove(clazz)

    fun post(event: Any) =
            subscriptions[event.javaClass]?.forEach { sub ->
                sub.methodAccess.invoke(sub.instance, sub.mIndex, event)
            }


}