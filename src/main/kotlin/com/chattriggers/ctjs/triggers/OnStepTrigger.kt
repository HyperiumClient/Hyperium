package com.chattriggers.ctjs.triggers

import com.chattriggers.ctjs.engine.ILoader
import com.chattriggers.ctjs.minecraft.wrappers.Client
import com.chattriggers.ctjs.utils.kotlin.External

@External
class OnStepTrigger(method: Any, loader: ILoader) : OnTrigger(method, TriggerType.STEP, loader) {
    private var fps: Long = 60L
    private var delay: Long? = null
    private var systemTime: Long = Client.getSystemTime()
    private var elapsed: Long = 0L

    /**
     * Sets the frames per second that the trigger activates.
     * This is limited to 1 step per second.
     * @param fps the frames per second to set
     * @return the trigger for method chaining
     */
    fun setFps(fps: Long) = apply {
        this.fps = if (fps < 1) 1L else fps
        this.systemTime = Client.getSystemTime() + 1000 / this.fps
    }

    /**
     * Sets the delay in seconds between the trigger activation.
     * This is limited to one step every second. This will override [.setFps].
     * @param delay The delay in seconds
     * @return the trigger for method chaining
     */
    fun setDelay(delay: Long) = apply {
        this.delay = if (delay < 1) 1L else delay
        this.systemTime = Client.getSystemTime() - this.delay!! * 1000
    }

    override fun trigger(vararg args: Any?) {
        if (delay == null) {
            // run trigger based on set fps value (60 per second by default)
            while (systemTime < Client.getSystemTime() + 1000 / fps) {
                callMethod(++elapsed)
                systemTime += 1000 / fps
            }
        } else {
            // run trigger based on set delay in seconds
            while (Client.getSystemTime() > systemTime + delay!! * 1000) {
                callMethod(++elapsed)
                systemTime += delay!! * 1000
            }
        }
    }
}
