package com.chattriggers.ctjs.minecraft.libs

import cc.hyperium.event.CancellableEvent
import com.chattriggers.ctjs.utils.Cancellable
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

object EventLib {
    /**
     * Cancel an event. Automatically used with `cancel(event)`.
     *
     * @param event the event to cancel
     * @throws IllegalArgumentException if event can be cancelled "normally"
     */
    @JvmStatic
    @Throws(IllegalArgumentException::class)
    fun cancel(event: Any) {
        when (event) {
            is CallbackInfoReturnable<*> -> {
                if (!event.isCancellable) return
                event.setReturnValue(null)
            }
            is CallbackInfo -> {
                if (!event.isCancellable) return
                event.cancel()
            }
            is CancellableEvent ->
                event.isCancelled = true
            is Cancellable ->
                event.isCancelled = true
            else -> throw IllegalArgumentException()
        }
    }
}