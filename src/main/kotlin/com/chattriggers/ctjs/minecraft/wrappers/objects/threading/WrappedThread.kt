package com.chattriggers.ctjs.minecraft.wrappers.objects.threading

class WrappedThread(task: Runnable) : Thread({
    task.run()
})