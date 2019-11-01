package com.chattriggers.ctjs.minecraft.objects.gui

import com.chattriggers.ctjs.engine.ILoader
import com.chattriggers.ctjs.minecraft.wrappers.Client
import com.chattriggers.ctjs.minecraft.wrappers.Player
import com.chattriggers.ctjs.triggers.OnRegularTrigger
import com.chattriggers.ctjs.triggers.TriggerType
import com.chattriggers.ctjs.utils.kotlin.External
import com.chattriggers.ctjs.utils.kotlin.NotAbstract
import net.minecraft.client.gui.GuiButton
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.renderer.GlStateManager
import org.lwjgl.input.Mouse

@External
@NotAbstract
abstract class Gui : GuiScreen() {
    private var onDraw: OnRegularTrigger? = null
    private var onClick: OnRegularTrigger? = null
    private var onKeyTyped: OnRegularTrigger? = null
    private var onMouseReleased: OnRegularTrigger? = null
    private var onMouseDragged: OnRegularTrigger? = null
    private var onActionPerformed: OnRegularTrigger? = null

    private var mouseX = 0
    private var mouseY = 0

    var doesPauseGame = false

    fun open() {
        GuiHandler.openGui(this)
    }

    fun close() {
        if (isOpen()) Player.getPlayer()?.closeScreen()
    }

    fun isOpen(): Boolean = Client.getMinecraft().currentScreen === this

    fun isControlDown(): Boolean = isCtrlKeyDown()
    fun isShiftDown(): Boolean = isShiftKeyDown()
    fun isAltDown(): Boolean = isAltKeyDown()

    /**
     * Registers a method to be ran while Gui is open.<br></br>
     * Registered method runs on draw.<br></br>
     * Arguments passed through to method:<br></br>
     * int mouseX<br></br>
     * int mouseY<br></br>
     * float partialTicks
     *
     * @param method the method to run
     * @return the trigger
     */
    fun registerDraw(method: Any): OnRegularTrigger? {
        onDraw = OnRegularTrigger(method, TriggerType.OTHER, getLoader())
        return onDraw
    }

    /**
     * Registers a method to be ran while Gui is open.<br></br>
     * Registered method runs on mouse click.<br></br>
     * Arguments passed through to method:<br></br>
     * int mouseX<br></br>
     * int mouseY<br></br>
     * int button
     *
     * @param method the method to run
     * @return the trigger
     */
    fun registerClicked(method: Any): OnRegularTrigger? {
        onClick = OnRegularTrigger(method, TriggerType.OTHER, getLoader())
        return onClick
    }

    /**
     * Registers a method to be ran while Gui is open.<br></br>
     * Registered method runs on key input.<br></br>
     * Arguments passed through to method:<br></br>
     * char typed character<br></br>
     * int key code
     *
     * @param method the method to run
     * @return the trigger
     */
    fun registerKeyTyped(method: Any): OnRegularTrigger? {
        onKeyTyped = OnRegularTrigger(method, TriggerType.OTHER, getLoader())
        return onKeyTyped
    }

    /**
     * Registers a method to be ran while Gui is open.<br></br>
     * Registered method runs on key input.<br></br>
     * Arguments passed through to method:<br></br>
     * mouseX<br></br>
     * mouseY<br></br>
     * clickedMouseButton<br></br>
     * timeSinceLastClick
     *
     * @param method the method to run
     * @return the trigger
     */
    fun registerMouseDragged(method: Any): OnRegularTrigger? {
        onMouseDragged = OnRegularTrigger(method, TriggerType.OTHER, getLoader())
        return onMouseDragged
    }

    /**
     * Registers a method to be ran while Gui is open.<br></br>
     * Registered method runs on mouse release.<br></br>
     * Arguments passed through to method:<br></br>
     * mouseX<br></br>
     * mouseY<br></br>
     * button
     *
     * @param method the method to run
     * @return the trigger
     */
    fun registerMouseReleased(method: Any): OnRegularTrigger? {
        onMouseReleased = OnRegularTrigger(method, TriggerType.OTHER, getLoader())
        return onMouseReleased
    }

