package cc.hyperium.event

import cc.hyperium.Hyperium
import com.google.common.reflect.TypeToken
import java.util.concurrent.CopyOnWriteArrayList

object EventBus {

    private val subscriptions = hashMapOf<Class<*>, CopyOnWriteArrayList<EventSubscriber>>()

    /**
     * Registers all methods of a class into the event system with
     * the [InvokeEvent] annotation
     *
     * @param any An instance of the class which you would like to register as an event
     */
    @Suppress("UnstableApiUsage")
    fun register(any: Any) {
        // also contains the class itself
        val token = TypeToken.of(any.javaClass)
        val superClasses = token.types.rawTypes()

        // we also want to loop over the super classes, since declaredMethods only gets method in the class itself
        for (temp in superClasses) {
            val clazz = temp as Class<*>

            // iterates though all the methods in the class
            for (method in clazz.declaredMethods) {
                // all the information and error checking before the method is added such
                // as if it even is an event before the element even touches the HashMap
                if (method.getAnnotation(InvokeEvent::class.java) == null) {
                    continue
                }

                method.parameters[0] ?: throw IllegalArgumentException("Couldn't find parameter inside of ${method.name}")

                val event = method.parameters[0].type
                val priority = method.getAnnotation(InvokeEvent::class.java).priority
                method.isAccessible = true

                // where the method gets added to the event key inside of the subscription hashmap
                // the arraylist is either sorted or created before the element is added
                if (subscriptions.containsKey(event)) {
                    // sorts array on insertion
                    subscriptions[event]?.add(EventSubscriber(any, method, priority))
                    subscriptions[event]?.sortByDescending { priority.value }
                } else {
                    // event hasn't been added before so it creates a new instance
                    // sorting does not matter here since there is no other elements to compete against
                    subscriptions[event] = CopyOnWriteArrayList()
                    subscriptions[event]?.add(EventSubscriber(any, method, priority))
                    subscriptions[event]?.sortByDescending { priority.value }
                }
            }
        }
    }

    /**
     * Unregisters all methods of the class instance from the event system
     * inside of [subscriptions]
     *
     * @param any An instance of the class which you would like to register as an event
     */
    fun unregister(any: Any) {
        subscriptions.values.forEach { map ->
            map.removeIf {
                it.instance == any
            }
        }
    }

    /**
     * Unregisters all methods of the class from the event system
     * inside of [subscriptions]
     *
     * @param clazz An instance of the class which you would like to register as an event
     */
    fun unregister(clazz: Class<*>) {
        subscriptions.values.forEach { map ->
            map.removeIf {
                it.instance.javaClass == clazz
            }
        }
    }

    /**
     * Invokes all of the methods which are inside of the classes
     * registered to the event
     *
     * @param event Event that is being posted
     */
    fun post(event: Event) {
        for (sub in subscriptions.getOrDefault(event.javaClass, CopyOnWriteArrayList())) {
            try {
                sub.invoke(event)
            } catch (e: Throwable) {
                Hyperium.LOGGER.fatal("Failed to post ${event.javaClass.name}", e)
            }
        }
    }
}
