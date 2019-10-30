package com.chattriggers.ctjs.triggers

import cc.hyperium.event.render.RenderHUDEvent
import com.chattriggers.ctjs.engine.ILoader

class OnRenderTrigger(method: Any, triggerType: TriggerType, loader: ILoader) : OnTrigger(method, triggerType, loader) {
    private var triggerIfCanceled: Boolean = true

    /**
     * Sets if the render trigger should run if the event has already been canceled.
     * True by default.
     * @param bool Boolean to set
     * @return the trigger object for method chaining
     */
    fun triggerIfCanceled(bool: Boolean) = apply { this.triggerIfCanceled = bool }

    override fun trigger(vararg args: Any?) {
        if (args[0] !is RenderHUDEvent)
            throw IllegalArgumentException("Argument 0 must be a RenderHUDEvent")

        callMethod(*args)
    }
}