    /**
     * Registers a method to be ran while Gui is open.<br></br>
     * Registered method runs when an action is performed (clicking a button)<br></br>
     * Arguments passed through to method:<br></br>
     * the button that is clicked
     *
     * @param method the method to run
     * @return the trigger
     */
    fun registerActionPerformed(method: Any): OnRegularTrigger? {
        onActionPerformed = OnRegularTrigger(method, TriggerType.OTHER, getLoader())
        return onActionPerformed
    }

    /**
     * Internal method to run trigger. Not meant for public use
     */
    override fun mouseClicked(mouseX: Int, mouseY: Int, button: Int) {
        super.mouseClicked(mouseX, mouseY, button)
        onClick?.trigger(mouseX, mouseY, button)
    }

    /**
     * Internal method to run trigger. Not meant for public use
     */
    override fun mouseReleased(mouseX: Int, mouseY: Int, button: Int) {
        super.mouseReleased(mouseX, mouseY, button)
        onMouseReleased?.trigger(mouseX, mouseY, button)
    }

    /**
     * Internal method to run trigger. Not meant for public use
     */
    override fun actionPerformed(button: GuiButton) {
        super.actionPerformed(button)
        onActionPerformed?.trigger(button.id)
    }

    /**
     * Internal method to run trigger. Not meant for public use
     */
    override fun mouseClickMove(mouseX: Int, mouseY: Int, clickedMouseButton: Int, timeSinceLastClick: Long) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick)
        onMouseDragged?.trigger(mouseX, mouseY, clickedMouseButton, timeSinceLastClick)
    }

    /**
     * Internal method to run trigger. Not meant for public use
     */
    override fun handleMouseInput() {
        super.handleMouseInput()

        val i = Mouse.getEventDWheel()

        when {
            i > 0 -> onClick?.trigger(mouseX, mouseY, -1)
            i < 0 -> onClick?.trigger(mouseX, mouseY, -2)
        }
    }

    /**
     * Internal method to run trigger. Not meant for public use
     */
    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        super.drawScreen(mouseX, mouseY, partialTicks)

        GlStateManager.pushMatrix()

        this.mouseX = mouseX
        this.mouseY = mouseY

        onDraw?.trigger(mouseX, mouseY, partialTicks)

        GlStateManager.popMatrix()
    }

    /**
     * Internal method to run trigger. Not meant for public use
     */
    override fun keyTyped(typedChar: Char, keyCode: Int) {
        super.keyTyped(typedChar, keyCode)

        onKeyTyped?.trigger(typedChar, keyCode)
    }

    /**
     * Internal method to run trigger. Not meant for public use
     */
    override fun doesGuiPauseGame() = doesPauseGame

    fun setDoesPauseGame(doesPauseGame: Boolean) = apply { this.doesPauseGame = doesPauseGame }

    /**
     * Add a base Minecraft button to the Gui
     *
     * @param buttonId   id for the button
     * @param x          X position of the button
     * @param y          Y position of the button
     * @param buttonText the label of the button
     */
    fun addButton(buttonId: Int, x: Int, y: Int, buttonText: String) {
        buttonList.add(GuiButton(buttonId, x, y, buttonText))
    }

    /**
     * Add a base Minecraft button to the Gui
     *
     * @param buttonId   id for the button
     * @param x          X position of the button
     * @param y          Y position of the button
     * @param width      the width of the button
     * @param height     the height of the button
     * @param buttonText the label of the button
     */
    fun addButton(buttonId: Int, x: Int, y: Int, width: Int = 200, height: Int = 20, buttonText: String) {
        buttonList.add(GuiButton(buttonId, x, y, width, height, buttonText))
    }

    fun setButtonVisibility(buttonId: Int, visible: Boolean) {
        buttonList.firstOrNull {
            it.id == buttonId
        }?.visible = visible
    }

    internal abstract fun getLoader(): ILoader
}
