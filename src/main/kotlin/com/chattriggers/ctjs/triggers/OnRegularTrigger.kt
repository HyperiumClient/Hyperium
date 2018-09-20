package com.chattriggers.ctjs.triggers

import com.chattriggers.ctjs.engine.ILoader

class OnRegularTrigger(method: Any, triggerType: TriggerType, loader: ILoader) : OnTrigger(method, triggerType, loader) {
    override fun trigger(vararg args: Any) {
        callMethod(*args)
    }
}
