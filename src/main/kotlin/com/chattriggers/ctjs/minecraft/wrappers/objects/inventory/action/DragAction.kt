package com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.action

import com.chattriggers.ctjs.utils.kotlin.External

//#if MC>10809
//$$ import com.chattriggers.ctjs.utils.kotlin.MCClickType
//#endif

@External
class DragAction(slot: Int, windowId: Int) : Action(slot, windowId) {
    private lateinit var clickType: ClickType
    private lateinit var stage: Stage

    fun getClickType(): ClickType = clickType

    /**
     * The type of click (REQUIRED)
     *
     * @param clickType the new click type
     */
    fun setClickType(clickType: ClickType) = apply {
        this.clickType = clickType
    }

    fun getStage(): Stage = stage

    /**
     * The stage of this drag (REQUIRED)
     * BEGIN is when beginning the drag
     * SLOT is for each slot being dragged into
     * END is for ending the drag
     *
     * @param stage the stage
     */
    fun setStage(stage: Stage) = apply {
        this.stage = stage
    }

    /**
     * Sets the type of click.
     * Possible values are: LEFT, RIGHT, MIDDLE
     *
     * @param clickType the click type
     * @return the current Action for method chaining
     */
    fun setClickString(clickType: String) = apply {
        this.clickType = ClickType.valueOf(clickType.toUpperCase())
    }

    /**
     * Sets the stage of this drag.
     * Possible values are: BEGIN, SLOT, END {@link #stage}
     *
     * @param stage the stage
     * @return the current Action for method chaining
     */
    fun setStageString(stage: String) = apply {
        this.stage = Stage.valueOf(stage.toUpperCase())
    }

    override fun complete() {
        val button = stage.stage and 3 or (clickType.button and 3) shl 2

        if (stage != Stage.SLOT) {
            slot = -999
            println("Enforcing slot of -999")
        }

        //#if MC<=10809
        doClick(button, 5)
        //#else
        //$$ doClick(button, MCClickType.QUICK_CRAFT)
        //#endif
    }

    enum class ClickType(val button: Int) {
        LEFT(0), RIGHT(1), MIDDLE(2)
    }

    enum class Stage(val stage: Int) {
        BEGIN(0), SLOT(1), END(2)
    }
}

