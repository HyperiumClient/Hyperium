package com.chattriggers.ctjs.minecraft.listeners

import cc.hyperium.event.*
import cc.hyperium.event.client.TickEvent
import cc.hyperium.event.gui.GuiOpenEvent
import cc.hyperium.event.render.DrawBlockHighlightEvent
import cc.hyperium.event.render.RenderHUDEvent
import cc.hyperium.event.render.RenderWorldEvent
import cc.hyperium.event.world.item.ItemPickupEvent
import cc.hyperium.event.world.item.ItemTooltipEvent
import cc.hyperium.event.world.item.ItemTossEvent
import com.chattriggers.ctjs.minecraft.wrappers.Client
import com.chattriggers.ctjs.minecraft.wrappers.Scoreboard
import com.chattriggers.ctjs.minecraft.wrappers.World
import com.chattriggers.ctjs.minecraft.wrappers.objects.PlayerMP
import com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.Item
import com.chattriggers.ctjs.triggers.TriggerType
import com.chattriggers.ctjs.utils.kotlin.KotlinListener
import net.minecraft.entity.player.EntityPlayerMP
import org.lwjgl.input.Mouse
import org.lwjgl.opengl.GL11
import paulscode.sound.Vector3D

@KotlinListener
object ClientListener {
    private var ticksPassed: Int = 0
    private val mouseState: MutableMap<Int, Boolean>
    private val draggedState: MutableMap<Int, State>

    class State(val x: Float, val y: Float)

    init {
        ticksPassed = 0

        mouseState = mutableMapOf()
        draggedState = mutableMapOf()

        for (i in 0..4)
            mouseState[i] = false
    }

    @InvokeEvent
    fun onTick(event: TickEvent) {
        if (World.getWorld() == null) return

        TriggerType.TICK.triggerAll(ticksPassed)
        ticksPassed++

        Scoreboard.resetCache()
    }

    private fun handleMouseInput() {
        if (!Mouse.isCreated()) return

        for (button in 0..4) {
            handleDragged(button)

            // normal clicked
            if (Mouse.isButtonDown(button) == mouseState[button]) continue

            TriggerType.CLICKED.triggerAll(
                Client.getMouseX(),
                Client.getMouseY(),
                button,
                Mouse.isButtonDown(button)
            )

            mouseState[button] = Mouse.isButtonDown(button)

            // add new dragged
            if (Mouse.isButtonDown(button))
                draggedState[button] = State(Client.getMouseX(), Client.getMouseY())
            else if (draggedState.containsKey(button))
                draggedState.remove(button)
        }
    }

    private fun handleDragged(button: Int) {
        if (button !in draggedState)
            return

        TriggerType.DRAGGED.triggerAll(
            Client.getMouseX() - (draggedState[button]?.x ?: 0f),
            Client.getMouseY() - (draggedState[button]?.y ?: 0f),
            Client.getMouseX(),
            Client.getMouseY(),
            button
        )

        // update dragged
        draggedState[button] = State(Client.getMouseX(), Client.getMouseY())
    }

    @InvokeEvent
    fun onRenderWorld(event: RenderWorldEvent) {
        TriggerType.RENDER_WORLD.triggerAll(event.partialTicks)
    }

    @InvokeEvent
    fun onRenderGameOverlay(event: RenderHUDEvent) {
        GL11.glPushMatrix()
        TriggerType.RENDER_OVERLAY.trigger(event)
        GL11.glPopMatrix()
        TriggerType.STEP.triggerAll()

        handleMouseInput()
    }

    @InvokeEvent
    fun onGuiOpened(event: GuiOpenEvent) {
        TriggerType.GUI_OPENED.triggerAll(event)
    }

    @InvokeEvent
    fun onBlockHighlight(event: DrawBlockHighlightEvent) {
        if (event.target.blockPos == null) return

        val position = Vector3D(
            event.target.blockPos.x.toFloat(),
            event.target.blockPos.y.toFloat(),
            event.target.blockPos.z.toFloat()
        )

        TriggerType.BLOCK_HIGHLIGHT.triggerAll(event, position)
    }

    @InvokeEvent
    fun onPickupItem(event: ItemPickupEvent) {
        if (event.player !is EntityPlayerMP) return

        val player = event.player

        val item = event.item

        val position = Vector3D(
            item.posX.toFloat(),
            item.posY.toFloat(),
            item.posZ.toFloat()
        )
        val motion = Vector3D(
            item.motionX.toFloat(),
            item.motionY.toFloat(),
            item.motionZ.toFloat()
        )

        TriggerType.PICKUP_ITEM.triggerAll(
            Item(item.entityItem),
            PlayerMP(player),
            position,
            motion
        )
    }

    @InvokeEvent
    fun onDropItem(event: ItemTossEvent) {
        if (event.player !is EntityPlayerMP) return

        val player = event.player as EntityPlayerMP
        val entityItem = event.item

        val position = Vector3D(
            entityItem.posX.toFloat(),
            entityItem.posY.toFloat(),
            entityItem.posZ.toFloat()
        )
        val motion = Vector3D(
            entityItem.motionX.toFloat(),
            entityItem.motionY.toFloat(),
            entityItem.motionZ.toFloat()
        )

        TriggerType.DROP_ITEM.triggerAll(
            Item(entityItem.entityItem),
            PlayerMP(player),
            position,
            motion
        )
    }

    @InvokeEvent
    fun onItemTooltip(e: ItemTooltipEvent) {
        TriggerType.TOOLTIP.triggerAll(
            e.toolTip,
            Item(e.item)
        )
    }
}
