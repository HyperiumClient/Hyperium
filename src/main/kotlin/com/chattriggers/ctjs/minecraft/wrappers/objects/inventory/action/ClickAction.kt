package com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.action

import com.chattriggers.ctjs.minecraft.wrappers.Player
import com.chattriggers.ctjs.utils.kotlin.External

@External
class ClickAction(slot: Int, windowId: Int) : Action(slot, windowId) {
    private lateinit var clickType: ClickType
    private var holdingShift = false
    private var itemInHand = Player.getPlayer()?.inventory?.currentItem == null

    fun getClickType(): ClickType = clickType

    /**
     * The type of click (REQUIRED)
     *
     * @param clickType the new click type
     */
    fun setClickType(clickType: ClickType) = apply {
        this.clickType = clickType
    }

    fun getHoldingShift(): Boolean = holdingShift

    /**
     * Whether the click should act as if shift is being held (defaults to false)
     *
     * @param holdingShift to hold shift or not
     */
    fun setHoldingShift(holdingShift: Boolean) = apply {
        this.holdingShift = holdingShift
    }

    fun getItemInHand(): Boolean = itemInHand

    /**
     * Whether the click should act as if an item is being held
     * (defaults to whether there actually is an item in the hand)
     *
     * @param itemInHand to be holding an item or not
     */
    fun setItemInHand(itemInHand: Boolean) = apply {
        this.itemInHand = itemInHand
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

    override fun complete() {
        var mode = 0

        if (clickType == ClickType.MIDDLE) {
            mode = 3
        } else if (slot == -999 && !itemInHand) {
            mode = 4
        } else if (holdingShift) {
            mode = 1
        }

        doClick(clickType.button, mode)
    }

    enum class ClickType(val button: Int) {
        LEFT(0), RIGHT(1), MIDDLE(2);
    }
}
