package com.chattriggers.ctjs.utils.kotlin

import cc.hyperium.event.EventBus
import cc.hyperium.event.InvokeEvent
import com.chattriggers.ctjs.CTJS
import com.chattriggers.ctjs.engine.ILoader
import com.chattriggers.ctjs.engine.ModuleManager
import org.apache.logging.log4j.LogManager
import java.lang.reflect.Modifier
import kotlin.reflect.full.companionObjectInstance

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class KotlinListener

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class ModuleLoader

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class External

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class NotAbstract

object AnnotationHandler {
    private val LOGGER = LogManager.getLogger(AnnotationHandler::class.java)

    private val registered = mutableSetOf<Any>()

    fun subscribeAutomatic() {
        val ref = CTJS.reflections

        val listeners = ref.getTypesAnnotatedWith(KotlinListener::class.java)
        val loaders = ref.getTypesAnnotatedWith(ModuleLoader::class.java)

        listeners.forEach {
            try {
                val kotlinClass = it.kotlin
                val objectInstance = kotlinClass.objectInstance ?: kotlinClass.companionObjectInstance ?: return@forEach

                if (hasObjectEventHandlers(objectInstance) && objectInstance !in registered) {
                    EventBus.INSTANCE.register(objectInstance)
                    registered += objectInstance
                    LOGGER.debug("Registered @KotlinListener object instance {}", it.name)
                }

            } catch (e: Throwable) {
                LOGGER.error("An error occurred trying to load an @KotlinListener object {}", it.name)
                throw e
            }
        }

        loaders.forEach {
            try {
                val kotlinClass = it.kotlin
                val objectInstance = kotlinClass.objectInstance ?: kotlinClass.companionObjectInstance ?: return@forEach
                val loaderInstance: ILoader = objectInstance as? ILoader ?: return@forEach

                ModuleManager.loaders.add(loaderInstance)
            } catch (e: Throwable) {
                LOGGER.error("An error occurred trying to load an @ModuleLoader object {}", it.name)
                throw e
            }
        }
    }

    private fun hasObjectEventHandlers(objectInstance: Any): Boolean {
        return objectInstance.javaClass.methods.any {
            !Modifier.isStatic(it.modifiers) && it.isAnnotationPresent(InvokeEvent::class.java)
        }
    }
}
