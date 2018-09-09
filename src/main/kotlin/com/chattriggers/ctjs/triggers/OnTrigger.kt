package com.chattriggers.ctjs.triggers

import com.chattriggers.ctjs.engine.ILoader
import jdk.nashorn.internal.objects.Global

abstract class OnTrigger protected constructor(var method: Any, var type: TriggerType, protected var loader: ILoader) {
    var priority: Priority
        private set
    private var global: Global?

    init {
        priority = Priority.NORMAL
        global = null

        register()
    }

    /**
     * Sets a triggers priority using [Priority].
     * Highest runs first.
     * @param priority the priority of the trigger
     * @return the trigger for method chaining
     */
    fun setPriority(priority: Priority): OnTrigger {
        this.priority = priority
        return this
    }

    /**
     * Registers a trigger based on its type.
     * This is done automatically with TriggerRegister.
     * @return the trigger for method chaining
     */
    fun register() = apply {
        this.loader.addTrigger(this)
    }

    /**
     * Unregisters a trigger.
     * @return the trigger for method chaining
     */
    fun unregister() = apply {
        this.loader.removeTrigger(this)
    }

    protected fun callMethod(vararg args: Any) {
        this.loader.trigger(this, this.method, *args)
    }

    abstract fun trigger(vararg args: Any)

    enum class TriggerResult {
        CANCEL
    }

    enum class Priority {
        //LOWEST IS RAN LAST
        LOWEST, LOW, NORMAL, HIGH, HIGHEST
    }
}